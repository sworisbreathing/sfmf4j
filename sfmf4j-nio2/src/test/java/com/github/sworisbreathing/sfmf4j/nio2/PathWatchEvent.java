/*
 *Copyright 2013 Steven Swor.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.github.sworisbreathing.sfmf4j.nio2;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Mock implementation of a path watch event.
 *
 * @author Steven Swor
 */
class PathWatchEvent implements WatchEvent<Path> {

    /**
     * The kind.
     */
    private final Kind<Path> kind;
    
    /**
     * The count.
     */
    private final int count;
    
    /**
     * The context.
     */
    private final Path context;

    /**
     * Creates a new PathWatchEvent.
     *
     * @param kind the kind
     * @param context the context
     * @param count the count
     */
    public PathWatchEvent(final Kind<Path> kind, final Path context, final int count) {
        this.kind = kind;
        this.count = count;
        this.context = context;
    }

    @Override
    public Path context() {
        return context;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public Kind<Path> kind() {
        return kind;
    }
}
