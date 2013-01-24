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
import sfmf4j.api.FileMonitorServiceFactory;

/**
 * jpathwatch implementation of FileMonitorServiceFactory.
 * @author Steven Swor
 */
public class WatchServiceFileMonitorServiceFactory implements FileMonitorServiceFactory {

    /**
     * The executor service.
     */
    private ExecutorService executorService;

    /**
     * The watch service.
     */
    private WatchService watchService;

    /**
     * Creates a new WatchServiceFileMonitorServiceFactory.
     */
    public WatchServiceFileMonitorServiceFactory() {
    }

    /**
     * Gets the executor service used to monitor the watch service for events.
     * @return the executor service
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Gets the watch service used to track file system events.
     * @return the watch service
     */
    public WatchService getWatchService() {
        return this.watchService;
    }

    /**
     * Sets the executor service used to monitor the watch service for events.
     * @param executorService the executor service
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Sets the watch service used to track file system events.
     * @param watchService the watch service
     */
    public void setWatchService(WatchService watchService) {
        this.watchService = watchService;
    }

    @Override
    public WatchServiceFileMonitorServiceImpl createFileMonitorService() {
        WatchServiceFileMonitorServiceImpl instance = new WatchServiceFileMonitorServiceImpl(watchService, executorService);
        return instance;
    }


}
