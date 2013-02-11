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
package sfmf4j.api;

import java.util.concurrent.TimeUnit;

/**
 * Factory interface for creating file monitor services.
 *
 * @author Steven Swor
 */
public interface FileMonitorServiceFactory {

    /**
     * Creates a new file monitor service.
     *
     * @return a new file monitor service
     */
    FileMonitorService createFileMonitorService();
}
