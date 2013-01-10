/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Constants;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author sswor
 */
@RunWith(JUnit4TestRunner.class)
public abstract class AbstractOSGiTest {

    protected abstract Option implementationOption();

    protected abstract String implementationClassName();
    
    protected long eventTimeoutDuration(){return 10;}
    
    protected TimeUnit eventTimeoutTimeUnit(){return TimeUnit.SECONDS;}

    @Inject
    private FileMonitorServiceFactory factoryInstance;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    protected static String sfmf4jGroupId() {
        return System.getProperty("sfmf4j.groupId");
    }

    protected static String sfmf4jVersion() {
        return System.getProperty("sfmf4j.version");
    }

    @Configuration
    public Option[] config() throws Exception {
        return options(
                mavenBundle("org.slf4j","slf4j-api").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("org.codehaus.groovy","groovy-all").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("ch.qos.logback","logback-core").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle("ch.qos.logback","logback-classic").startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
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
    public void testFileMonitoring() throws Throwable {
        assertNotNull(factoryInstance);
        final Logger logger = LoggerFactory.getLogger(getClass());
        FileMonitorService fileMonitor = factoryInstance.createFileMonitorService();
        fileMonitor.initialize();
        try {
            final File folder = tempFolder.getRoot();

            final List<File> createdFiles = new LinkedList<File>();
            final List<File> modifiedFiles = new LinkedList<File>();
            final List<File> deletedFiles = new LinkedList<File>();
            final CyclicBarrier barrier = new CyclicBarrier(2);
            fileMonitor.registerDirectoryListener(folder, new DirectoryListener() {

                public void fileCreated(File created) {
                    createdFiles.add(created);
                    try {
                        barrier.await(eventTimeoutDuration(), eventTimeoutTimeUnit());
                    }catch(Exception ex) {
                        //trap
                    }
                }

                public void fileChanged(File changed) {
                    modifiedFiles.add(changed);
                    try {
                        barrier.await(eventTimeoutDuration(), eventTimeoutTimeUnit());
                    }catch(Exception ex) {
                        //trap
                    }
                }

                public void fileDeleted(File deleted) {
                    deletedFiles.add(deleted);
                    try {
                        barrier.await(eventTimeoutDuration(), eventTimeoutTimeUnit());
                    }catch(Exception ex) {
                        //trap
                    }
                }
            });

            /*
             * Create a new file.
             */
            File newFile = tempFolder.newFile();
            logger.debug("Test file: {}", newFile.getAbsolutePath());
            logger.info("Testing for created event.");
            barrier.await(eventTimeoutDuration(), eventTimeoutTimeUnit());
            barrier.reset();
            assertEquals(1, createdFiles.size());
            assertEquals(newFile.getAbsolutePath(), createdFiles.get(0).getAbsolutePath());

            /*
             * Write to the file.
             */
            logger.info("Testing for modification event.");
            FileOutputStream fileOut = null;
            byte[] bytes = new byte[4096];
            try {
                fileOut = new FileOutputStream(newFile);
                fileOut.write(bytes);
            }finally{
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch(Exception ex) {
                        //trap.  We'll have bigger fish to fry if this happens
                    }
                }
            }
            barrier.await(eventTimeoutDuration(), eventTimeoutTimeUnit());
            barrier.reset();
            assertEquals(1, modifiedFiles.size());
            assertEquals(newFile.getAbsolutePath(), modifiedFiles.get(0).getAbsolutePath());
        }finally {
            fileMonitor.shutdown();
        }

    }
}
