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

package com.github.sworisbreathing.sfmf4j.jpathwatch;

import java.io.IOException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.WatchService;

/**
 * Factory class for creating a watch service (makes IoC easier).
 * @author Steven Swor
 */
public class FileSystemFactory {

    /**
     * Creates a new WatchService.
     * @return a new WatchService
     * @throws IOException if a new watch service cannot be created
     * @see FileSystems#getDefault()
     * @see name.pachler.nio.file.FileSystem#newWatchService()
     */
    public WatchService newWatchService() throws IOException {
        return FileSystems.getDefault().newWatchService();
    }
}
