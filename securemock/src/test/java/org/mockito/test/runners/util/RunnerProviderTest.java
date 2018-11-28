/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.runners.util;

import org.junit.Test;
import org.mockito.internal.runners.DefaultInternalRunner;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertNotNull;

public class RunnerProviderTest extends TestBase {

    @Test
    public void shouldCreateRunnerInstance() throws Throwable {
        //given
        RunnerProvider provider = new RunnerProvider();
        //when
        InternalRunner runner = provider.newInstance(DefaultInternalRunner.class.getName(), this.getClass(), null);
        //then
        assertNotNull(runner);
    }
}