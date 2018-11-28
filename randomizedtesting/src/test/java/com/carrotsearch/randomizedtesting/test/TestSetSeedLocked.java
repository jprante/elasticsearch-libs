package com.carrotsearch.randomizedtesting.test;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSetSeedLocked extends RandomizedTest {
  @Test
  public void testMethod() {
    try {
      getRandom().setSeed(0);
      fail();
    } catch (RuntimeException e) {
      // Ok, expected.
    }
  }
}
