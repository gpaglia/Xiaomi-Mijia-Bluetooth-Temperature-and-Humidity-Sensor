package com.gpaglia.bluetooth.exp.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JniExample3 {
  private JniExample3() {
  }

  /**
   * Main method.
   * 
   * @param args The args
   * @throws InterruptedException The exception
   */
  public static void main(String[] args) throws InterruptedException {
    System.loadLibrary("jniexp");
    final JniExample3 example = new JniExample3();

    // example.doIt1();

    example.doIt2();

    System.exit(0);
  }

  private void doIt1() throws InterruptedException {
    int count = 5;
    int tslp = 2;
    int sleep = 3;
    final MyCallback cb = new MyCallback();

    System.out.printf(
        "Starting native thread with count=%d, thread sleep=%d and java sleep=%d %n", 
        count, 
        tslp, 
        sleep
    );

    final ByteBuffer ctx = startThread(count, tslp, 100, cb);

    ctx.order(ByteOrder.nativeOrder());

    final int pt = ctx.getInt(0);

    System.out.printf("In Java: pthread is %x %n", pt);

    Thread.sleep((count - 2) * tslp * 1000);

    final boolean flag = isThreadAlive(ctx);

    System.out.printf("Is thread alive after %d sec? %b %n", (count - 2) * tslp, flag);

    Thread.sleep(3 * tslp * 1000);

    final boolean flag2 = isThreadAlive(ctx);

    System.out.printf("Is thread alive after another %d sec? %b %n", 3 * tslp, flag2);
 
    
  }

  private void doIt2() throws InterruptedException {
    int count = 5;
    int tslp = 2;
    int sleep = 3;
    final MyCallback cb = new MyCallback();

    final ByteBuffer ctx = startThread(count, tslp,  200, cb);

    Thread.sleep(sleep * 1000);

    stopThread(ctx);

    Thread.sleep(100);

    final boolean flag = isThreadAlive(ctx);
    System.out.printf("Is thread alive after stop? %b %n", flag);
    
  }

  private native ByteBuffer startThread(
      final int count, final int slp, final int val, final MyCallback cb
  );

  private native boolean isThreadAlive(final ByteBuffer ctx);

  private native void stopThread(final ByteBuffer ctx);
}

@SuppressWarnings("OneTopLevelClass")
class MyCallback {
  public MyCallback() {}

  public void accept(final int val, final int iter) {
    System.out.printf("MyCallback: val=%d, iter=%d %n", val, iter);
  }
}