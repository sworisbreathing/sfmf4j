/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

/**
 *
 * @author sswor
 */
public class CommonsIOOSGiTest extends AbstractOSGiTest {

    public CommonsIOOSGiTest(){
        super(new SFMF4JImplementationOptionBuilder() {

            public Option sfmf4jOption() {
                final String groupId = System.getProperty("project.groupId");
                final String version = System.getProperty("project.version");
                return mavenBundle(groupId, "sfmf4j-commonsio", version);
            }
        });
    }
}
