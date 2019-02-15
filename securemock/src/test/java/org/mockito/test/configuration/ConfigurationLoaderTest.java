/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.configuration;

import org.junit.Test;
import org.mockito.test.mockitousage.IMethods;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ConfigurationLoaderTest extends TestBase {

    @Test
    public void shouldReadConfigurationClass() {
        ConfigurationAccess.getConfig().overrideDefaultAnswer(invocation -> "foo");
        IMethods mock = mock(IMethods.class);
        assertEquals("foo", mock.simpleMethod());
    }
}
