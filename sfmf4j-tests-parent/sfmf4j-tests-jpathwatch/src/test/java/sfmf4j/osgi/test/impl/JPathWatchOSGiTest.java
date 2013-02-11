/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test.impl;

import org.ops4j.pax.exam.Constants;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
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
                wrappedBundle(mavenBundle("jpathwatch", "jpathwatch", "0.95")).startLevel(Constants.START_LEVEL_SYSTEM_BUNDLES),
                mavenBundle(sfmf4jGroupId(), "sfmf4j-jpathwatch", sfmf4jVersion()));
    }
}
