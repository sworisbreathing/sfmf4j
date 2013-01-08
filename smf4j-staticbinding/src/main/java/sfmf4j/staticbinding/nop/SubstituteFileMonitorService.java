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
import org.slf4j.LoggerFactory;
import sfmf4j.api.DirectoryListener;
import sfmf4j.api.FileMonitorService;

/**
 *
 * @author Steven Swor
 */
public class SubstituteFileMonitorService implements FileMonitorService {
    
    private final SubstituteFileMonitorServiceFactory factory;

    public SubstituteFileMonitorService(SubstituteFileMonitorServiceFactory factory) {
        this.factory = factory;
    }

    public void initialize() {
        LoggerFactory.getLogger(SubstituteFileMonitorService.class).warn("Initializing substitute file monitor service");
    }

    public void registerDirectoryListener(File directory, DirectoryListener directoryListener) {
        factory.getFolders().add(directory);
    }

    public void shutdown() {
    }

    public void unregisterDirectoryListener(File directory, DirectoryListener directoryListener) {
    }

    
}
