/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.test.mockitoutil.ClassLoaders.excludingClassLoader;

import java.lang.reflect.Method;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.junit.ExceptionFactory;

public class ExceptionFactoryTest {

    private static ClassLoader classLoaderWithoutJUnit = excludingClassLoader()
            .withCodeSourceUrlOf(ExceptionFactory.class)
            .without("org.junit", "junit")
            .build();

    /** loaded by the current current class loader */
    private static Class<?> junitArgumentsAreDifferent;

    /** loaded by the custom classloader classLoaderWithoutJUnit, which excludes junit-classes */
    private static Class<?> nonJunitArgumentsAreDifferent;

    @BeforeClass
    public static void init() throws ClassNotFoundException {
        nonJunitArgumentsAreDifferent = classLoaderWithoutJUnit.loadClass(ArgumentsAreDifferent.class.getName());
        junitArgumentsAreDifferent = ArgumentsAreDifferent.class;
    }

    @Test
    public void createArgumentsAreDifferentException_withoutJUnit() throws Exception {
        Class<?> exceptionFactory = classLoaderWithoutJUnit.loadClass(ExceptionFactory.class.getName());

        Method m = exceptionFactory.getDeclaredMethod("createArgumentsAreDifferentException", String.class, String.class, String.class);
        Object e = m.invoke(null, "message", "wanted", "actual");

        assertThat(e).isExactlyInstanceOf(nonJunitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit() throws Exception {
        AssertionError e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");

        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent);
    }

    @Test
    public void createArgumentsAreDifferentException_withJUnit2x() throws Exception {
        AssertionError e;

        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent);

        e = ExceptionFactory.createArgumentsAreDifferentException("message", "wanted", "actual");
        assertThat(e).isExactlyInstanceOf(junitArgumentsAreDifferent);
    }
}
