package com.gpaglia.bluetooth.exp.test;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("java:S106")
public class ThreadExample {
    private final int count; // how many loops
    private final int sleep; // in ms
    private Thread myThread;

    public ThreadExample(final int count, final int sleep) {
        this.count = count;
        this.sleep = sleep;
    }

    private class Callback {
        public void reportCallback(final long startts, final int data) {
            System.out.printf("callback: called after %dms data %d ...%n", System.currentTimeMillis() - startts, data);
        }
    }

    private class Runner implements Runnable {

        private final int nloops;
        private final int delay;
        private final Callback callback;
        private final TimerTask interrupter;
        private final Timer t = new Timer();
        private long ts;

        private Runner(final int nloops, final int delay) {
            this.nloops = nloops;
            this.delay = delay;
            this.callback = new Callback();
            this.interrupter = new TimerTask() {
                @Override
                public void run() {
                    System.out.printf("timer, after %dms, myThread not null? %b %n", System.currentTimeMillis() - ts, myThread != null);
                    if (myThread != null) {
                        myThread.interrupt();
                    }
                    t.cancel();
                }
            };
        }

        @Override
        public void run() {
            int iter = 0;
            ts = System.currentTimeMillis();
            System.out.printf("Runner thread starting at ts %d %n", ts);
            try {

                t.schedule(interrupter, nloops * (delay + 100L));

                while (!Thread.interrupted()) {
                    // do loop action
                    doAction(iter++, delay, callback);
                }

            } catch (InterruptedException iex) {
                System.out.printf("Loop interrupted after %dms !!%n", delta());
                Thread.currentThread().interrupt();
            } finally {
                System.out.printf("Loop finally clause: calling finished()  after %dms %n...", delta());
                finished();
            }

        }

        @SuppressWarnings("java:S2142")
        private void doAction(final int cnt, final int delay, final Callback cb) throws InterruptedException {
            System.out.printf("doAction: after %dms cnt %d about to sleep for %d ms ...%n", delta(), cnt, delay);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.out.printf("doAction: after %dms Interrupted!! %n", delta());
                throw ex;
            }
            System.out.printf("doAction: after %dms finished sleeping, calling callback ... %n", delta());

            cb.reportCallback(ts, cnt);

        }

        private void finished() {
            System.out.printf("finished called after %dms ...%n", delta());
        }

        private long delta() {
            return System.currentTimeMillis() - ts;
        }
    }

    public static void main(String[] args) {
        final int nloops = 5;
        final int delay = 1000;
        final ThreadExample example = new ThreadExample(nloops, delay);
        example.startRunner();
    }

    private void startRunner() {
        final Runner runner = new Runner(this.count, this.sleep);
        myThread = new Thread(runner);
        myThread.start();
    }

}
