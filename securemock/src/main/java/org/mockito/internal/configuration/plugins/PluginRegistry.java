/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    /**
     * Returns the implementation of the stack trace cleaner for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider} if no
     * {@link StackTraceCleanerProvider} extension exists or is visible in the current classpath.</p>
     */
    static StackTraceCleanerProvider getStackTraceCleanerProvider() {
        PluginLoader pluginLoader = new PluginLoader();
        StackTraceCleanerProvider stackTraceCleanerProvider = pluginLoader.loadPlugin(StackTraceCleanerProvider.class);
        if (stackTraceCleanerProvider == null) {
            throw new IllegalArgumentException("no stack trace cleaner provider defined");
        }
        return stackTraceCleanerProvider;
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker} if no
     * {@link MockMaker} extension exists or is visible in the current classpath.</p>
     */
    static MockMaker getMockMaker() {
        PluginLoader pluginLoader = new PluginLoader();
        MockMaker mockMaker = pluginLoader.loadPlugin(MockMaker.class);
        if (mockMaker == null) {
            throw new IllegalArgumentException("no mock maker defined");
        }
        return mockMaker;
    }

    /**
     * Returns the instantiator provider available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.instance.DefaultInstantiatorProvider} if no
     * {@link InstantiatorProvider} extension exists or is visible in the current classpath.</p>
     */
    static InstantiatorProvider getInstantiatorProvider() {
        PluginLoader pluginLoader = new PluginLoader();
        InstantiatorProvider instantiatorProvider = pluginLoader.loadPlugin(InstantiatorProvider.class);
        if (instantiatorProvider == null) {
            throw new IllegalArgumentException("no instantiator provider defined");
        }
        return instantiatorProvider;
    }

    /**
     * Returns the annotation engine available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.configuration.InjectingAnnotationEngine} if no
     * {@link AnnotationEngine} extension exists or is visible in the current classpath.</p>
     */
    static AnnotationEngine getAnnotationEngine() {
        PluginLoader pluginLoader = new PluginLoader();
        AnnotationEngine annotationEngine = pluginLoader.loadPlugin(AnnotationEngine.class);
        if (annotationEngine == null) {
            throw new IllegalArgumentException("no annotation engine defined");
        }
        return annotationEngine;
    }
}
