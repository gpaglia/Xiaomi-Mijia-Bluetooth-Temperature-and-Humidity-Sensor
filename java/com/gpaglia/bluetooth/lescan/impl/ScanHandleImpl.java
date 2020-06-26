package com.gpaglia.bluetooth.lescan.impl;

import com.gpaglia.bluetooth.lescan.api.DeviceAddress;
import com.gpaglia.bluetooth.lescan.api.HCIException;
import com.gpaglia.bluetooth.lescan.api.ScanHandle;
import com.gpaglia.bluetooth.lescan.api.ScanInfoCallback;
import com.gpaglia.bluetooth.lescan.api.ScanType;
import com.gpaglia.bluetooth.lescan.api.ScannerException;

import java.util.Set;

public class ScanHandleImpl implements ScanHandle {

  private int fd = -1;
  private boolean scanActive = false;

  ScanHandleImpl(final int fd) {
    this.fd = fd;
  }

  @Override
  public synchronized boolean isScanActive() {
    return scanActive;
  }

  @Override
  public synchronized void setWhitelist(Set<DeviceAddress> addresses) throws HCIException {
    // TODO Auto-generated method stub

  }

  @Override
  public synchronized void setWhiteList(DeviceAddress... addreses) throws HCIException {
    // TODO Auto-generated method stub

  }

  @Override
  public synchronized void startScan(
    ScanType scanType, 
    boolean withWhitelist, 
    boolean withDuplicates, 
    boolean withPublicAddress,
    int interval, 
    int window, 
    int timeout, 
    ScanInfoCallback callback
    ) throws HCIException {
      if (scanActive) {
        throw new ScannerException("Scan session already active!!");
      }
    // TODO Auto-generated method stub

  }

  @Override
  public synchronized void stopScan() throws HCIException {
    // TODO Auto-generated method stub

  }
    
}