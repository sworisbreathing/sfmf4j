/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test.impl;

import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import org.ops4j.pax.exam.Option;
import sfmf4j.osgi.test.AbstractOSGiTest;

/**
 *
 * @author sswor
 */
public class JPathWatchOSGiTest extends AbstractOSGiTest {

    @Override
    protected Option implementationOption() {
        return composite(
                mavenBundle("jpathwatch", "jpathwatch", "0.94"),
                mavenBundle(sfmf4jGroupId(), "sfmf4j-jpathwatch", sfmf4jVersion()));
    }

    @Override
    protected String implementationClassName() {
        return "sfmf4j.jpathwatch.WatchServiceFileMonitorServiceFactory";
    }
}
