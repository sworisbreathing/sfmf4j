/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi;

import javax.inject.Inject;
import org.junit.After;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
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

    protected AbstractOSGiTest(final SFMF4JImplementationOptionBuilder resolver){
        this.implementationResolver = resolver;
    }
    private final SFMF4JImplementationOptionBuilder implementationResolver;
    
    protected void doBefore() throws Exception {}
    
    protected void doAfter() throws Exception {}
    
    @After
    public void tearDown() throws Exception {
        doAfter();
    }
    @Inject
    private FileMonitorServiceFactory factoryInstance;

    @Configuration
    public Option[] config() throws Exception {
        doBefore();
        return options(
                mavenBundle("org.apache.felix","org.apache.felix.configadmin","1.2.4"),
                mavenBundle("org.apache.aries","org.apache.aries.util","1.0.0"),
                mavenBundle("org.apache.aries.proxy","org.apache.aries.proxy","1.0.0"),
                mavenBundle("org.apache.aries.blueprint","org.apache.aries.blueprint","1.0.0"),
                implementationResolver.sfmf4jOption(),
                junitBundles()
                );
    }

    @Test
    public void getFactory() {
        assertNotNull(factoryInstance);
        assertEquals(implementationResolver.getExpectedClassName(), factoryInstance.getClass().getName());
    }
}
