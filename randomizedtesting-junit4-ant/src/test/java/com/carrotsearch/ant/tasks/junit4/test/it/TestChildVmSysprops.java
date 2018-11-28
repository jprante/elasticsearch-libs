package com.carrotsearch.ant.tasks.junit4.test.it;


import org.junit.Test;


public class TestChildVmSysprops extends JUnit4XmlTestBase {
  @Test
  public void sysprops() {
    executeTarget("childvm_sysprops");
  }
}
