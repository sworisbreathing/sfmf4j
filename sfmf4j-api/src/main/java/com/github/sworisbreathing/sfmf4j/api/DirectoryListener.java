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

package com.github.sworisbreathing.sfmf4j.api;

import java.io.File;

/**
 * Interface for callbacks invoked when a directory's contents have changed.
 * Developers are encouraged to extend {@link DirectoryListenerAdapter} if they
 * only need to implement a subset of this interface's callbacks.
 * 
 * @author Steven Swor
 */
public interface DirectoryListener {
    
    /**
     * Callback invoked when a new file is detected.
     * @param created the new file
     */
    void fileCreated(File created);
    
    /**
     * Callback invoked when a change to a file is detected.
     * @param changed the file which was changed
     */
    void fileChanged(File changed);
    
    /**
     * Callback invoked when a file in a directory is no longer detected.
     * @param deleted the file which was deleted
     */
    void fileDeleted(File deleted);
}
