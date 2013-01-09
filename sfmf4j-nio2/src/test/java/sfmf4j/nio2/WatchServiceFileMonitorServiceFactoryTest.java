/*
 * Copyright 2013 Steven Swor.
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

import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceFactoryTest {

    @Test
    public void testConstructor() throws Exception {
        ExecutorService dummyService = Executors.newFixedThreadPool(1);
        dummyService.shutdown();
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchService.close();
        WatchServiceFileMonitorServiceFactory instance = new WatchServiceFileMonitorServiceFactory(dummyService, watchService);
        assertSame(dummyService, instance.getExecutorService());
        assertSame(watchService, instance.getWatchService());
    }

    @Test
    public void testCreateFileMonitorService() throws Exception {
        ExecutorService dummyService = Executors.newFixedThreadPool(1);
        dummyService.shutdown();
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchService.close();
        WatchServiceFileMonitorServiceFactory instance = new WatchServiceFileMonitorServiceFactory(dummyService, watchService);
        WatchServiceFileMonitorServiceImpl service = instance.createFileMonitorService();
        assertSame(dummyService, service.getExecutorService());
        assertSame(watchService, service.getWatchService());
        
    }
    
}
