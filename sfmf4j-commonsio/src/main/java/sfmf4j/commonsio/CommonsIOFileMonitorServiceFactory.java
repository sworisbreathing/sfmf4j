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

import java.util.concurrent.TimeUnit;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import sfmf4j.api.FileMonitorService;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author Steven Swor
 */
public class CommonsIOFileMonitorServiceFactory implements FileMonitorServiceFactory {

    /**
     * Creates a new {@link CommonsIOFileMonitorServiceImpl}.
     * @param pollingInterval the polling interval
     * @param pollingTimeUnit the time unit for the polling interval.
     * @return a new {@link CommonsIOFileMonitorServiceImpl}, which wraps a
     * newly-created {@link FileAlterationMonitor}
     */
    public FileMonitorService createFileMonitorService(long pollingInterval, TimeUnit pollingTimeUnit) {
        long pollingIntervalMillis = pollingTimeUnit.toMillis(pollingInterval);
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(pollingIntervalMillis);
        return new CommonsIOFileMonitorServiceImpl(fileAlterationMonitor);
    }
}
