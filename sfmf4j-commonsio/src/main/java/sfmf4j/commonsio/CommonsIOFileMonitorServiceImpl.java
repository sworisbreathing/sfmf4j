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
package sfmf4j.commonsio;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 * File monitor service which uses a Commons-IO {@link FileAlterationMonitor}.
 *
 * @author Steven Swor
 */
public class CommonsIOFileMonitorServiceImpl implements FileMonitorService {

    /**
     * The file monitor.
     */
    private final FileAlterationMonitor fileMonitor;
    /**
     * The observers for each directory.
     */
    private final ConcurrentMap<File, FileAlterationObserver> directoryObservers;

    /**
     * Creates a new CommonsIOFileMonitorServiceImpl.
     *
     * @param fileMonitor the file monitor
     */
    public CommonsIOFileMonitorServiceImpl(FileAlterationMonitor fileMonitor) {
        this.fileMonitor = fileMonitor;
        this.directoryObservers = new ConcurrentHashMap<File, FileAlterationObserver>();
    }

    @Override
    public void initialize() {
        try {
            fileMonitor.start();
        } catch (Exception ex) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public boolean isMonitoringDirectory(File directory) {
        return directoryObservers.containsKey(directory);
    }

    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public synchronized void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
        FileAlterationObserver newObserver = new FileAlterationObserver(directory);
        FileAlterationObserver oldObserver = directoryObservers.putIfAbsent(directory, newObserver);
        final FileAlterationObserver observer;
        if (oldObserver == null) {
            observer = newObserver;
        } else {
            observer = oldObserver;
        }
        SFMF4JFileAlterationListener newListener = new SFMF4JFileAlterationListener(directoryListener);
        boolean found = false;
        for (FileAlterationListener fal : observer.getListeners()) {
            if (newListener.equals(fal)) {
                found = true;
                break;
            }
        }
        if (!found) {
            observer.addListener(newListener);
        }
        if (observer == newObserver) {
            try {
                observer.initialize();
                fileMonitor.addObserver(observer);
            } catch (Exception ex) {
                //trap
            }
        }

    }

    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public void shutdown() {
        try {
            fileMonitor.stop();
        } catch (Exception ex) {
            //trap
        }
    }

    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public synchronized void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
        SFMF4JFileAlterationListener listener = new SFMF4JFileAlterationListener(directoryListener);
        final FileAlterationObserver observer = directoryObservers.get(directory);
        if (observer != null) {
            boolean shouldDestroy = false;
            observer.removeListener(listener);
            if (!observer.getListeners().iterator().hasNext()) {
                shouldDestroy = true;
                directoryObservers.remove(directory);
                fileMonitor.removeObserver(observer);
            }
            if (shouldDestroy) {
                try {
                    observer.destroy();
                } catch (Exception ex) {
                    //trap
                }
            }
        }
    }
}
