/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.test.concurrentmockito;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.test.MockitoTest;
import org.mockito.test.exceptions.base.MockitoAssertionErrorTest;
import org.mockito.test.exceptions.base.MockitoExceptionTest;
import org.mockito.test.internal.AllInvocationsFinderTest;
import org.mockito.test.internal.InvalidStateDetectionTest;
import org.mockito.test.creation.bytebuddy.TypeCachingMockBytecodeGeneratorTest;
import org.mockito.test.internal.exceptions.ReporterTest;
import org.mockito.test.handler.MockHandlerImplTest;
import org.mockito.test.invocation.InvocationMatcherTest;
import org.mockito.test.invocation.InvocationsFinderTest;
import org.mockito.test.matchers.ComparableMatchersTest;
import org.mockito.test.matchers.EqualsTest;
import org.mockito.test.matchers.MatchersToStringTest;
import org.mockito.test.progress.MockingProgressImplTest;
import org.mockito.test.progress.TimesTest;
import org.mockito.test.stubbing.defaultanswers.ReturnsEmptyValuesTest;
import org.mockito.test.stubbing.defaultanswers.ReturnsGenericDeepStubsTest;
import org.mockito.test.util.MockUtilTest;
import org.mockito.test.util.collections.ListUtilTest;
import org.mockito.test.verification.DefaultRegisteredInvocationsTest;
import org.mockito.test.verification.checkers.MissingInvocationCheckerTest;
import org.mockito.test.verification.checkers.MissingInvocationInOrderCheckerTest;
import org.mockito.test.verification.checkers.NumberOfInvocationsCheckerTest;
import org.mockito.test.verification.checkers.NumberOfInvocationsInOrderCheckerTest;
import org.mockito.test.mockitousage.basicapi.ReplacingObjectMethodsTest;
import org.mockito.test.mockitousage.basicapi.ResetTest;
import org.mockito.test.mockitousage.basicapi.UsingVarargsTest;
import org.mockito.test.mockitousage.examples.use.ExampleTest;
import org.mockito.test.mockitousage.matchers.CustomMatchersTest;
import org.mockito.test.mockitousage.matchers.InvalidUseOfMatchersTest;
import org.mockito.test.mockitousage.matchers.MatchersTest;
import org.mockito.test.mockitousage.matchers.VerificationAndStubbingUsingMatchersTest;
import org.mockito.test.mockitousage.misuse.InvalidUsageTest;
import org.mockito.test.mockitousage.puzzlers.BridgeMethodPuzzleTest;
import org.mockito.test.mockitousage.puzzlers.OverloadingPuzzleTest;
import org.mockito.test.mockitousage.stacktrace.ClickableStackTracesTest;
import org.mockito.test.mockitousage.stacktrace.PointingStackTraceToActualInvocationTest;
import org.mockito.test.mockitousage.stacktrace.StackTraceFilteringTest;
import org.mockito.test.mockitousage.stubbing.BasicStubbingTest;
import org.mockito.test.mockitousage.stubbing.ReturningDefaultValuesTest;
import org.mockito.test.mockitousage.stubbing.StubbingWithThrowablesTest;
import org.mockito.test.mockitousage.verification.*;
import org.mockito.test.mockitoutil.TestBase;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ThreadsRunAllTestsHalfManualTest extends TestBase {

    private static class AllTestsRunner extends Thread {

        private Set<Class<?>> failed = new HashSet<Class<?>>();

        public void run() {
            Result result = JUnitCore.runClasses(
                    EqualsTest.class,
                    ListUtilTest.class,
                    MockingProgressImplTest.class,
                    TimesTest.class,
                    MockHandlerImplTest.class,
                    AllInvocationsFinderTest.class,
                    ReturnsEmptyValuesTest.class,
                    NumberOfInvocationsCheckerTest.class,
                    DefaultRegisteredInvocationsTest.class,
                    MissingInvocationCheckerTest.class,
                    NumberOfInvocationsInOrderCheckerTest.class,
                    MissingInvocationInOrderCheckerTest.class,
                    TypeCachingMockBytecodeGeneratorTest.class,
                    InvocationMatcherTest.class,
                    InvocationsFinderTest.class,
                    MockitoTest.class,
                    MockUtilTest.class,
                    ReporterTest.class,
                    MockitoAssertionErrorTest.class,
                    MockitoExceptionTest.class,
                    StackTraceFilteringTest.class,
                    BridgeMethodPuzzleTest.class,
                    OverloadingPuzzleTest.class,
                    InvalidUsageTest.class,
                    UsingVarargsTest.class,
                    CustomMatchersTest.class,
                    ComparableMatchersTest.class,
                    InvalidUseOfMatchersTest.class,
                    MatchersTest.class,
                    MatchersToStringTest.class,
                    VerificationAndStubbingUsingMatchersTest.class,
                    BasicStubbingTest.class,
                    ReturningDefaultValuesTest.class,
                    StubbingWithThrowablesTest.class,
                    AtMostXVerificationTest.class,
                    BasicVerificationTest.class,
                    ExactNumberOfTimesVerificationTest.class,
                    VerificationInOrderTest.class,
                    NoMoreInteractionsVerificationTest.class,
                    SelectedMocksInOrderVerificationTest.class,
                    VerificationOnMultipleMocksUsingMatchersTest.class,
                    VerificationUsingMatchersTest.class,
                    RelaxedVerificationInOrderTest.class,
                    DescriptiveMessagesWhenVerificationFailsTest.class,
                    DescriptiveMessagesWhenTimesXVerificationFailsTest.class,
                    BasicVerificationInOrderTest.class,
                    VerificationInOrderMixedWithOrdiraryVerificationTest.class,
                    DescriptiveMessagesOnVerificationInOrderErrorsTest.class,
                    InvalidStateDetectionTest.class,
                    ReplacingObjectMethodsTest.class,
                    ClickableStackTracesTest.class,
                    ExampleTest.class,
                    PointingStackTraceToActualInvocationTest.class,
                    VerificationInOrderFromMultipleThreadsTest.class,
                    ResetTest.class,
                    ReturnsGenericDeepStubsTest.class
                );

                if (!result.wasSuccessful()) {
                    System.err.println("Thread[" + Thread.currentThread().getId() + "]: error!");
                    List<Failure> failures = result.getFailures();
                    System.err.println(failures.size());
                    for (Failure failure : failures) {
                        System.err.println(failure.getTrace());
                        failed.add(failure.getDescription().getTestClass());
                    }
                }
        }

        public Set<Class<?>> getFailed() {
            return failed;
        }
    }

    @Test
    public void shouldRunInMultipleThreads() throws Exception {
        //this test ALWAYS fails if there is a single failing unit
        assertEquals("Run in multiple thread failed for tests", Collections.emptySet(), runInMultipleThreads(3));
    }

    public static Set<Class<?>> runInMultipleThreads(int numberOfThreads) throws Exception {
        List<AllTestsRunner> threads = new LinkedList<AllTestsRunner>();
        for (int i = 1; i <= numberOfThreads; i++) {
            threads.add(new AllTestsRunner());
        }

        for (Thread t : threads) {
            t.start();
        }

        Set<Class<?>> failed = new HashSet<Class<?>>();
        for (AllTestsRunner t : threads) {
            t.join();
            failed.addAll(t.getFailed());
        }

        return failed;
    }

    public static void main(String[] args) throws Exception {
        int numberOfThreads = 20;
        long before = System.currentTimeMillis();
        Set<Class<?>> failed = runInMultipleThreads(numberOfThreads);
        long after = System.currentTimeMillis();
        long executionTime = (after-before)/1000;
        System.out.println("Finished tests in " + numberOfThreads + " threads in " + executionTime + " seconds. (" + failed.size() + " tests failed)");
    }
}
