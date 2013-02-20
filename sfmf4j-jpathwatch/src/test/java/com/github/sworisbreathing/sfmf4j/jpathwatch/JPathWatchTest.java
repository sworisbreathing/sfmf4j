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
package com.github.sworisbreathing.sfmf4j.jpathwatch;

import com.github.sworisbreathing.sfmf4j.api.FileMonitorServiceFactory;
import com.github.sworisbreathing.sfmf4j.test.AbstractNonOSGiTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 *
 * @author sswor
 */
public class JPathWatchTest extends AbstractNonOSGiTest<WatchServiceFileMonitorServiceFactory> {

    private static java.util.logging.Level originalLevel = null;

    /**
     * Installs the SLF4J-JUL bridge and enables all JUL logging.
     */
    @BeforeClass
    public static void setUpClass() {
        SLF4JBridgeHandler.install();
        java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger("");
        originalLevel = julLogger.getLevel();
        julLogger.setLevel(java.util.logging.Level.ALL);
    }

    /**
     * Uninstalls the SLF4J-JUL bridge and returns JUL logging to its original
     * level.
     */
    @AfterClass
    public static void tearDownClass() {
        SLF4JBridgeHandler.uninstall();
        java.util.logging.Logger.getLogger("").setLevel(originalLevel);
    }

    @Override
    protected FileMonitorServiceFactory factoryInstance() {
        return new WatchServiceFileMonitorServiceFactory();
    }

    @Override
    protected Class<WatchServiceFileMonitorServiceFactory> implementationClass() {
        return WatchServiceFileMonitorServiceFactory.class;
    }
}
