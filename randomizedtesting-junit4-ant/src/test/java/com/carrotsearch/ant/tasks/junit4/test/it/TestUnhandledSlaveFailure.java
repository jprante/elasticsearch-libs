package com.carrotsearch.ant.tasks.junit4.test.it;

import java.io.IOException;

import org.junit.Test;

import com.carrotsearch.ant.tasks.junit4.test.donotexecute.FireUnhandledRunnerException;

public class TestUnhandledSlaveFailure extends JUnit4XmlTestBase {
  @Test
  public void checkSlaveMainFailure() throws IOException {
    super.expectBuildExceptionContaining("slavemainfailure", "process threw an exception");
    assertLogContains(FireUnhandledRunnerException.EXCEPTION_MESSAGE);
  }
}
