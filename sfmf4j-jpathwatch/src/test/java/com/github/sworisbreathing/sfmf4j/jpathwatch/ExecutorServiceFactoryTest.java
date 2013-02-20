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
package com.github.sworisbreathing.sfmf4j.jpathwatch;

import java.util.concurrent.ExecutorService;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Unit tests for {@link ExecutorServiceFactory}.
 *
 * @author sswor
 */
public class ExecutorServiceFactoryTest {

    /**
     * Tests {@link ExecutorServiceFactory#newSingleThreadExecutor()}.
     */
    @Test
    public void testNewSingleThreadExecutor() {
        ExecutorService service = new ExecutorServiceFactory().newSingleThreadExecutor();
        try {
            assertNotNull(service);
            assertFalse(service.isShutdown());
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }
}