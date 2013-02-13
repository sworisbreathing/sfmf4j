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

package sfmf4j.commonsio;

import java.io.File;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import sfmf4j.api.DirectoryListener;

/**
 * A Commons-IO {@link FileAlterationListener} that decorates a {@link
 * DirectoryListener}, passing calls to it.
 *
 * @author Steven Swor
 */
public class SFMF4JFileAlterationListener implements FileAlterationListener {
    /**
     * The listener.
     */
    private final DirectoryListener listener;

    /**
     * Creates a new SFMF4JFileAlterationListener.
     * @param listener the listener to wrap
     */
    public SFMF4JFileAlterationListener(DirectoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDirectoryChange(File file) {
        listener.fileChanged(file);
    }

    @Override
    public void onDirectoryCreate(File file) {
        listener.fileCreated(file);
    }

    @Override
    public void onDirectoryDelete(File file) {
        listener.fileDeleted(file);
    }

    @Override
    public void onFileChange(File file) {
        listener.fileChanged(file);
    }

    @Override
    public void onFileCreate(File file) {
        listener.fileCreated(file);
    }

    @Override
    public void onFileDelete(File file) {
        listener.fileDeleted(file);
    }

    /**
     * No-op.
     * @param fao {@inheritDoc}
     */
    @Override
    public void onStart(FileAlterationObserver fao) {
    }

    /**
     * No-op.
     * @param fao {@inheritDoc}
     */
    @Override
    public void onStop(FileAlterationObserver fao) {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SFMF4JFileAlterationListener other = (SFMF4JFileAlterationListener) obj;
        if (this.listener != other.listener && (this.listener == null || !this.listener.equals(other.listener))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.listener != null ? this.listener.hashCode() : 0);
        return hash;
    }
}
