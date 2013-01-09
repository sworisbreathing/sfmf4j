/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test;

import javax.inject.Inject;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author sswor
 */
@RunWith(JUnit4TestRunner.class)
public abstract class AbstractOSGiTest {

    protected abstract Option implementationOption();

    protected abstract String implementationClassName();

    @Inject
    private FileMonitorServiceFactory factoryInstance;

    protected static String sfmf4jGroupId() {
        return System.getProperty("sfmf4j.groupId");
    }

    protected static String sfmf4jVersion() {
        return System.getProperty("sfmf4j.version");
    }

    @Configuration
    public Option[] config() throws Exception {
        return options(
                mavenBundle("org.apache.felix","org.apache.felix.configadmin","1.2.4"),
                mavenBundle("org.apache.aries","org.apache.aries.util","1.0.0"),
                mavenBundle("org.apache.aries.proxy","org.apache.aries.proxy","1.0.0"),
                mavenBundle("org.apache.aries.blueprint","org.apache.aries.blueprint","1.0.0"),
                wrappedBundle(mavenBundle(sfmf4jGroupId(),"sfmf4j-tests",sfmf4jVersion())),
                implementationOption(),
                junitBundles()
                );
    }

    @Test
    public void getFactory() {
        assertNotNull(factoryInstance);
        assertEquals(implementationClassName(), factoryInstance.getClass().getName());
    }
}
