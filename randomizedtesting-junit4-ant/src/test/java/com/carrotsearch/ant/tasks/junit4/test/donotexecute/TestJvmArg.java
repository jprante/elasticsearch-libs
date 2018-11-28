package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Assert;
import org.junit.Test;

public class TestJvmArg {
  /**
   * Check system property passing.
   */
  @Test
  public void testJvmArg() {
    Assert.assertEquals("foobar", System.getProperty("test.param"));
  }
}
