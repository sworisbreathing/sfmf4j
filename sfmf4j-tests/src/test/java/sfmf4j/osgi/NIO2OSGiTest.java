/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi;

import java.util.concurrent.Executors;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import org.ops4j.pax.exam.Option;

/**
 *
 * @author sswor
 */
public class NIO2OSGiTest extends AbstractOSGiTest {

    public NIO2OSGiTest(){
        super(new SFMF4JImplementationOptionBuilder() {

            @Override
            public Option sfmf4jOption() {
                Executors.newSingleThreadExecutor();
                return mavenBundle("sfmf4j", "sfmf4j-nio2", "1.0-SNAPSHOT");
            }

            @Override
            public String getExpectedClassName() {
                return "sfmf4j.nio2.WatchServiceFileMonitorServiceFactory";
            }
            
            
        });
    }
}
