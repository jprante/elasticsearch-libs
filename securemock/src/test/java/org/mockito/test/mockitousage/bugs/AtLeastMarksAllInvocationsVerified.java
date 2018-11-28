/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.mockitousage.bugs;

import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.test.mockitoutil.TestBase;

import static org.mockito.Mockito.*;

// see issue 112
public class AtLeastMarksAllInvocationsVerified extends TestBase {

    public static class SomeMethods {
        public void allowedMethod() {
        }
        public void disallowedMethod() {
        }
    }

    @Test(expected = NoInteractionsWanted.class)
    public void shouldFailBecauseDisallowedMethodWasCalled(){
        SomeMethods someMethods = mock(SomeMethods.class);

        someMethods.allowedMethod();
        someMethods.disallowedMethod();

        verify(someMethods, atLeast(1)).allowedMethod();
        verifyNoMoreInteractions(someMethods);
    }
}
