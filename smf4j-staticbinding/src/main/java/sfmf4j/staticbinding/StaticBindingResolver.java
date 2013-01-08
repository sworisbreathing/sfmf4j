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

package sfmf4j.staticbinding;

import sfmf4j.staticbinding.impl.StaticBindingImpl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfmf4j.api.FileMonitorServiceFactory;
import sfmf4j.staticbinding.nop.NopFileMonitorServiceFactory;
import sfmf4j.staticbinding.nop.SubstituteFileMonitorServiceFactory;

/**
 *
 * @author Steven Swor
 */
public class StaticBindingResolver {

    static final String STATIC_BINDING_KEY = "sfmf4j.staticbinding.StaticBindingImpl.class";
    
    static enum InitializationState {
      UNINITIALIZED,
      INITIALIZING,
      INITIALIZED,
      ERROR,
      NOP_FALLBACK;
    };
    
    static InitializationState initializationState = InitializationState.UNINITIALIZED;
    static NopFileMonitorServiceFactory noImplFallback = new NopFileMonitorServiceFactory();
    static SubstituteFileMonitorServiceFactory substitute = new SubstituteFileMonitorServiceFactory();
    
    static private final String[] API_COMPATIBILITY_LIST = new String[]{"1.0"};
    
    private StaticBindingResolver() {}
    
    static void reset() {
        initializationState = InitializationState.UNINITIALIZED;
        substitute = new SubstituteFileMonitorServiceFactory();
    }
    
    private static void performInitialization() {
        bind();
        if (initializationState==InitializationState.INITIALIZED) {
            versionSanityCheck();
        }
    }
    
    private static void versionSanityCheck() {
        String requested = StaticBindingImpl.REQUESTED_API_VERSION;
        boolean match = false;
        for (int i = 0;i < API_COMPATIBILITY_LIST.length;i++) {
            if (requested.startsWith(API_COMPATIBILITY_LIST[i])) {
                match = true;
                break;
            }
        }
        if (!match) {
            LoggerFactory.getLogger(StaticBindingResolver.class).warn("The requested version {} is is not compatible with {}", requested, Arrays.asList(API_COMPATIBILITY_LIST));
        }
    }
    
    private static Set<URL> findPossibleStaticBindingPaths() {
        Set<URL> results = new LinkedHashSet<URL>();
        try {
            ClassLoader cl = StaticBindingResolver.class.getClassLoader();
            Enumeration<URL> paths;
            if (cl==null) {
                paths = ClassLoader.getSystemResources(STATIC_BINDING_KEY);
            } else {
                paths = cl.getResources(STATIC_BINDING_KEY);
            }
            while(paths.hasMoreElements()) {
                URL path = paths.nextElement();
                results.add(path);
            }
        }catch(IOException ex) {
            LoggerFactory.getLogger(StaticBindingResolver.class).error("Unable to locate libraries for static binding.", ex);
        }
        return results;
    }
    
    private static boolean stringContainsImplementationClassName(final String msg) {
        if (msg==null) {
            return false;
        } else if (msg.indexOf("sfmf4j/staticbinding/impl/StaticBindingImpl") != -1) {
            return true;
        } else if (msg.indexOf("sfmf4j.staticbinding.impl.StaticBindingImpl") != -1) {
            return true;
        } else {
            return false;
        }
    }
    
    private static void bind() {
        try {
            Set<URL> paths = findPossibleStaticBindingPaths();
            reportMultipleBindingAmbiguity(paths);
            StaticBindingImpl.getSingleton();
            initializationState = InitializationState.INITIALIZED;
            reportActualBinding(paths);
            emitSubstituteWarning();
        }catch(NoClassDefFoundError ex) {
            String msg = ex.getMessage();
            if (stringContainsImplementationClassName(msg)) {
                initializationState = InitializationState.NOP_FALLBACK;
                Logger logger = LoggerFactory.getLogger(StaticBindingResolver.class);
                logger.warn("Failed to load class \"sfmf4j.staticbinding.impl.StaticBindingImpl\".");
                logger.warn("Defaulting to no-operation (NOP) file monitor service implementation.");
            }else{
                failedBinding(ex);
                throw ex;
            }
        }catch(Exception ex) {
            failedBinding(ex);
            throw new IllegalStateException("Unexpected initialization failure", ex);
        }
    }
    
    private static void failedBinding(final Throwable ex) {
        initializationState = InitializationState.ERROR;
        LoggerFactory.getLogger(StaticBindingResolver.class).error("Static binding failed.", ex);
    }
    
    private static void reportMultipleBindingAmbiguity(final Set<URL> paths) {
        if (paths.size()>1) {
            Logger logger = LoggerFactory.getLogger(StaticBindingResolver.class);
            logger.warn("Class path contains multiple sfmf4j bindings.");
            for (URL url : paths) {
                logger.warn("Found sfmf4j binding in {}", url);
            }
        }
    }
    
    private static void reportActualBinding(final Set<URL> paths) {
        if (paths.size() > 1) {
            LoggerFactory.getLogger(StaticBindingResolver.class).warn("Actual sfmf4j binding is of type [{}]", StaticBindingImpl.getSingleton().getFileMonitorServiceFactoryClassStr());
        }
    }
    
    private static void emitSubstituteWarning() {
        Set<File> folders = substitute.getFolders();
        if (folders.size()>0) {
            Logger logger = LoggerFactory.getLogger(StaticBindingResolver.class);
            logger.warn("The following folders will not be monitored because they were registered during initialization:");
            for (File folder : folders) {
                logger.warn("{}", folder.getAbsolutePath());
            }
        }
    }
    
    public static FileMonitorServiceFactory resolveFileMonitorServiceFactory() {
        if (initializationState==InitializationState.UNINITIALIZED) {
            initializationState = InitializationState.INITIALIZING;
            performInitialization();
        }
        switch(initializationState) {
            case INITIALIZED:
                return StaticBindingImpl.getSingleton().getFileMonitorServiceFactory();
            case NOP_FALLBACK:
                return noImplFallback;
            case ERROR:
                throw new IllegalStateException("Failed initialization");
            case INITIALIZING:
                return substitute;
        }
        throw new IllegalStateException("Unreachable code");
    }
}
