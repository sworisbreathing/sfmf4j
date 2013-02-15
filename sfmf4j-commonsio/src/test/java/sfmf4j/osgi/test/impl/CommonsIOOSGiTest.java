/*
 * Copyright 2012-2013 Steven Swor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sfmf4j.osgi.test.impl;

import static org.ops4j.pax.exam.CoreOptions.*;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.util.PathUtils;
import sfmf4j.osgi.test.AbstractOSGiTest;

/**
 *
 * @author sswor
 */
public class CommonsIOOSGiTest extends AbstractOSGiTest {

    @Override
    protected Option implementationOption() {
        //return mavenBundle(sfmf4jGroupId(), "sfmf4j-commonsio", sfmf4jVersion());
        return composite(mavenBundle("commons-io", "commons-io", "2.4")
                ,bundle("reference:file:" + PathUtils.getBaseDir() + "/target/classes")
                );
    }
}
