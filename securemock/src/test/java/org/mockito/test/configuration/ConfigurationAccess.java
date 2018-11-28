/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.GlobalConfiguration;

public class ConfigurationAccess {

    public static IMockitoConfiguration getConfig() {
        return new GlobalConfiguration().getIt();
    }
}
