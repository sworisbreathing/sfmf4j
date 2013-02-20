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

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.WatchService;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link FileSystemFactory}.
 *
 * @author sswor
 */
public class FileSystemFactoryTest {

    /**
     * Tests {@link FileSystemFactory#newWatchService()}.
     *
     * @throws ClosedWatchServiceException if the newly-created service is closed
     * @throws IOException if the test fails
     * @throws InterruptedException hopefully never
     */
    @Test
    public void testNewSingleThreadExecutor() throws ClosedWatchServiceException, IOException, InterruptedException {
        WatchService service = new FileSystemFactory().newWatchService();
        try {
            assertNotNull(service);
            assertNull(service.poll()); // verifies the service is not closed
        } finally {
            if (service != null) {
                try {
                    service.close();
                }catch(IOException ex) {
                    //trap
                }
            }
        }
    }
}