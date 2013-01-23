/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi.test.impl;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import org.ops4j.pax.exam.Option;
import sfmf4j.osgi.test.AbstractOSGiTest;

/**
 *
 * @author sswor
 */
public class CommonsIOOSGiTest extends AbstractOSGiTest {

    @Override
    protected Option implementationOption() {
        return mavenBundle(sfmf4jGroupId(), "sfmf4j-commonsio", sfmf4jVersion());
    }
}
