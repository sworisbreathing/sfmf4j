/*
 * Copyright 2013 Steven Swor.
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
package sfmf4j.nio2;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Objects;

/**
 *
 * @author Steven Swor
 */
public class ResolvedPathWatchEvent implements WatchEvent<Path> {

    private final Path context;
    private final int count;
    private final Kind<Path> kind;

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResolvedPathWatchEvent other = (ResolvedPathWatchEvent) obj;
        if (!Objects.equals(this.context, other.context)) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        if (!Objects.equals(this.kind, other.kind)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.context);
        hash = 29 * hash + this.count;
        hash = 29 * hash + Objects.hashCode(this.kind);
        return hash;
    }
}
