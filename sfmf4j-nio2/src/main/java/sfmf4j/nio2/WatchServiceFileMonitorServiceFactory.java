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

package sfmf4j.nio2;

import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceFactory implements FileMonitorServiceFactory {

    private ExecutorService executorService;
    
    private WatchService watchService;

    public WatchServiceFileMonitorServiceFactory() {
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
    
    public WatchService getWatchService() {
        return this.watchService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setWatchService(WatchService watchService) {
        this.watchService = watchService;
    }
    
    @Override
    public WatchServiceFileMonitorServiceImpl createFileMonitorService() {
        WatchServiceFileMonitorServiceImpl instance = new WatchServiceFileMonitorServiceImpl(watchService, executorService);
        return instance;
    }
    
    
}
