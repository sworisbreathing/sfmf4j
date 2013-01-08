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
package sfmf4j.nio2;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Steven Swor
 */
public class SFMF4JWatchListenerTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests {@link SFMF4JWatchListener#onEvent(java.nio.file.WatchEvent)}
     */
    @Test
    public void testOnCreatedEvent() throws Exception {
        FileTrackingListenerImpl listener = new FileTrackingListenerImpl();        
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        File created = tempFolder.newFile();
        Path createdPath = created.toPath();
        WatchEvent<Path> createdEvent = new SimpleWatchEvent<Path>(createdPath, StandardWatchEventKinds.ENTRY_CREATE);
        instance.onEvent(createdEvent);
        assertTrue(listener.getCreatedFiles().contains(created));
    }
    
    /**
     * Tests {@link SFMF4JWatchListener#onEvent(java.nio.file.WatchEvent)}
     */
    @Test
    public void testOnModifiedEvent() throws Exception {
        FileTrackingListenerImpl listener = new FileTrackingListenerImpl();        
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        File file = tempFolder.newFile();
        Path path = file.toPath();
        WatchEvent<Path> event = new SimpleWatchEvent<Path>(path, StandardWatchEventKinds.ENTRY_MODIFY);
        instance.onEvent(event);
        assertTrue(listener.getModifiedFiles().contains(file));
    }
    
    /**
     * Tests {@link SFMF4JWatchListener#onEvent(java.nio.file.WatchEvent)}
     */
    @Test
    public void testOnDeletedEvent() throws Exception {
        FileTrackingListenerImpl listener = new FileTrackingListenerImpl();        
        SFMF4JWatchListener instance = new SFMF4JWatchListener(listener);
        File file = tempFolder.newFile();
        Path path = file.toPath();
        WatchEvent<Path> event = new SimpleWatchEvent<Path>(path, StandardWatchEventKinds.ENTRY_DELETE);
        instance.onEvent(event);
        assertTrue(listener.getDeletedFiles().contains(file));
    }
}
