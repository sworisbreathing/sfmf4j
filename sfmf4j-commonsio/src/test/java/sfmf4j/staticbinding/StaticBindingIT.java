/*
 *  Copyright 2012 Steven Swor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package sfmf4j.staticbinding;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sfmf4j.api.FileMonitorServiceFactory;
import sfmf4j.commonsio.CommonsIOFileMonitorServiceFactory;

/**
 *
 * @author Steven Swor
 */
public class StaticBindingIT {
    
    @Test
    public void testStaticBinding() {
        FileMonitorServiceFactory instance = StaticBindingResolver.resolveFileMonitorServiceFactory();
        assertTrue(instance instanceof CommonsIOFileMonitorServiceFactory);
    }
}
