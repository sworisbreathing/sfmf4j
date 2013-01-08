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
package sfmf4j.commonsio;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 * 
 * @author Steven Swor
 */
public class CommonsIOFileMonitorServiceImpl implements FileMonitorService {

    private final FileAlterationMonitor fileMonitor;
    
    private final ConcurrentMap<File, FileAlterationObserver> directoryObservers;

    public CommonsIOFileMonitorServiceImpl(FileAlterationMonitor fileMonitor) {
        this.fileMonitor = fileMonitor;
        this.directoryObservers = new ConcurrentHashMap<File, FileAlterationObserver>();
    }

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

    public void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
        FileAlterationObserver newObserver = new FileAlterationObserver(directory);
        FileAlterationObserver oldObserver = directoryObservers.putIfAbsent(directory, newObserver);
        final FileAlterationObserver observer;
        if (oldObserver == null) {
            observer = newObserver;
        } else {
            observer = oldObserver;
        }
        synchronized (observer) {
            observer.addListener(new SFMF4JFileAlterationListener(directoryListener));
        }
        if (observer == newObserver) {
            try {
                observer.initialize();
            }catch(Exception ex) {
                //trap
            }
        }

    }

    public void shutdown() {
        try {
            fileMonitor.stop();
        } catch (Exception ex) {
            //trap
        }
    }

    public void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
        SFMF4JFileAlterationListener listener = new SFMF4JFileAlterationListener(directoryListener);
        final FileAlterationObserver observer = directoryObservers.get(directory);
        if (observer != null) {
            boolean shouldDestroy = false;
            synchronized (observer) {
                observer.removeListener(listener);
                if (!observer.getListeners().iterator().hasNext()) {
                    shouldDestroy = true;
                    directoryObservers.remove(directory);
                }
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
