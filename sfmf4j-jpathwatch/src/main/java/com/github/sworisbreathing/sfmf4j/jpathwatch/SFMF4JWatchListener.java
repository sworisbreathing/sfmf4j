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

import com.github.sworisbreathing.sfmf4j.api.DirectoryListener;
import java.io.File;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;

/**
 * Decorator to forward watch events to SFMF4J listeners.
 *
 * @author Steven Swor
 */
public class SFMF4JWatchListener {
    /**
     * The listener.
     */
    private final DirectoryListener listener;

    /**
     * Creates a new SFMF4JWatchListener.
     * @param listener the listener to decorate
     */
    public SFMF4JWatchListener(DirectoryListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener may not be null");
        }
        this.listener = listener;
    }

    /**
     * Forwards watch events to the listener.
     * @param event the event to forward
     * @see StandardWatchEventKind
     * @see DirectoryListener#fileCreated(File)
     * @see DirectoryListener#fileDeleted(File)
     * @see DirectoryListener#fileChanged(File)
     */
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
        if (this.listener != other.listener && !this.listener.equals(other.listener)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.listener.hashCode();
        return hash;
    }

}
