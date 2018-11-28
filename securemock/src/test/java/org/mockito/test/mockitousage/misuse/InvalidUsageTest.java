/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.mockitousage.misuse;

import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.test.mockitousage.IMethods;
import org.mockito.test.mockitoutil.TestBase;

import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.*;

public class InvalidUsageTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @After
    public void resetState() {
        super.resetState();
    }

    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingNoMoreInteractions() {
        verifyNoMoreInteractions();
    }

    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingZeroInteractions() {
        verifyNoInteractions();
    }

    @Test(expected=MockitoException.class)
    public void shouldNotCreateInOrderObjectWithoutMocks() {
        inOrder();
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowVerifyingInOrderUnfamilarMocks() {
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mockTwo).simpleMethod();
    }

    @Test(expected=MissingMethodInvocationException.class)
    public void shouldReportMissingMethodInvocationWhenStubbing() {
        when(mock.simpleMethod()).thenReturn("this stubbing is required to make sure Stubbable is pulled");
        when("".toString()).thenReturn("x");
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        when(mock.simpleMethod()).thenThrow(new Exception());
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        when(mock.simpleMethod()).thenThrow(new Throwable[] {null});
    }

    @SuppressWarnings("all")
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowableVararg() throws Exception {
        when(mock.simpleMethod()).thenThrow((Throwable) null);
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullConsecutiveThrowable() throws Exception {
        when(mock.simpleMethod()).thenThrow(new RuntimeException(), null);
    }

    final class FinalClass {}

    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingFinalClassesIfDisabled() throws Exception {
        assumeFalse("Inlining mock allows mocking final classes", mock(FinalClass.class).getClass() == FinalClass.class);
    }

    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockingPrimitives() throws Exception {
        mock(Integer.TYPE);
    }

    interface ObjectLikeInterface {
        boolean equals(Object o);
        String toString();
        int hashCode();
    }

    @Test
    public void shouldNotMockObjectMethodsOnInterface() throws Exception {
        ObjectLikeInterface inter = mock(ObjectLikeInterface.class);

        inter.equals(null);
        inter.toString();
        inter.hashCode();

        verifyNoInteractions(inter);
    }

    public void shouldNotMockObjectMethodsOnClass() throws Exception {
        Object clazz = mock(ObjectLikeInterface.class);

        clazz.equals(null);
        clazz.toString();
        clazz.hashCode();

        verifyNoInteractions(clazz);
    }
}
