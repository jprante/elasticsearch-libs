package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestEnvVar {
  /**
   * Check environment property passing.
   */
  @Test
  public void testEnvVar() throws IOException {
    String value = System.getenv("env.variable");
    Assert.assertEquals("foobar", value);
  }
}
