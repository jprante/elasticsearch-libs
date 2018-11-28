package com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub2;

import org.junit.Test;

public class TestHalfSecond {
  @Test
  public void halfSecond() throws Exception {
    Thread.sleep(500);
  }
}
