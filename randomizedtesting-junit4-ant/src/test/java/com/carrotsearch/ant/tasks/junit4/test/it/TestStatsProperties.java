package com.carrotsearch.ant.tasks.junit4.test.it;


import org.junit.Test;


public class TestStatsProperties extends JUnit4XmlTestBase {
  @Test
  public void allFilteredOut() {
    super.executeTarget("statsProperties");
  }
}
