package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Test;

public class SysPropertySets {
  @Test
  public void test1() {
    System.out.println(System.getProperty("prefix.dummy1"));
    System.out.println(System.getProperty("prefix.dummy2"));
  }
}
