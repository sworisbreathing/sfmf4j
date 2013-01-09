/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.nio2;

import java.util.Iterator;
import java.util.ServiceLoader;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author sswor
 */
public class ServiceLoaderTest {


    @Test
    public void testServiceLoader() {
        ServiceLoader<FileMonitorServiceFactory> serviceLoader = ServiceLoader.load(FileMonitorServiceFactory.class);
        assertNotNull(serviceLoader);
        Iterator<FileMonitorServiceFactory> iterator = serviceLoader.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        FileMonitorServiceFactory instance = iterator.next();
        assertNotNull(instance);
        assertTrue(instance instanceof WatchServiceFileMonitorServiceFactory);
    }
}
