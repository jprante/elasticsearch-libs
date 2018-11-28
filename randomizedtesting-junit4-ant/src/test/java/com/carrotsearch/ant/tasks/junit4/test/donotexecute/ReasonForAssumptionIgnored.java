package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carrotsearch.ant.tasks.junit4.test.it.TestTextReport;
import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.carrotsearch.randomizedtesting.RandomizedTest;

/**
 * Holder class for tests used in
 * {@link TestTextReport#reasonForSuiteAssumptionIgnored()}.
 */
public class ReasonForAssumptionIgnored {
  public final static String MESSAGE = "FooBar";
  
  @RunWith(RandomizedRunner.class)
  public static class IgnoredClassRR extends IgnoredClass {
  }

  public static class IgnoredClass {
    @BeforeClass
    public static void beforeClass() {
      RandomizedTest.assumeTrue(MESSAGE, false);
    }
    
    @Test
    public void ignored1() {}
    
    @Test
    public void ignored2() {}
  }
}
