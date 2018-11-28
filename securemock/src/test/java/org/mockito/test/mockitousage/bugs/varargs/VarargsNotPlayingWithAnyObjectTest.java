/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.bugs.varargs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

//see issue 62
public class VarargsNotPlayingWithAnyObjectTest extends TestBase {

    interface VarargMethod {
        Object run(String... args);
    }

    @Mock VarargMethod mock;

    @Test
    public void shouldMatchAnyVararg() {
        mock.run("a", "b");

        verify(mock).run(anyString(), anyString());
        verify(mock).run((String) any(), (String) any());

        verify(mock).run((String[]) any());

        verify(mock, never()).run();
        verify(mock, never()).run(anyString(), eq("f"));
    }

    //we cannot use anyObject() for entire varargs because it makes the verification pick up extra invocations
    //see other tests in this package
    @Test
    public void shouldNotAllowUsingAnyObjectForVarArgs() {
        mock.run("a", "b");

        try {
            verify(mock).run((String[]) any());
            fail();
        } catch (AssertionError e) {}
    }

    @Test
    public void shouldStubUsingAnyVarargs() {
        when(mock.run((String[]) any())).thenReturn("foo");

        assertEquals("foo", mock.run("a", "b"));
    }
}
