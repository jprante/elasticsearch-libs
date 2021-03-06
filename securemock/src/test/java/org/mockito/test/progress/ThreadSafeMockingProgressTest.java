/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.progress;

import org.junit.After;
import org.junit.Test;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.test.verification.DummyVerificationMode;
import org.mockito.test.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class ThreadSafeMockingProgressTest extends TestBase {

    @After
    public void after() {
        this.resetState();
    }

    @Test
    public void shouldShareState() throws Exception {
        //given
        MockingProgress p = mockingProgress();
        p.verificationStarted(new DummyVerificationMode());

        //then
        p = mockingProgress();
        assertNotNull(p.pullVerificationMode());
    }

    @Test
    public void shouldKnowWhenVerificationHasStarted() throws Exception {
        //given
        verify(mock(List.class));
        MockingProgress p = mockingProgress();

        //then
        assertNotNull(p.pullVerificationMode());
    }
}
