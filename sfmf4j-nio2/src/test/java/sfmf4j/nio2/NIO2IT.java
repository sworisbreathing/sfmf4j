/*
 *  Copyright 2013 Steven Swor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package sfmf4j.nio2;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.WatchService;
import java.util.concurrent.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Steven Swor
 */
public class NIO2IT {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private WatchService watchService = null;
    private ExecutorService executor = null;
    private CyclicBarrier barrier = null;
    private FileTrackingListenerImpl listener = null;
    private File folder = null;
    private WatchServiceFileMonitorServiceFactory factory = null;
    private WatchServiceFileMonitorServiceImpl fileMonitorService = null;

    @Before
    public void setUp() throws Exception {
        watchService = FileSystems.getDefault().newWatchService();
        executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NIO2 Integration Test File Watcher");
            }
        });
        barrier = new CyclicBarrier(2);
        listener = new FileTrackingListenerImpl() {

            @Override
            public void fileChanged(File changed) {
                super.fileChanged(changed);
                try {
                    barrier.await();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void fileCreated(File created) {
                super.fileCreated(created);
                try {
                    barrier.await();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void fileDeleted(File deleted) {
                super.fileDeleted(deleted);
                try {
                    barrier.await();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        folder = tempFolder.getRoot();
        factory = new WatchServiceFileMonitorServiceFactory(executor, watchService);
        fileMonitorService = factory.createFileMonitorService(0, null);
        fileMonitorService.initialize();
        fileMonitorService.registerDirectoryListener(folder, listener);

    }

    @After
    public void tearDown() throws Exception {
        if (fileMonitorService != null) {
            fileMonitorService.unregisterDirectoryListener(folder, listener);
            fileMonitorService.shutdown();
            fileMonitorService = null;
        }
        factory = null;
        listener = null;
        barrier = null;
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        if (watchService != null) {
            watchService.close();
            watchService = null;
        }
    }

    @Test
    public void performTest() throws Exception {
        File newFile = tempFolder.newFile();
        barrier.await();
        assertEquals(1, listener.getCreatedFiles().size());
        File created = listener.getCreatedFiles().get(0);
        assertTrue(Files.isSameFile(newFile.toPath(), created.toPath()));
    }
}
