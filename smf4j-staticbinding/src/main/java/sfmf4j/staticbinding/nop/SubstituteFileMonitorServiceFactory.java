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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import sfmf4j.api.FileMonitorService;
import sfmf4j.api.FileMonitorServiceFactory;

/**
 *
 * @author Steven Swor
 */
public class SubstituteFileMonitorServiceFactory implements FileMonitorServiceFactory {
    
    private final Set<File> folders= new LinkedHashSet<File>();
    
    public Set<File> getFolders() {
        return folders;
    }

    @Override
    public FileMonitorService createFileMonitorService(long pollingInterval, TimeUnit pollingTimeUnit) {
        LoggerFactory.getLogger(SubstituteFileMonitorServiceFactory.class).warn("Substitute factory created");
        return new SubstituteFileMonitorService(this);
    }

    
}
