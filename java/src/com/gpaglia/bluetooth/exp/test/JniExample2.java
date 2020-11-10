package com.gpaglia.bluetooth.exp.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JniExample2 {
  
  private JniExample2() {}

  /**
  * Main method.
  * @param args standard args
  */
  public static void main(String[] args) {
    System.loadLibrary("jniexp");
    final JniExample2 example = new JniExample2();
    
    final ByteBuffer bb = example.callWithByteBuffer(100, (short) 200);
    bb.order(ByteOrder.nativeOrder());
    System.out.printf("Result from jni is %d %d %n", bb.getInt(0), bb.getShort(4));

    System.out.print("Buffed content: [");
    for (int i = 0; i < bb.limit(); i++) {
      System.out.printf("%02x ", bb.get(i));
    }
    System.out.print("]\n");
}

  private native ByteBuffer callWithByteBuffer(final int ival, final short shval);

}
