package com.carrotsearch.randomizedtesting.test;

import static org.junit.Assert.assertEquals;

import com.carrotsearch.randomizedtesting.RandomizedContext;
import com.carrotsearch.randomizedtesting.RandomizedRunner;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carrotsearch.randomizedtesting.annotations.Seed;

/**
 * Seed fixing for static fixtures and/or methods using annotations.
 */
@RunWith(RandomizedRunner.class)
@Seed("deadbeef")
public class TestSeedFixing {
  @BeforeClass
  public static void beforeClass() {
    assertEquals(0xdeadbeefL, RandomizedContext.current().getRandomness().getSeed());
  }

  @Seed("cafebabe")
  @Test
  public void dummy() {
    Assertions
      .assertThat(Long.toHexString(RandomizedContext.current().getRandomness().getSeed()))
      .isEqualTo("cafebabe");
  }
}
