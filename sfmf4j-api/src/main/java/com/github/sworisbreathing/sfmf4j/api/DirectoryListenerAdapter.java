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
 * No-op implementation of {@link DirectoryListener}.  Developers are encouraged
 * to extend this class and override its methods when they only need to
 * implement a subset of the callbacks defined by the interface.
 *
 * @author Steven Swor
 */
public class DirectoryListenerAdapter implements DirectoryListener {

    @Override
    public void fileChanged(File changed) {
    }

    @Override
    public void fileCreated(File created) {
    }

    @Override
    public void fileDeleted(File deleted) {
    }


}
