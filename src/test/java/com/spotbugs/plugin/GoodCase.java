package com.spotbugs.plugin;

/** Test case for false positive */
class GoodCase {
  public byte aByte;
  public short aShort;
  public int anInt;
  public long aLong;
  public float aFloat;
  public double aDouble;
  char aChar;
  boolean aBoolean;

  void method() {
    System.err.println("Hello SpotBugs!");
  }
}
