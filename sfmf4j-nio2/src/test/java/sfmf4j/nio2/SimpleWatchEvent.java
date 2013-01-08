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

import java.nio.file.WatchEvent;

/**
 *
 * @author Steven Swor
 */
public class SimpleWatchEvent<T> implements WatchEvent<T> {

    private final T context;
    
    private final Kind<T> kind;
    
    public SimpleWatchEvent(final T context, final Kind<T> kind) {
        this.context = context;
        this.kind = kind;
    }

    @Override
    public T context() {
        return this.context;
    }

    @Override
    public int count() {
        return 1;
    }

    @Override
    public Kind<T> kind() {
        return kind;
    }
}
