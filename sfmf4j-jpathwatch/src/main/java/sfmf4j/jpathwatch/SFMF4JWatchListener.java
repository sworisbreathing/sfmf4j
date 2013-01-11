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

package sfmf4j.jpathwatch;

import java.io.File;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import sfmf4j.api.DirectoryListener;

/**
 *
 * @author Steven Swor
 */
public class SFMF4JWatchListener {
    private final DirectoryListener listener;

    public SFMF4JWatchListener(DirectoryListener listener) {
        this.listener = listener;
    }

    public void onEvent(final WatchEvent<Path> event) {
        Kind kind = event.kind();
        if (StandardWatchEventKind.ENTRY_CREATE.equals(kind)) {
            File file = new File(event.context().toString());
            listener.fileCreated(file);
        } else if (StandardWatchEventKind.ENTRY_DELETE.equals(kind)) {
            File file = new File(event.context().toString());
            listener.fileDeleted(file);
        } else if (StandardWatchEventKind.ENTRY_MODIFY.equals(kind)) {
            File file = new File(event.context().toString());
            listener.fileChanged(file);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SFMF4JWatchListener other = (SFMF4JWatchListener) obj;
        if (this.listener != other.listener && (this.listener == null || !this.listener.equals(other.listener))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.listener != null ? this.listener.hashCode() : 0);
        return hash;
    }

}