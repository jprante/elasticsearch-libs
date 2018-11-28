package com.carrotsearch.ant.tasks.junit4.test.donotexecute;

import org.junit.Test;

import com.carrotsearch.randomizedtesting.RandomizedTest;

public class ReasonForIgnoredByDisabledGroup extends RandomizedTest
{
  @Test
  @DisabledGroup("foo bar")
  public void disabledGroup() {}
}
