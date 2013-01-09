/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi;

import org.junit.Ignore;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import org.ops4j.pax.exam.Option;
import sfmf4j.osgi.test.AbstractOSGiTest;

/**
 *
 * @author sswor
 */
@Ignore
public class NIO2OSGiTest extends AbstractOSGiTest {

    @Override
    protected Option implementationOption() {
        return mavenBundle(sfmf4jGroupId(), "sfmf4j-nio2", sfmf4jVersion());
    }

    @Override
    protected String implementationClassName() {
         return "sfmf4j.nio2.WatchServiceFileMonitorServiceFactory";
    }

}
