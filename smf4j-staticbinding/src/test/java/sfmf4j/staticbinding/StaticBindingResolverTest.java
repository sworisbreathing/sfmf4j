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

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import sfmf4j.api.FileMonitorServiceFactory;
import sfmf4j.staticbinding.nop.NopFileMonitorServiceFactory;

/**
 * Unit tests for {@link StaticBindingResolver}.
 * 
 * @author Steven Swor
 */
public class StaticBindingResolverTest {

    /**
     * Ensures that {@link
     * StaticBindingResolver#resolveFileMonitorServiceFactory()} returns a no-op
     * instance when it can't find a concrete implementation on the classpath.
     */
    @Test
    public void testResolveFileMonitorServiceFactoryNop() {
        FileMonitorServiceFactory factory = StaticBindingResolver.resolveFileMonitorServiceFactory();
        assertTrue(factory instanceof NopFileMonitorServiceFactory);
    }
}
