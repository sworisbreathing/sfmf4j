/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author sswor
 */
public abstract class AbstractSFMF4JTest {

    protected long eventTimeoutDuration() {
        return 10;
    }

    protected TimeUnit eventTimeoutTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    protected static String sfmf4jGroupId() {
        return System.getProperty("sfmf4j.groupId");
    }

    protected static String sfmf4jVersion() {
        return System.getProperty("sfmf4j.version");
    }

    protected abstract FileMonitorServiceFactory factoryInstance();

    private FileMonitorServiceFactory factoryInstance = null;

    private FileMonitorService fileMonitor = null;

    @Before
    public void setUp() throws Exception{
        factoryInstance = factoryInstance();
        assert (factoryInstance != null);
        fileMonitor = factoryInstance.createFileMonitorService();
        assert (fileMonitor != null);
        fileMonitor.initialize();
    }

    @After
    public void tearDown() throws Exception {
        if (fileMonitor != null) {
            fileMonitor.shutdown();
        }
        fileMonitor = null;
        factoryInstance = null;
    }

    @Test
    public void testFileMonitoring() throws Throwable {
        final Logger logger = LoggerFactory.getLogger(getClass());
        File newFile = null;
        final BlockingQueue<File> createdFiles = new LinkedBlockingQueue<File>();
        final BlockingQueue<File> modifiedFiles = new LinkedBlockingQueue<File>();
        final BlockingQueue<File> deletedFiles = new LinkedBlockingQueue<File>();
        final DirectoryListener listener = new DirectoryListener() {
                public void fileCreated(File created) {
                    createdFiles.add(created);
                }

                public void fileChanged(File changed) {
                    modifiedFiles.add(changed);
                }

                public void fileDeleted(File deleted) {
                    deletedFiles.add(deleted);
                }
            };
        File folder = null;
        try {
            folder = tempFolder.getRoot();
            fileMonitor.registerDirectoryListener(folder, listener);

            /*
             * Create a new file.
             */
            newFile = tempFolder.newFile();
            logger.debug("Test file: {}", newFile.getAbsolutePath());
            logger.info("Testing for created event.");
            File created = createdFiles.poll(eventTimeoutDuration(), eventTimeoutTimeUnit());
            assertNotNull(created);
            assertEquals(newFile.getAbsolutePath(), created.getAbsolutePath());

            /*
             * Write to the file.
             */
            logger.info("Testing for modification event.");
            FileOutputStream fileOut = null;
            byte[] bytes = new byte[4096];
            try {
                fileOut = new FileOutputStream(newFile);
                fileOut.write(bytes);
            } finally {
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (Exception ex) {
                        //trap.  We'll have bigger fish to fry if this happens
                    }
                }
            }
            File modified = modifiedFiles.poll(eventTimeoutDuration(), eventTimeoutTimeUnit());
            assertNotNull(modified);
            assertEquals(newFile.getAbsolutePath(), modified.getAbsolutePath());
            /*
             * Test deletion.
             */
            newFile.delete();
            File deleted = deletedFiles.poll(eventTimeoutDuration(), eventTimeoutTimeUnit());
            assertNotNull(deleted);
            assertEquals(newFile.getAbsolutePath(), deleted.getAbsolutePath());
        } finally {
            if (folder!=null) {
                try {
                    fileMonitor.unregisterDirectoryListener(folder, listener);
                }catch(Exception ex) {
                    //trap
                }
            }
            if (newFile != null && newFile.exists()) {
                try {
                    newFile.delete();
                }catch(Exception ex) {
                    //trap
                }
            }
            fileMonitor.shutdown();
        }

    }
}
