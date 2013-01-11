/*
 * Copyright 2012 Steven Swor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sfmf4j.jpathwatch;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 *
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceImpl implements FileMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(WatchServiceFileMonitorServiceImpl.class);
    private Future watchFuture = null;
    private final WatchService watchService;
    private static final Kind[] interested_types = new Kind[]{StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE, StandardWatchEventKind.ENTRY_MODIFY};
    private final ExecutorService executorService;

    /**
     * Package-protected getter (for automated testing).
     *
     * @return
     */
    ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Package-protected getter (for automated testing).
     *
     * @return
     */
    WatchService getWatchService() {
        return watchService;
    }
    private final ConcurrentMap<String, WatchKey> watchKeysByPath;
    private final ConcurrentMap<WatchKey, String> pathsByWatchKey;
    private final ConcurrentMap<WatchKey, Collection<SFMF4JWatchListener>> listenersByWatchKey;

    public WatchServiceFileMonitorServiceImpl(final WatchService watchService, final ExecutorService executorService) {
        this.watchService = watchService;
        this.executorService = executorService;
        this.watchKeysByPath = new ConcurrentHashMap<String, WatchKey>();
        this.pathsByWatchKey = new ConcurrentHashMap<WatchKey, String>();
        this.listenersByWatchKey = new ConcurrentHashMap<WatchKey, Collection<SFMF4JWatchListener>>();
    }

    private synchronized WatchKey getWatchKeyForPath(final String path) throws IOException {
        WatchKey key = watchKeysByPath.get(path);
        if (key == null) {
            logger.debug("Lazy-instantiating watch key for path: {}", path);
            key = Paths.get(path).register(watchService, interested_types);
            watchKeysByPath.put(path, key);
            pathsByWatchKey.put(key, path);
            listenersByWatchKey.put(key, Collections.newSetFromMap(new ConcurrentHashMap<SFMF4JWatchListener, Boolean>()));
        }
        return key;
    }

    @Override
    public void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
        String path = directory.getAbsolutePath();
        try {
            synchronized (this) {
                WatchKey key = getWatchKeyForPath(path);
                SFMF4JWatchListener listener = new SFMF4JWatchListener(directoryListener);
                listenersByWatchKey.get(key).add(listener);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
        String path = directory.getAbsolutePath();
        synchronized (this) {
            final WatchKey key = watchKeysByPath.get(path);
            if (key != null) {
                Collection<SFMF4JWatchListener> listeners = listenersByWatchKey.get(key);
                listeners.remove(new SFMF4JWatchListener(directoryListener)); // note the equals implementation
                if (listeners.isEmpty()) {
                    //no longer listening on that path.
                    cleanup(key);
                } else {
                    logger.debug("somebody is still listening: {}", path);
                }
            }
        }
    }

    private synchronized WatchEvent<Path> resolveEventWithCorrectPath(final WatchKey key, final WatchEvent<Path> event) {
        Path correctPath = Paths.get(pathsByWatchKey.get(key));
        return new ResolvedPathWatchEvent(event, correctPath);
    }

    private synchronized void cleanup(final WatchKey key) {
        logger.trace("cleanUp {}", key);
        key.cancel();
        Collection<SFMF4JWatchListener> listeners = listenersByWatchKey.remove(key);
        if (listeners != null && !listeners.isEmpty()) {
            logger.warn("Cleaning up key but listeners are still registered.");
        }
        String path = pathsByWatchKey.remove(key);
        if (path != null) {
            watchKeysByPath.remove(path);
        }

    }

    @Override
    public synchronized void initialize() {
        watchFuture = executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Collection<SFMF4JWatchListener> listeners;
                        List<WatchEvent<?>> events;
                        final WatchKey key = watchService.take();
                        synchronized (this) {
                            listeners = listenersByWatchKey.get(key);
                            if (listeners != null && !listeners.isEmpty()) {
                                listeners = new LinkedList<SFMF4JWatchListener>(listeners);
                                events = key.pollEvents();
                                boolean stillValid = key.reset();
                                if (!stillValid) {
                                    logger.warn("Key no longer valid.");
                                    cleanup(key);
                                } else {
                                    logger.debug("Key is still valid.");
                                    if (events != null && !events.isEmpty()) {
                                        for (WatchEvent event : events) {
                                            WatchEvent<Path> resolvedEvent = resolveEventWithCorrectPath(key, event);
                                            logger.debug("Event kind={} count={} path={}", new Object[]{resolvedEvent.kind().name(), resolvedEvent.count(), resolvedEvent.context()});
                                            for (SFMF4JWatchListener listener : listeners) {
                                                listener.onEvent(resolvedEvent);
                                            }
                                        }
                                    }
                                }
                            } else {
                                logger.debug("No listeners found for valid key... cleaning up.");
                                cleanup(key);
                            }
                        }
                    } catch (InterruptedException ex) {
                        return;
                    } catch (ClosedWatchServiceException ex) {
                        return;
                    }
                }
            }
        });
    }

    @Override
    public synchronized void shutdown() {
        watchFuture.cancel(true);
        for (WatchKey key : pathsByWatchKey.keySet()) {
            cleanup(key);
        }
    }

    public synchronized boolean isMonitoringDirectory(File directory) {
        return !getExecutorService().isShutdown() && watchKeysByPath.containsKey(directory.getAbsolutePath());
    }


}
