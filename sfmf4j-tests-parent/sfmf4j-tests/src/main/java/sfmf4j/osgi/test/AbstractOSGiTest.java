/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
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

    @Test
    public void testFileMonitoring() throws Throwable {
        assertNotNull(factoryInstance);
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
                        barrier.await(10, TimeUnit.SECONDS);
                    }catch(Exception ex) {
                        //trap
                    }
                }

                public void fileChanged(File changed) {
                    modifiedFiles.add(changed);
                    try {
                        barrier.await(10, TimeUnit.SECONDS);
                    }catch(Exception ex) {
                        //trap
                    }
                }

                public void fileDeleted(File deleted) {
                    deletedFiles.add(deleted);
                    try {
                        barrier.await(10, TimeUnit.SECONDS);
                    }catch(Exception ex) {
                        //trap
                    }
                }
            });

            File newFile = tempFolder.newFile();
            barrier.await(10, TimeUnit.SECONDS);
            assertEquals(1, createdFiles.size());
            assertEquals(newFile.getAbsolutePath(), createdFiles.get(0).getAbsolutePath());
        }finally {
            fileMonitor.shutdown();
        }

    }
}
