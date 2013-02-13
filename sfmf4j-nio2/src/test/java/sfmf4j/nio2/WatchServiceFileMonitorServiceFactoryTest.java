/*
 * Copyright 2013 sswor.
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
package sfmf4j.nio2;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link WatchServiceFileMonitorServiceFactory}.
 *
 * @author sswor
 */
public class WatchServiceFileMonitorServiceFactoryTest {

    private ExecutorService executorService = null;
    private WatchService watchService = null;

    @Before
    public void setUp() throws IOException {
        executorService = Executors.newSingleThreadExecutor();
        watchService = FileSystems.getDefault().newWatchService();
    }

    @After
    public void tearDown() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ex) {
                //trap
            }
        }
    }

    /**
     * Tests {@link WatchServiceFileMonitorServiceFactory#getExecutorService()}
     * and
     * {@link WatchServiceFileMonitorServiceFactory#setExecutorServiceExecutorService)}.
     */
    @Test
    public void testGetExecutorServiceAndSetExecutorService() {
        WatchServiceFileMonitorServiceFactory instance = new WatchServiceFileMonitorServiceFactory();
        instance.setExecutorService(executorService);
        assertEquals(executorService, instance.getExecutorService());
    }

    /**
     * Tests {@link WatchServiceFileMonitorServiceFactory#getWatchService()} and
     * {@link WatchServiceFileMonitorServiceFactory#setWatchService(WatchService)}.
     */
    @Test
    public void testGetWatchServiceAndSetWatchService() {
        WatchServiceFileMonitorServiceFactory instance = new WatchServiceFileMonitorServiceFactory();
        instance.setWatchService(watchService);
        assertEquals(watchService, instance.getWatchService());
    }

    /**
     * Tests
     * {@link WatchServiceFileMonitorServiceFactory#createFileMonitorService()}.
     */
    @Test
    public void testCreateFileMonitorService() {
        WatchServiceFileMonitorServiceFactory instance = new WatchServiceFileMonitorServiceFactory();
        instance.setExecutorService(executorService);
        instance.setWatchService(watchService);
        WatchServiceFileMonitorServiceImpl service = instance.createFileMonitorService();
        assertNotNull(service);
        assertEquals(executorService, service.getExecutorService());
        assertEquals(watchService, service.getWatchService());
    }
}