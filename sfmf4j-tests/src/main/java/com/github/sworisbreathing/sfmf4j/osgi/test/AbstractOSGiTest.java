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
package com.github.sworisbreathing.sfmf4j.osgi.test;

import com.github.sworisbreathing.sfmf4j.api.FileMonitorService;
import com.github.sworisbreathing.sfmf4j.api.FileMonitorServiceFactory;
import com.github.sworisbreathing.sfmf4j.test.AbstractSFMF4JTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Constants;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.util.PathUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Parent class for testing implementations in an OSGi environment.
 * @author sswor
 */
@RunWith(JUnit4TestRunner.class)
public abstract class AbstractOSGiTest extends AbstractSFMF4JTest {

    /**
     * The required options to provide the implementation to the OSGi container.
     * At the very least, this should include a reference to the
     * implementation's class files or bundled JAR.
     * @return the required options to provide the implementation to the OSGi
     * container
     */
    protected abstract Option implementationOption();

    /**
     * The bundle context, used to obtain service instances.
     */
    @Inject
    private BundleContext bundleContext;

    /**
     * The configuration admin service, used to configure the service instance.
     */
    @Inject
    private ConfigurationAdmin configurationAdmin;

    /**
     * Gets the SFMF4J group ID from the system property {@code sfmf4j.groupId}.
     * @return the SFMf4J group ID
     */
    protected static String sfmf4jGroupId() {
        return System.getProperty("sfmf4j.groupId", "com.github.sworisbreathing.sfmf4j");
    }

    /**
     * Gets the SFMF4J version from the system property {@code sfmf4j.version}.
     * @return the SFMF4J version
     */
    protected static String sfmf4jVersion() {
        return System.getProperty("sfmf4j.version", "1.0-SNAPSHOT");
    }

    /**
     * Gets the Pax Exam configuration for the OSGi tests.
     * @return the Pax Exam configuration to run the tests
     */
    @Configuration
    public Option[] config() {
        return options(
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("NONE"),
                systemProperty("logback.configurationFile").value("file:" + PathUtils.getBaseDir() + "/src/test/resources/logback.groovy"),
                // copy most options from PaxExamRuntime.defaultTestSystemOptions(),
                // except RBC and Pax Logging
                bootDelegationPackage( "sun.*" ),
                frameworkStartLevel( Constants.START_LEVEL_TEST_BUNDLE ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.exam.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.exam.inject.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.extender.service.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.osgi.compendium.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.base.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.swissbox.core.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.swissbox.extender.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.swissbox.framework.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.ops4j.pax.swissbox.lifecycle.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                url( "link:classpath:META-INF/links/org.apache.geronimo.specs.atinject.link" ).startLevel( Constants.START_LEVEL_SYSTEM_BUNDLES ),
                wrappedBundle(mavenBundle("com.carrotsearch","junit-benchmarks","0.4.0")).startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.slf4j", "slf4j-api","1.7.2").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.codehaus.groovy", "groovy-all", "2.0.5").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("ch.qos.logback", "logback-core","1.0.9").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("ch.qos.logback", "logback-classic","1.0.9").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.2.4"),
                mavenBundle("org.apache.aries", "org.apache.aries.util", "1.0.0"),
                mavenBundle("org.apache.aries.proxy", "org.apache.aries.proxy", "1.0.0"),
                mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint", "1.0.0"),
                mavenBundle(sfmf4jGroupId(), "sfmf4j-api", sfmf4jVersion()),
                wrappedBundle(mavenBundle(sfmf4jGroupId(), "sfmf4j-tests", sfmf4jVersion())),
                implementationOption(),
                junitBundles());
    }

    /**
     * The file monitor service.
     */
    private FileMonitorService fileMonitor = null;

    protected void configure(final ConfigurationAdmin configAdmin) {}

    /**
     * Initializes the file monitor service before running the tests.
     */
    @Before
    public void setUp() {
        configure(configurationAdmin);
        FileMonitorServiceFactory factoryInstance = factoryInstance();
        assertNotNull(factoryInstance);
        fileMonitor = factoryInstance.createFileMonitorService();
        fileMonitor.initialize();
    }

    /**
     * Gets the service instance from the bundle context.
     * @return the service instance
     */
    private FileMonitorServiceFactory factoryInstance() {
        final CountDownLatch latch = new CountDownLatch(1);
        bundleContext.addServiceListener(new ServiceListener() {

            @Override
            public void serviceChanged(ServiceEvent event) {
                ServiceReference ref = event.getServiceReference();
                if (ref.isAssignableTo(ref.getBundle(), FileMonitorServiceFactory.class.getName())) {
                    latch.countDown();
                }
            }
        });
        FileMonitorServiceFactory results = null;
        do {
            ServiceReference<FileMonitorServiceFactory> ref = bundleContext.getServiceReference(FileMonitorServiceFactory.class);
            if (ref != null) {
                results = bundleContext.getService(ref);
            }
            if (results == null) {
                try {
                    latch.await(1, TimeUnit.SECONDS);
                }catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }while(results == null);
        return results;
    }

    /**
     * Shuts down the file monitor service after running the tests.
     */
    @After
    public void tearDown() {
        if (fileMonitor != null) {
            fileMonitor.shutdown();
        }
        fileMonitor = null;
    }

    /**
     * Runs the file system monitoring tests in the OSGi container.
     * @throws Throwable if the test fails
     */
    @Test
    public void testFileMonitoring() throws Throwable {
        doTestFileMonitoring(fileMonitor);
    }
}
