package com.gpaglia.bluetooth.lescan.impl;

import com.gpaglia.bluetooth.lescan.api.DeviceAddress;
import com.gpaglia.bluetooth.lescan.api.HCIException;
import com.gpaglia.bluetooth.lescan.api.ScanHandle;
import com.gpaglia.bluetooth.lescan.api.Scanner;

public class ScannerImpl implements Scanner {

    @Override
    public ScanHandle openHciDevice(Integer hciDeviceNo) throws HCIException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanHandle openDevice(DeviceAddress adapterAddress) throws HCIException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScanHandle openDevice(String deviceName) throws HCIException {
        // TODO Auto-generated method stub
        return null;
    }
    
    private native int openDeviceNoFromAddress(String addr);

    private native int openDeviceNoFromName(String name);

    private native int openDeviceFromDevNo(int devNo);

    private ScanHandleImpl buildScanHandle(int fd) {
        final ScanHandleImpl sh = new ScanHandleImpl(fd);

        return sh;
    }
}