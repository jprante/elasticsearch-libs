package com.carrotsearch.ant.tasks.junit4.test.it;


import org.junit.Test;

/**
 * Test report-text listener.
 */
public class TestSecuritySandbox extends JUnit4XmlTestBase {
  @Test
  public void gh255() {
    super.executeTarget("gh255");
    assertLogContains("Tests summary: 2 suites, 2 tests, 1 error");
  }
}
