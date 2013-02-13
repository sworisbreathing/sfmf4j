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
package sfmf4j.jpathwatch;

import java.util.Random;
import java.util.UUID;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.impl.PathWatchEvent;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ResolvedPathWatchEvent}.
 *
 * @author sswor
 */
public class ResolvedPathWatchEventTest {

    private Random random = null;

    @Before
    public void setUp() {
        random = new Random();
    }

    @After
    public void tearDown() {
        random = null;
    }

    protected static Kind randomKind() {
        final String name = UUID.randomUUID().toString();
        return new Kind() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Class type() {
                return getClass();
            }
        };
    }

    /**
     * Tests
     * {@link ResolvedPathWatchEvent#ResolvedPathWatchEvent(WatchEvent, Path)}
     * with a null source.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testConstructorWithNullSource() {
        new ResolvedPathWatchEvent(null, Paths.get(UUID.randomUUID().toString()));
    }

    /**
     * Tests
     * {@link ResolvedPathWatchEvent#ResolvedPathWatchEvent(WatchEvent, Path)}
     * with a null path.
     */
    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testConstructorWithNullPath() {
        new ResolvedPathWatchEvent(new PathWatchEvent(randomKind(), Paths.get(UUID.randomUUID().toString()), 0), null);
    }

    /**
     * Tests {@link ResolvedPathWatchEvent#ResolvedPathWatchEvent(WatchEvent, Path)},
     * {@link ResolvedPathWatchEvent#context()},
     * {@link ResolvedPathWatchEvent#count()}, and
     * {@link ResolvedPathWatchEvent#kind()}.
     */
    @Test
    public void testConstructorAndGetters() {
        final Path child = Paths.get(UUID.randomUUID().toString());
        final Path parent = Paths.get(UUID.randomUUID().toString());
        int expectedCount = random.nextInt();
        final Kind expectedKind = randomKind();
        ResolvedPathWatchEvent instance = new ResolvedPathWatchEvent(new PathWatchEvent(expectedKind, child, expectedCount), parent);
        Path expectedContext = parent.resolve(child);
        assertEquals(expectedContext, instance.context());
        assertEquals(expectedCount, instance.count());
        assertEquals(expectedKind, instance.kind());
    }

    /**
     * Tests {@link ResolvedPathWatchEvent#equals(Object)} and
     * {@link ResolvedPathWatchEvent#hashCode()}.
     */
    @Test
    @SuppressWarnings({"ObjectEqualsNull", "IncompatibleEquals"})
    public void testEqualsAndHashCode() {
        final Path parent = Paths.get(UUID.randomUUID().toString());
        final Path child = Paths.get(UUID.randomUUID().toString());
        int count = random.nextInt();
        final Kind kind = randomKind();
        final WatchEvent<Path> source = new PathWatchEvent(kind, child, count);
        ResolvedPathWatchEvent instance = new ResolvedPathWatchEvent(source, parent);
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("a string"));

        final ResolvedPathWatchEvent nullContextInstance = new ResolvedPathWatchEvent(new PathWatchEvent(kind, null, count), parent);
        assertFalse(instance.equals(nullContextInstance));
        assertFalse(nullContextInstance.equals(instance));

        final ResolvedPathWatchEvent wrongContextInstance = new ResolvedPathWatchEvent(new PathWatchEvent(kind, Paths.get(UUID.randomUUID().toString()), count), parent);
        assertFalse(instance.equals(wrongContextInstance));

        final ResolvedPathWatchEvent wrongCountInstance = new ResolvedPathWatchEvent(new PathWatchEvent(kind, child, 1 + count), parent);
        assertFalse(instance.equals(wrongCountInstance));

        final ResolvedPathWatchEvent nullKindInstance = new ResolvedPathWatchEvent(new PathWatchEvent(null, child, count), parent);
        assertFalse(instance.equals(nullKindInstance));
        assertFalse(nullKindInstance.equals(instance));

        final ResolvedPathWatchEvent wrongKindInstance = new ResolvedPathWatchEvent(new PathWatchEvent(randomKind(), child, count), parent);
        assertFalse(instance.equals(wrongKindInstance));

        final ResolvedPathWatchEvent wrongParentInstance = new ResolvedPathWatchEvent(new PathWatchEvent(kind, child, count), Paths.get(UUID.randomUUID().toString()));
        assertFalse(instance.equals(wrongParentInstance));

        final ResolvedPathWatchEvent shouldBeEqual = new ResolvedPathWatchEvent(new PathWatchEvent(kind, child, count), parent);
        assertEquals(instance, shouldBeEqual);
        assertEquals(instance.hashCode(), shouldBeEqual.hashCode());
    }
}