package com.gpaglia.bluetooth.exp.test;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("java:S106")
public class ThreadExample {
  private final int count; // how many loops
  private final int sleep; // in secs
  private Thread myThread;

  public ThreadExample(final int count, final int sleep) {
    this.count = count;
    this.sleep = sleep;
  }

  private class Runner implements Runnable {

    private final int nloops;
    private final int delay; // in sec
    private final TimerTask interrupter;
    private final Timer t = new Timer();
    private long ts;
    private final CallbackEx cb;

    private Runner(final int nloops, final int delay) {
      this.nloops = nloops;
      this.delay = delay;
      this.cb = new CallbackEx(System.currentTimeMillis(), 23);
      this.interrupter = new TimerTask() {
        @Override
        public void run() {
          System.out.printf("timer, after %dms, myThread not null? %b %n", System.currentTimeMillis() - ts,
              myThread != null);
          if (myThread != null) {
            myThread.interrupt();
          }
          t.cancel();
        }
      };
    }

    @Override
    public void run() {
      ts = System.currentTimeMillis();
      System.out.printf("Runner thread starting at ts %d %n", ts);
      try {

        t.schedule(interrupter, (nloops - 2) * (delay * 1000) + 100L);

        doAction(nloops, delay, cb);

      } catch (InterruptedException iex) {
        System.out.printf("Loop interrupted after %dms !!%n", delta());
        Thread.currentThread().interrupt();
      } finally {
        System.out.printf("Loop finally clause: calling finished()  after %dms %n...", delta());
        finished();
      }

    }

    @SuppressWarnings("java:S2142")
    private void doAction(final int cnt, final int st, final CallbackEx cb) throws InterruptedException {
      System.out.printf("doAction: cnt=%d st=%d ...%n", cnt, st);
      try {

        final int result = loop(cb, cnt, st);
        System.out.printf("Loop finished rerurning %d %n", result);
      } catch (InterruptedException ex) {
        System.out.printf("doAction: after %dms Interrupted!! %n", delta());
        throw ex;
      }
      System.out.printf("doAction: after %dms finished loop ... %n", delta());

    }

    private void finished() {
      System.out.printf("finished called after %dms ...%n", delta());
    }

    private long delta() {
      return System.currentTimeMillis() - ts;
    }

    private native int loop(
        final CallbackEx cb, final int cnt, final int st
    ) throws InterruptedException;
  }

  /**
   * Main method.
   * @param args Standard args.
   */
  public static void main(String[] args) {
    System.loadLibrary("jniexp");
    final int nloops = 5;
    final int delay = 1;
    final ThreadExample example = new ThreadExample(nloops, delay);
    example.startRunner();
  }

  private void startRunner() {
    final Runner runner = new Runner(this.count, this.sleep);
    myThread = new Thread(runner);
    myThread.start();
  }

}

@SuppressWarnings("java:S106")
class CallbackEx {
  private final long startts;
  private final int vv;

  CallbackEx(final long startts, final int vv) {
    this.startts = startts;
    this.vv = vv;
  }

  public void callback(final int extv) {
    System.out.printf(
        "callback: called after %dms cbv %d localv %d...%n", 
        System.currentTimeMillis() - startts, 
        extv,
        vv
    );
  }
}