/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

import java.util.HashMap;
import java.util.Map;

public class DefaultMockitoPlugins implements MockitoPlugins {

    private final static Map<String, String> DEFAULT_PLUGINS = new HashMap<String, String>();

    static {
        //Keep the mapping: plugin interface name -> plugin implementation class name
        DEFAULT_PLUGINS.put(PluginSwitch.class.getName(), DefaultPluginSwitch.class.getName());
        DEFAULT_PLUGINS.put(MockMaker.class.getName(), "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(StackTraceCleanerProvider.class.getName(), "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");
        DEFAULT_PLUGINS.put(InstantiatorProvider.class.getName(), "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
        DEFAULT_PLUGINS.put(AnnotationEngine.class.getName(), "org.mockito.internal.configuration.InjectingAnnotationEngine");
    }

    @Override
    public <T> T getDefaultPlugin(Class<T> pluginType) {
        String className = DEFAULT_PLUGINS.get(pluginType.getName());
        return create(pluginType, className);
    }

    public String getDefaultPluginClass(String classOrAlias) {
        return DEFAULT_PLUGINS.get(classOrAlias);
    }

    /**
     * Creates an instance of given plugin type, using specific implementation class.
     */
    private <T> T create(Class<T> pluginType, String className) {
        if (className == null) {
            throw new IllegalStateException(
                "No default implementation for requested Mockito plugin type: " + pluginType.getName() + "\n"
                    + "Is this a valid Mockito plugin type? If yes, please report this problem to Mockito team.\n"
                    + "Otherwise, please check if you are passing valid plugin type.\n"
                    + "Examples of valid plugin types: MockMaker, StackTraceCleanerProvider.");
        }
        try {
            // Default implementation. Use our own ClassLoader instead of the context
            // ClassLoader, as the default implementation is assumed to be part of
            // Mockito and may not be available via the context ClassLoader.
            return pluginType.cast(Class.forName(className).getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Internal problem occurred, please report it. " +
                "Mockito is unable to load the default implementation of class that is a part of Mockito distribution. " +
                "Failed to load " + pluginType, e);
        }
    }
}
