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
package sfmf4j.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sfmf4j.api.FileMonitorService;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 * Parent class for testing implementations in a typical J2SE environment.
 *
 * @author sswor
 */
public abstract class AbstractNonOSGiTest extends AbstractSFMF4JTest {

    /**
     * Gets the implementation of {@link FileMonitorServiceFactory} being
     * tested.
     * @return the implementation being tested
     */
    protected abstract FileMonitorServiceFactory factoryInstance();

    /**
     * The implementation of {@link FileMonitorServiceFactory} being tested.
     */
    private FileMonitorServiceFactory factoryInstance = null;

    /**
     * The implementation of {@link FileMonitorService} being tested.
     */
    private FileMonitorService fileMonitor = null;

    /**
     * Creates the {@link FileMonitorService} implementation being tested (from
     * the {@link FileMonitorServiceFactory}) and initializes it, prior to
     * testing.
     */
    @Before
    public void setUp() {
        factoryInstance = factoryInstance();
        assert (factoryInstance != null);
        fileMonitor = factoryInstance.createFileMonitorService();
        assert (fileMonitor != null);
        fileMonitor.initialize();
    }

    /**
     * Shuts down the {@link FileMonitorService} after testing.
     */
    @After
    public void tearDown() {
        if (fileMonitor != null) {
            fileMonitor.shutdown();
        }
        fileMonitor = null;
        factoryInstance = null;
    }

    /**
     * Runs the test against the file monitor implementation.
     * @throws Throwable if the test fails
     */
    @Test
    public void testFileMonitoring() throws Throwable {
        doTestFileMonitoring(fileMonitor);
    }
}
