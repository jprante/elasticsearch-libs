package com.carrotsearch.randomizedtesting.timeouts;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.Utils;
import com.carrotsearch.randomizedtesting.WithNestedTestClass;
import com.carrotsearch.randomizedtesting.annotations.TimeoutSuite;

public class Test006TimeoutAndThreadLeak extends WithNestedTestClass {
  /**
   * Nested test suite class with {@link TimeoutSuite}.
   */
  @TimeoutSuite(millis = 500)
  public static class Nested extends ApplyAtPlace {}

  @Test public void testClassRule() { check(Place.CLASS_RULE); }
  @Test public void testBeforeClass() { check(Place.BEFORE_CLASS); }
  @Test public void testConstructor() { check(Place.CONSTRUCTOR); }
  @Test public void testTestRule() { check(Place.TEST_RULE); }
  @Test public void testBefore() { check(Place.BEFORE); }
  @Test public void testTest() { check(Place.TEST); }
  @Test public void testAfter() { check(Place.AFTER); }
  @Test public void testAfterClass() { check(Place.AFTER_CLASS); }

  /**
   * Check a given timeout place. 
   */
  private void check(Place p) {
    ApplyAtPlace.place = p;
    ApplyAtPlace.runnable = new Runnable() {
      @Override
      public void run() {
        startThread("foobar");
        while (true) RandomizedTest.sleep(1000);
      }
    };

    FullResult r = runTests(Nested.class);
    Utils.assertFailureWithMessage(r, "Suite timeout exceeded");
    Utils.assertFailuresContainSeeds(r);
    Utils.assertNoLiveThreadsContaining("foobar");

    Assertions.assertThat(getLoggingMessages())
      .doesNotContain("Uncaught exception");
  }
}

