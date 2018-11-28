/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.junit;

import org.mockito.internal.junit.MismatchReportingTestListener;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.StrictRunner;
import org.mockito.test.util.SimpleMockitoLogger;

import java.lang.reflect.InvocationTargetException;

public class TestableJUnitRunner extends MockitoJUnitRunner {

    private final static ThreadLocal<SimpleMockitoLogger> LOGGER = new ThreadLocal<SimpleMockitoLogger>() {
        protected SimpleMockitoLogger initialValue() {
            return new SimpleMockitoLogger();
        }
    };

    public TestableJUnitRunner(Class<?> klass) throws InvocationTargetException {
        super(new StrictRunner(new RunnerFactory().create(klass, () ->
                new MismatchReportingTestListener(LOGGER.get())), klass));
    }

    public static SimpleMockitoLogger refreshedLogger() {
        return LOGGER.get().clear();
    }
}
