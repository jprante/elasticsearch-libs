/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.mockitousage.configuration;

import org.junit.Test;
import org.mockito.configuration.TestMockitoConfiguration;
import org.mockito.test.mockitousage.IMethods;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;


/**
 * @see TestMockitoConfiguration#getAnnotationEngine() for the custom smartmock injection engine
 */
public class CustomizedAnnotationForSmartMockTest extends TestBase {

    @TestMockitoConfiguration.SmartMock
    IMethods smartMock;

    @Test
    public void shouldUseCustomAnnotation() {
        assertEquals("SmartMock should return empty String by default", "", smartMock.simpleMethod(1));
        verify(smartMock).simpleMethod(1);
    }
}
