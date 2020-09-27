package com.gpaglia.bluetooth.lescan.hci;

import java.util.Timer;
import java.util.TimerTask;

import com.gpaglia.bluetooth.lescan.api.AbstractScanner;
import com.gpaglia.bluetooth.lescan.api.ScanInfoCallback;
import com.gpaglia.bluetooth.lescan.api.ScannerException;


public final class HciScanner extends AbstractScanner {
  private Thread scanThread = null;

  HciScanner(final int device) {
    super(device);
  }

  private class ScanRunner implements Runnable {

    private final int timeout;
    private final ScanInfoCallback callback;
    private final TimerTask interrupter;
    private final Timer t = new Timer();
    

    private ScanRunner(final int timeout, final ScanInfoCallback cb) {
      this.timeout = timeout;
      this.callback = cb;
      this.interrupter = new TimerTask() {

        @Override
        public void run() {
          if (scanThread != null) {
            scanThread.interrupt();
          }
          t.cancel();
        }
      };
    }

    @Override
    public void run() {
      int fd = -1;
      try {
        fd = openDeviceFromDevNo(getDevice());

        configureInterval(fd, getInterval(), getWindow());
        configureScanType(fd, getScanType().getTypeCode());
        configureDiscoveryType(fd, getDiscoveryType().getTypeCode());
        configureDuplicates(fd, isDuplicatesEnabled() ? 1 : 0);
        configurePublicAddr(fd, isPublicAddressEnabled() ? 1 : 0);

        t.schedule(interrupter, timeout);
        enableScan(fd, callback);

        while (! Thread.interrupted()) {
          // do scan loop
        }
  
      } catch (InterruptedException iex) {
        Thread.currentThread().interrupt();
      } finally {
        disableScan(fd);
        scanFinished();
      }

    }

    // native methods

    private native int openDeviceFromDevNo(final int devNo) throws InterruptedException;

    private native int configureInterval(final int fd, final int interval, final int window) throws InterruptedException;

    private native int configureScanType(final int fd, final int type) throws InterruptedException;

    private native int configureDiscoveryType(final int fd, final int type) throws InterruptedException;

    private native int configureDuplicates(final int fd, final int flag) throws InterruptedException;

    private native int configurePublicAddr(final int fd, final int flag) throws InterruptedException;

    private native int enableScan(final int fd, ScanInfoCallback cb) throws InterruptedException;

    private native int disableScan(final int fd);

    // TODO: whitelists
  }

  @Override
  protected void startScanLocal(int timeout, ScanInfoCallback callback) throws ScannerException {
  
    final ScanRunner runner = new ScanRunner(timeout, callback);
    scanThread = new Thread(runner);

  }

  @Override
  protected void stopScanLocal() throws ScannerException {

    // TODO Auto-generated method stub
  }

  // private methods

  void scanFinished() {

    // do other stuff if necessary

    reportScanCompleted();
  }
  
   
}