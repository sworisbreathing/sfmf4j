/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.jpathwatch;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.bridge.SLF4JBridgeHandler;
import sfmf4j.api.FileMonitorServiceFactory;
import sfmf4j.test.AbstractSFMF4JTest;

/**
 *
 * @author sswor
 */
public class JPathWatchTest extends AbstractSFMF4JTest {

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


}
