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

import com.github.sworisbreathing.sfmf4j.api.FileMonitorServiceFactory;
import com.github.sworisbreathing.sfmf4j.test.AbstractNonOSGiTest;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for commons-io implementation.
 * @author Steven Swor
 */
public class CommonsIoTest extends AbstractNonOSGiTest<CommonsIOFileMonitorServiceFactory> {

    @Override
    protected FileMonitorServiceFactory factoryInstance() {
        CommonsIOFileMonitorServiceFactory instance = new CommonsIOFileMonitorServiceFactory();
        instance.setPollingInterval(250);
        instance.setPollingTimeUnit(TimeUnit.MILLISECONDS);
        return instance;
    }

    @Override
    protected Class<CommonsIOFileMonitorServiceFactory> implementationClass() {
        return CommonsIOFileMonitorServiceFactory.class;
    }
}
