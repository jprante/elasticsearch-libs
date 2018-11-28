package com.carrotsearch.ant.tasks.junit4.test.donotexecute.bad;

import org.junit.Test;

public abstract class TestAbstract {
  @Test
  public void shouldBeIgnored() {}
}
