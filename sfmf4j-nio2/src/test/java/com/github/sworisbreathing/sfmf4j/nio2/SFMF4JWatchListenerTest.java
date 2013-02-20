/*
 * Copyright 2013 sswor.
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
package com.github.sworisbreathing.sfmf4j.nio2;

import com.github.sworisbreathing.sfmf4j.api.DirectoryListener;
import com.github.sworisbreathing.sfmf4j.api.DirectoryListenerAdapter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for {@link SFMF4JWatchListener}.
 *
 * @author sswor
 */
public class SFMF4JWatchListenerTest {

    /**
     * Tests {@link SFMF4JWatchListener#SFMF4JWatchListener(DirectoryListener)}
     * when the listener is {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testConstructorWithNull() {
        new SFMF4JWatchListener(null);
    }

    /**
     * Tests {@link SFMF4JWatchListener#onEvent(WatchEvent)} for
     * {@link StandardWatchEventKind#ENTRY_CREATE}.
     */
    @Test
    public void testOnCreateEvent() {
        final List<File> files = new LinkedList<File>();
        final Path path = Paths.get(UUID.randomUUID().toString());
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileCreated(File created) {
                files.add(created);
            }
        };
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        instance.onEvent(new PathWatchEvent(StandardWatchEventKinds.ENTRY_CREATE, path, 1));
        assertEquals(1, files.size());
        assertEquals(path, Paths.get(files.get(0).getPath()));
    }

    /**
     * Tests {@link SFMF4JWatchListener#onEvent(WatchEvent)} for
     * {@link StandardWatchEventKind#ENTRY_MODIFY}.
     */
    @Test
    public void testOnModifyEvent() {
        final List<File> files = new LinkedList<File>();
        final Path path = Paths.get(UUID.randomUUID().toString());
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileChanged(File changed) {
                files.add(changed);
            }
        };
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        instance.onEvent(new PathWatchEvent(StandardWatchEventKinds.ENTRY_MODIFY, path, 1));
        assertEquals(1, files.size());
        assertEquals(path, Paths.get(files.get(0).getPath()));
    }

    /**
     * Tests {@link SFMF4JWatchListener#onEvent(WatchEvent)} for
     * {@link StandardWatchEventKind#ENTRY_DELETE}.
     */
    @Test
    public void testOnDeleteEvent() {
        final List<File> files = new LinkedList<File>();
        final Path path = Paths.get(UUID.randomUUID().toString());
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileDeleted(File deleted) {
                files.add(deleted);
            }
        };
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        instance.onEvent(new PathWatchEvent(StandardWatchEventKinds.ENTRY_DELETE, path, 1));
        assertEquals(1, files.size());
        assertEquals(path, Paths.get(files.get(0).getPath()));
    }

    /**
     * Tests {@link SFMF4JWatchListener#onEvent(WatchEvent)} for other events
     */
    @Test
    public void testOnOtherEvent() {
        final List<File> files = new LinkedList<File>();
        final Path path = Paths.get(UUID.randomUUID().toString());
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileCreated(File created) {
                files.add(created);
            }

            @Override
            public void fileChanged(File created) {
                files.add(created);
            }

            @Override
            public void fileDeleted(File deleted) {
                files.add(deleted);
            }
        };
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        instance.onEvent(new PathWatchEvent(null, path, 1));
        assertTrue(files.isEmpty());
    }

    /**
     * Tests {@link SFMF4JWatchListener#equals(Object)} and
     * {@link SFMF4JWatchListener#hashCode()}.
     */
    @Test
    @SuppressWarnings({"ObjectEqualsNull", "IncompatibleEquals"})
    public void testEqualsAndHashCode() {
        DirectoryListener listener1 = new DirectoryListenerAdapter();
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener1);
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("a string"));

        DirectoryListener listener2 = new DirectoryListenerAdapter();
        SFMF4JWatchListener other = new SFMF4JWatchListener(listener2);
        assertFalse(instance.equals(other));

        SFMF4JWatchListener shouldBeEqual = new SFMF4JWatchListener(listener1);
        assertEquals(instance, shouldBeEqual);
        assertEquals(instance.hashCode(), shouldBeEqual.hashCode());
    }
}