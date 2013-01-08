/*
 * Copyright 2012 Steven Swor.
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

package sfmf4j.staticbinding.impl;

import sfmf4j.api.FileMonitorServiceFactory;

/**
 * 
 * @author Steven Swor
 */
public class StaticBindingImpl {
    
    public static String REQUESTED_API_VERSION = "1.0.99";

    private static final StaticBindingImpl SINGLETON = new StaticBindingImpl();
    
    public static StaticBindingImpl getSingleton() {
        return SINGLETON;
    }
    
    private StaticBindingImpl() {
        throw new UnsupportedOperationException("This code should never have made it into the jar");
    }
    
    public String getFileMonitorServiceFactoryClassStr() {
        throw new UnsupportedOperationException("This code should never have made it into the jar");
    }
    
    public FileMonitorServiceFactory getFileMonitorServiceFactory() {
        throw new UnsupportedOperationException("This code should never have made it into the jar");
    }
}
