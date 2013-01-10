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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 *
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceImpl implements FileMonitorService {

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
    private final ConcurrentMap<SFMF4JWatchListener, WatchKey> watchKeysByListener;
    private final ConcurrentMap<WatchKey, SFMF4JWatchListener> listenersByWatchKey;
    private final ConcurrentMap<Path, WatchKey> keysByPath;
    private final ConcurrentMap<WatchKey, Path> pathsByKey;

    public WatchServiceFileMonitorServiceImpl(final WatchService watchService, final ExecutorService executorService) {
        this.watchService = watchService;
        this.executorService = executorService;
        this.watchKeysByListener = new ConcurrentHashMap<SFMF4JWatchListener, WatchKey>();
        this.listenersByWatchKey = new ConcurrentHashMap<WatchKey, SFMF4JWatchListener>();
        this.keysByPath = new ConcurrentHashMap<Path, WatchKey>();
        this.pathsByKey = new ConcurrentHashMap<WatchKey, Path>();
    }

    @Override
    public void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
        Path path = Paths.get(directory.getAbsolutePath());
        try {
            WatchKey key = path.register(watchService, interested_types);
            SFMF4JWatchListener listener = new SFMF4JWatchListener(directoryListener);
            if (watchKeysByListener.putIfAbsent(listener, key) != null) {
                key.cancel();
            } else {
                listenersByWatchKey.put(key, listener);
                keysByPath.put(path, key);
                pathsByKey.put(key, path);
            }
        } catch (IOException ex) {
            Logger.getLogger(WatchServiceFileMonitorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
        SFMF4JWatchListener listener = new SFMF4JWatchListener(directoryListener);
        WatchKey key = watchKeysByListener.remove(listener);
        if (key != null) {
            key.cancel();
            listenersByWatchKey.remove(key);
            Path path = pathsByKey.remove(key);
            keysByPath.remove(path);
        }
    }

    private WatchEvent<Path> resolveEventWithCorrectPath(final WatchKey key, final WatchEvent<Path> event) {
        Path correctPath = pathsByKey.get(key);
        return new ResolvedPathWatchEvent(event, correctPath);
    }

    @Override
    public void initialize() {
        watchFuture = executorService.submit(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        WatchKey key = watchService.take();
                        SFMF4JWatchListener listener = listenersByWatchKey.get(key);
                        if (listener != null) {
                            for (WatchEvent event : key.pollEvents()) {
                                WatchEvent<Path> resolvedEvent = resolveEventWithCorrectPath(key, event);
                                listener.onEvent(resolvedEvent);
                            }
                        }
                        key.reset();
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
    public void shutdown() {
        watchFuture.cancel(true);
        for (SFMF4JWatchListener listener : watchKeysByListener.keySet()) {
            WatchKey key = watchKeysByListener.remove(listener);
            if (key != null) {
                key.cancel();
                listenersByWatchKey.remove(key);
            }
        }
    }
}
