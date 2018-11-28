package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Test;

public class TestSecuritySandbox {
  @Test
  public void accessDenied() {
    System.getProperty("foo");
    System.setProperty("foo", "bar");
  }
}
