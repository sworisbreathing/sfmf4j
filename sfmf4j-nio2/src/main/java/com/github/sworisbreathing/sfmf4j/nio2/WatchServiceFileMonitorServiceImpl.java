/*
 * Copyright 2012-2013 Steven Swor.
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
package com.github.sworisbreathing.sfmf4j.nio2;

import com.github.sworisbreathing.sfmf4j.api.DirectoryListener;
import com.github.sworisbreathing.sfmf4j.api.FileMonitorService;
import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jpathwatch implementation of a file monitor service.
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceImpl implements FileMonitorService {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(WatchServiceFileMonitorServiceImpl.class);

    /**
     * Future for the asynchronous task which polls the watch service.
     */
    private Future watchFuture = null;

    /**
     * The watch service.
     */
    private WatchService watchService;

    /**
     * The watch kinds we are interested in.
     * @see StandardWatchEventKind#ENTRY_CREATE
     * @see StandardWatchEventKind#ENTRY_DELETE
     * @see StandardWatchEventKind#ENTRY_MODIFY
     */
    private static final Kind[] interested_types = new Kind[]{StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY};

    /**
     * The executor service which runs the task of polling the watch service.
     */
    private ExecutorService executorService;

    /**
     * Flag to indicate that the watch service should be stopped during
     * shutdown.
     */
    private volatile boolean closeWatchServiceOnShutdown = false;

    /**
     * Flag to indiate that the executor service should be stopped during
     * shutdown.
     */
    private volatile boolean shutdownExecutorServiceOnShutdown = false;


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

    /**
     * The registered watch keys for each path.
     */
    private final ConcurrentMap<String, WatchKey> watchKeysByPath;

    /**
     * The registered paths for each watch key.
     */
    private final ConcurrentMap<WatchKey, String> pathsByWatchKey;

    /**
     * The listeners for each watch key.
     */
    private final ConcurrentMap<WatchKey, Collection<SFMF4JWatchListener>> listenersByWatchKey;

    /**
     * Creates a new WatchServiceFileMonitorServiceImpl.
     * @param watchService the watch service to use.  When this argument is
     * {@code null}, the watch service will be created during {@link
     * #initialize()} and stopped during {@link #shutdown()}.  If this argument
     * is not {@code null}, then it is assumed that the watch service is running
     * and will be closed elsewhere (for example, an IoC container)
     * @param executorService the executor service to use.  When this argument
     * is {@code null}, the executor service will be created during {@link
     * #initialize()} and stopped during {@link #shutdown()}.  If this argument
     * is not {@code null}, then it is assumed that the executor service is
     * running and will be closed elsewhere (for example, in an IoC container)
     */
    public WatchServiceFileMonitorServiceImpl(final WatchService watchService, final ExecutorService executorService) {
        this.watchService = watchService;
        this.executorService = executorService;
        this.watchKeysByPath = new ConcurrentHashMap<String, WatchKey>();
        this.pathsByWatchKey = new ConcurrentHashMap<WatchKey, String>();
        this.listenersByWatchKey = new ConcurrentHashMap<WatchKey, Collection<SFMF4JWatchListener>>();
    }

    /**
     * Gets the watch key for a path with lazy initialization.
     * @param path the path
     * @return a registered watch key for the path
     * @throws IOException if lazy initialization fails
     */
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

    /**
     * Resolves a watch event with its absolute path.
     * @param key the watch key (used to look up the parent path)
     * @param event the event to resolve
     * @return a copy of the event, with a resolved path
     */
    private synchronized WatchEvent<Path> resolveEventWithCorrectPath(final WatchKey key, final WatchEvent<Path> event) {
        Path correctPath = Paths.get(pathsByWatchKey.get(key));
        return new ResolvedPathWatchEvent(event, correctPath);
    }

    /**
     * Properly unregisters and removes a watch key.
     * @param key the watch key
     */
     @SuppressWarnings("PMD.EmptyCatchBlock")
    private synchronized void cleanup(final WatchKey key) {
        logger.trace("cleanUp {}", key);
        try {
            key.cancel();
        }catch(Exception ex) {

        }
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
        if (watchService==null) {
            logger.warn("No watch service was explicitly set.  Setting one now.");
            closeWatchServiceOnShutdown = true;
            try {
                this.watchService = FileSystems.getDefault().newWatchService();
            }catch(IOException ex) {
                throw new RuntimeException(ex.getLocalizedMessage(), ex);
            }
        }
        if (executorService==null) {
            logger.warn("No executor service was explicitly set.  Setting one now.");
            shutdownExecutorServiceOnShutdown = true;
            this.executorService = Executors.newCachedThreadPool(new ThreadFactory(){

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "FileMonitorService");
                }
            });
        }
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
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public synchronized void shutdown() {
        watchFuture.cancel(true);
        for (WatchKey key : pathsByWatchKey.keySet()) {
            cleanup(key);
        }
        if (shutdownExecutorServiceOnShutdown) {
            executorService.shutdownNow();
        }
        if (closeWatchServiceOnShutdown) {
            try {
                watchService.close();
            }catch(IOException ex) {
                //trap
            }
        }
    }

    @Override
    public synchronized boolean isMonitoringDirectory(File directory) {
        return !getExecutorService().isShutdown() && watchKeysByPath.containsKey(directory.getAbsolutePath());
    }


}
