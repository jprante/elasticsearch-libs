package com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub1;

import org.junit.Test;

public class TestAssertions {
  @Test
  public void failOnAssertion() {
    assert false : "foobar";
  }
}
