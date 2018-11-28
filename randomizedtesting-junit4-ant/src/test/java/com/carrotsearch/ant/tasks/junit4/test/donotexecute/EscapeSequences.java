package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Test;

public class EscapeSequences {
  @Test
  public void emitEscape() {
    System.out.print("stdout: <b>foo</b> &amp; bar");
  }
}
