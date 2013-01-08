/*
 * Copyright 2013 Steven Swor.
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
package sfmf4j.nio2;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import sfmf4j.api.DirectoryListener;

/**
 *
 * @author Steven Swor
 */
public class FileTrackingListenerImpl implements DirectoryListener {

    private final List<File> modifiedFiles;
    private final List<File> createdFiles;
    private final List<File> deletedFiles;

    public FileTrackingListenerImpl() {
        modifiedFiles = new LinkedList<File>();
        createdFiles = new LinkedList<File>();
        deletedFiles = new LinkedList<File>();
    }

    @Override
    public void fileChanged(File changed) {
        modifiedFiles.add(changed);
    }

    @Override
    public void fileCreated(File created) {
        createdFiles.add(created);
    }

    @Override
    public void fileDeleted(File deleted) {
        deletedFiles.add(deleted);
    }

    public List<File> getCreatedFiles() {
        return createdFiles;
    }

    public List<File> getDeletedFiles() {
        return deletedFiles;
    }

    public List<File> getModifiedFiles() {
        return modifiedFiles;
    }
    
    
}
