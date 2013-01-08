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

package sfmf4j.api;

import java.io.File;

/**
 * A file monitor service.
 * 
 * @author Steven Swor
 */
public interface FileMonitorService {
    /**
     * Registers interest in receiving notifications when a directory's contents
     * have changed.
     * @param directory the directory
     * @param directoryListener the callback
     */
    void registerDirectoryListener(File directory, DirectoryListener directoryListener);
    /**
     * Unregisters interest in receiving notifications when a directory's
     * contents have changed.
     * @param directory the directory
     * @param directoryListener the callback
     */
    void unregisterDirectoryListener(File directory, DirectoryListener directoryListener);
    /**
     * Shuts down the file monitor service.
     */
    void shutdown();
    /**
     * Initializes the file monitor service.
     */
    void initialize();
}
