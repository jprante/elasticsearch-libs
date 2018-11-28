/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.test.mockitousage.IMethods;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class NPEWhenMockingThrowablesTest extends TestBase {

    @Mock IMethods mock;
    @Mock DummyException mock2;

    class DummyException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    //issue 70
    @Test
    public void shouldNotThrowNPE() {
        when(mock.simpleMethod()).thenThrow(mock2);
        try {
            mock.simpleMethod();
            fail();
        } catch(DummyException e) {}
    }
}
