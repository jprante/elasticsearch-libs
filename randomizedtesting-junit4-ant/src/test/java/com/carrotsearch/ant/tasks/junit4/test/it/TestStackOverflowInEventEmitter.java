package com.carrotsearch.ant.tasks.junit4.test.it;


import org.junit.Test;


public class TestStackOverflowInEventEmitter  extends JUnit4XmlTestBase {
  @Test
  public void stackoverflow() {
    super.expectBuildExceptionContaining("stackoverflow", "Quit event not received from the forked process?");
  }
}
