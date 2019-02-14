/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.ServiceLoader;

class PluginLoader {

    private final DefaultMockitoPlugins plugins = new DefaultMockitoPlugins();

    /**
     * Scans the classpath for given pluginType. If not found, default class is used.
     */
    @SuppressWarnings("unchecked")
    <T> T loadPlugin(final Class<T> pluginType) {
        try {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(pluginType);
            Optional<T> plugin = serviceLoader.findFirst();
            return plugin.orElseGet(() -> plugins.getDefaultPlugin(pluginType));
        } catch (final Throwable t) {
            return (T) Proxy.newProxyInstance(pluginType.getClassLoader(),
                    new Class<?>[]{pluginType},
                    (proxy, method, args) -> {
                        throw new IllegalStateException("Could not initialize plugin: " + pluginType, t);
                    });
        }
    }
}
