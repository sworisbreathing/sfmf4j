/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sfmf4j.osgi;

import org.ops4j.pax.exam.Option;

/**
 *
 * @author sswor
 */
public interface SFMF4JImplementationOptionBuilder {

    Option sfmf4jOption();
    
    String getExpectedClassName();

}
