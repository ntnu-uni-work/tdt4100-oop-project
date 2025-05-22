package com.tftrolldownsimulator.models;

import java.util.Random;

public class RandomUtil {
  public static final Random RANDOM = new Random(123);

  public static double nextDouble() {
    return RANDOM.nextDouble();
  }

  public static int nextInt(int cap) {
    return RANDOM.nextInt(cap);
  }
}
