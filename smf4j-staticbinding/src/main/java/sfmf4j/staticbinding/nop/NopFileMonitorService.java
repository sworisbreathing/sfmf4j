/*
 * Copyright 2012 Steven Swor.
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

package sfmf4j.staticbinding.nop;

import java.io.File;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 *
 * @author Steven Swor
 */
public class NopFileMonitorService implements FileMonitorService {

    public void initialize() {
    }

    public void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
    }

    public void shutdown() {
    }

    public void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
    }
    
}
