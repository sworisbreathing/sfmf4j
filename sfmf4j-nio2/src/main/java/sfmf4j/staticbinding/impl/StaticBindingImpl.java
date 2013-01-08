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
import sfmf4j.nio2.WatchServiceFileMonitorServiceFactory;

/**
 * 
 * @author Steven Swor
 */
public class StaticBindingImpl {
    
    public static String REQUESTED_API_VERSION = "1.0.99";

    private static volatile StaticBindingImpl SINGLETON = null;
    
    public static StaticBindingImpl getSingleton() {
        if (SINGLETON==null) {
            synchronized(StaticBindingImpl.class) {
                if (SINGLETON == null) {
                    SINGLETON = new StaticBindingImpl();
                }
            }
        }
        return SINGLETON;
    }
    
    private volatile FileMonitorServiceFactory factory = null;
    
    private StaticBindingImpl() {
        
    }
    
    public String getFileMonitorServiceFactoryClassStr() {
        return WatchServiceFileMonitorServiceFactory.class.getName();
    }
    
    public FileMonitorServiceFactory getFileMonitorServiceFactory() {
        if (factory==null) {
            synchronized(this) {
                if (factory == null) {
                    factory = new WatchServiceFileMonitorServiceFactory(null, null);
                }
            }
        }
        return factory;
    }
}
