/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.mock.SerializableMode;

import java.util.Collections;
import java.util.Set;

public class MockFeatures<T> {

    final Class<?> callerClass;
    final Class<T> mockedType;
    final Set<Class<?>> interfaces;
    final SerializableMode serializableMode;
    final boolean stripAnnotations;

    private MockFeatures(Class<?> callerClass, Class<T> mockedType, Set<Class<?>> interfaces,
                         SerializableMode serializableMode, boolean stripAnnotations) {
        this.callerClass = callerClass;
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.serializableMode = serializableMode;
        this.stripAnnotations = stripAnnotations;
    }

    public static <T> MockFeatures<T> withMockFeatures(Class<?> callerClass,
                                                       Class<T> mockedType,
                                                       Set<Class<?>> interfaces,
                                                       SerializableMode serializableMode,
                                                       boolean stripAnnotations) {
        return new MockFeatures<T>(callerClass, mockedType, interfaces, serializableMode, stripAnnotations);
    }
}