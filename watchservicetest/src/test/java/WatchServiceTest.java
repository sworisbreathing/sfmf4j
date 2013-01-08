/*
 *  Copyright 2013 Steven Swor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import java.io.File;
import java.nio.file.*;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Steven Swor
 */
public class WatchServiceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private WatchService watchService = null;
    private WatchKey watchKey = null;
    private Path watchPath = null;

    @Before
    public void setUp() throws Exception {
        watchService = FileSystems.getDefault().newWatchService();
        File folder = tempFolder.getRoot();
        watchPath = folder.toPath().toAbsolutePath();
        watchKey = watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
    }

    @After
    public void tearDown() throws Exception {
        if (watchKey != null) {
            watchKey.cancel();
            watchKey = null;
        }
        if (watchService != null) {
            watchService.close();
            watchService = null;
        }
    }

    @Test
    public void performTest() throws Exception {
        File newFile = tempFolder.newFile();
        assertTrue(newFile.exists());
        Path newFilePath = newFile.toPath().toAbsolutePath();
        System.out.println(newFilePath.toString());
        assertTrue(Files.exists(newFilePath));
        WatchKey key = watchService.take();
        List<WatchEvent<?>> events = key.pollEvents();
        assertEquals(1, events.size());
        WatchEvent event = events.get(0);
        assertEquals(StandardWatchEventKinds.ENTRY_CREATE, event.kind());
        assertEquals(1, event.count());
        Path eventPath = watchPath.resolve((Path) event.context());
        System.out.println(eventPath.toString());
        assertTrue(Files.exists(eventPath));
        assertTrue(Files.isSameFile(newFilePath, eventPath));
    }
}
