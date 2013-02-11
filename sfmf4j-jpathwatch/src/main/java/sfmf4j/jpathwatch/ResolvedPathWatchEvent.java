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
package sfmf4j.jpathwatch;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchEvent.Kind;

/**
 * A watch event with a resolved path.
 * @author Steven Swor
 */
public class ResolvedPathWatchEvent extends WatchEvent<Path> {

    /**
     * The path.
     */
    private final Path context;

    /**
     * The event count.
     */
    private final int count;

    /**
     * The kind.
     */
    private final Kind<Path> kind;

    /**
     * Copy constructor for a watch event, which ensures the path is fully
     * resolved.
     * @param source the event to copy
     * @param parentPath the parent path
     * @see Path#resolve(Path)
     */
    public ResolvedPathWatchEvent(final WatchEvent<Path> source, final Path parentPath) {
        this.context = parentPath.resolve(source.context());
        this.count = source.count();
        this.kind = source.kind();
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.context != null ? this.context.hashCode() : 0);
        hash = 53 * hash + this.count;
        hash = 53 * hash + (this.kind != null ? this.kind.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResolvedPathWatchEvent other = (ResolvedPathWatchEvent) obj;
        if (this.context != other.context && (this.context == null || !this.context.equals(other.context))) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        if (this.kind != other.kind && (this.kind == null || !this.kind.equals(other.kind))) {
            return false;
        }
        return true;
    }
}
