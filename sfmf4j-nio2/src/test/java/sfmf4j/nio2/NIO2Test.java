/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.nio2;

import sfmf4j.api.FileMonitorServiceFactory;
import sfmf4j.test.AbstractNonOSGiTest;

/**
 *
 * @author sswor
 */
public class NIO2Test extends AbstractNonOSGiTest {

    @Override
    protected FileMonitorServiceFactory factoryInstance() {
        return new WatchServiceFileMonitorServiceFactory();
    }


}
