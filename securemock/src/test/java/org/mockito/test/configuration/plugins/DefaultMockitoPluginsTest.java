/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.configuration.plugins;

import org.junit.Test;
import org.mockito.internal.configuration.plugins.DefaultMockitoPlugins;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.plugins.MockMaker;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

public class DefaultMockitoPluginsTest extends TestBase {

    private DefaultMockitoPlugins plugins = new DefaultMockitoPlugins();

    @Test
    public void provides_plugins() throws Exception {
        assertEquals(ByteBuddyMockMaker.class, plugins.getDefaultPlugin(MockMaker.class).getClass());
    }
}
