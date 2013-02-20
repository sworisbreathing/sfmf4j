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
package com.github.sworisbreathing.sfmf4j.commonsio;

import com.github.sworisbreathing.sfmf4j.api.DirectoryListener;
import com.github.sworisbreathing.sfmf4j.api.DirectoryListenerAdapter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for {@link SFMF4JFileAlterationListener}.
 *
 * @author sswor
 */
public class SFMF4JFileAlterationListenerTest {

    /**
     * Tests
     * {@link SFMF4JFileAlterationListener#SFMF4JFileAlterationListener(DirectoryListener)}
     * with a {@code null} argument.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testConstructorWithNull() {
        new SFMF4JFileAlterationListener(null);
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onDirectoryChange(File)}.
     */
    @Test
    public void testOnDirectoryChange() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileChanged(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onDirectoryChange(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onDirectoryCreate(File)}.
     */
    @Test
    public void testOnDirectoryCreate() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileCreated(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onDirectoryCreate(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onDirectoryDelete(File)}.
     */
    @Test
    public void testOnDirectoryDelete() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileDeleted(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onDirectoryDelete(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onFileChange(File)}.
     */
    @Test
    public void testOnFileChange() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileChanged(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onFileChange(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onFileCreate(File)}.
     */
    @Test
    public void testOnFileCreate() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileCreated(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onFileCreate(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#onFileDelete(File)}.
     */
    @Test
    public void testOnFileDelete() {
        final List<File> files = new LinkedList<File>();
        DirectoryListener listener = new DirectoryListenerAdapter() {
            @Override
            public void fileDeleted(final File file) {
                files.add(file);
            }
        };
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener);
        File f = new File(UUID.randomUUID().toString());
        instance.onFileDelete(f);
        assertEquals(1, files.size());
        assertTrue(files.contains(f));
    }

    /**
     * Tests {@link SFMF4JFileAlterationListener#equals(Object)} and
     * {@link SFMF4JFileAlterationListener#hashCode()}.
     */
    @Test
    @SuppressWarnings({"ObjectEqualsNull", "IncompatibleEquals"})
    public void testEqualsAndHashCode() {
        DirectoryListener listener1 = new DirectoryListenerAdapter();
        SFMF4JFileAlterationListener instance = new SFMF4JFileAlterationListener(listener1);
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("a string"));

        DirectoryListener listener2 = new DirectoryListenerAdapter();
        SFMF4JFileAlterationListener other = new SFMF4JFileAlterationListener(listener2);
        assertFalse(instance.equals(other));

        SFMF4JFileAlterationListener shouldBeEqual = new SFMF4JFileAlterationListener(listener1);
        assertEquals(instance, shouldBeEqual);
        assertEquals(instance.hashCode(), shouldBeEqual.hashCode());
    }
}