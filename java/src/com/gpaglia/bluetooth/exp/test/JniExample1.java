package com.gpaglia.bluetooth.exp.test;

@SuppressWarnings("java:S106")
public class JniExample1 {

  private JniExample1() {
  }

  public static void main(String[] args) {
    System.loadLibrary("jniexp");
    final JniExample1 example = new JniExample1();
    final Callback another = new Callback(17);

    final int result = example.callc(another, 234);

    System.out.printf("Result from jni is %d %n", result);

  }

  private native int callc(final Callback ao, final int val);

}

@SuppressWarnings("java:S106")
class Callback {
  private static final String FMT = "This is the callback with local value %d and provided value %d %n";
  private final int aval;

  Callback(final int v) {
    this.aval = v;
  }

  public void callback(final int extv) {
    System.out.printf(FMT, aval, extv);
  }
}
