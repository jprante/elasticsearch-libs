package com.carrotsearch.ant.tasks.junit4.test.it;


import org.junit.Test;


public class TestDuplicateResourceCollections extends JUnit4XmlTestBase {
  @Test 
  public void duplicateResourceCollectionEntries() {
    super.executeTarget("duplicateresources");

    assertLogContains("10 suites, 10 tests");
    assertLogContains("JVM J0");
    assertLogContains("JVM J1");
  }
}
