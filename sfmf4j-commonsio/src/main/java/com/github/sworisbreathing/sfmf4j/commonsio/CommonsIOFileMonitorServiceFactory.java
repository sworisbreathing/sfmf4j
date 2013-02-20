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

package com.github.sworisbreathing.sfmf4j.commonsio;

import com.github.sworisbreathing.sfmf4j.api.FileMonitorService;
import com.github.sworisbreathing.sfmf4j.api.FileMonitorServiceFactory;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.monitor.FileAlterationMonitor;

/**
 * Commons-IO implementation of FileMonitorServiceFactory.
 * @author Steven Swor
 */
public class CommonsIOFileMonitorServiceFactory implements FileMonitorServiceFactory {

    /**
     * The polling interval.
     */
    private volatile long pollingInterval = 1;

    /**
     * The polling time interval.
     */
    private volatile TimeUnit pollingTimeUnit = TimeUnit.MINUTES;

    /**
     * Gets the polling interval.
     * @return the polling interval
     */
    public long getPollingInterval() {
        return pollingInterval;
    }

    /**
     * Sets the polling interval.
     * @param pollingInterval the polling interval
     */
    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    /**
     * Gets the polling time unit.
     * @return the polling time unit
     */
    public TimeUnit getPollingTimeUnit() {
        return pollingTimeUnit;
    }

    /**
     * Sets the polling time unit.
     * @param pollingTimeUnit the polling time unit
     */
    public void setPollingTimeUnit(TimeUnit pollingTimeUnit) {
        this.pollingTimeUnit = pollingTimeUnit;
    }

    /**
     * Creates a new {@link CommonsIOFileMonitorServiceImpl}.
     * @param pollingInterval the polling interval
     * @param pollingTimeUnit the time unit for the polling interval.
     * @return a new {@link CommonsIOFileMonitorServiceImpl}, which wraps a
     * newly-created {@link FileAlterationMonitor}
     */
    @Override
    public FileMonitorService createFileMonitorService() {
        long pollingIntervalMillis = getPollingTimeUnit().toMillis(getPollingInterval());
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(pollingIntervalMillis);
        return new CommonsIOFileMonitorServiceImpl(fileAlterationMonitor);
    }
}
