package com.spotbugs.plugin;

import java.util.ArrayList;
import java.util.List;

class BadCase {
  public static int i = 0;
  public static String s = "";
  public static List l = new ArrayList();
  public static Singleton singleton = new Singleton();
  public static String[] strings = new String[4];
  public static int[] ints = new int[4];

  void method() {
    System.out.println(singleton);
    System.out.println(l);
    System.out.println(strings);
    System.out.println(ints);
    System.out.println(s);
    System.out.println("Hello SpotBugs!");
  }

  static class Singleton {}
}
