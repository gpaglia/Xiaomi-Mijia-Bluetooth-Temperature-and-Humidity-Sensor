package com.gpaglia.bluetooth.lescan.impl;

import com.gpaglia.bluetooth.lescan.api.DeviceAddress;
import com.gpaglia.bluetooth.lescan.api.HCIException;
import com.gpaglia.bluetooth.lescan.api.ScanHandle;
import com.gpaglia.bluetooth.lescan.api.Scanner;

public class ScannerImpl implements Scanner {

    @Override
    public ScanHandle openHciDevice(Integer hciDeviceNo) throws HCIException {
        final int fd = openDeviceFromDevNo(hciDeviceNo.intValue());

        return buildScanHandle(fd);
    }

    @Override
    public ScanHandle openDevice(DeviceAddress adapterAddress) throws HCIException {
        final int fd = openDeviceFromAddress(adapterAddress.getAddress());
        return buildScanHandle(fd);
    }

    @Override
    public ScanHandle openDevice(String deviceName) throws HCIException {
        final int fd = openDeviceFromName(deviceName);
        return buildScanHandle(fd);
    }
    
    private native int openDeviceFromAddress(String addr);

    private native int openDeviceFromName(String name);

    private native int openDeviceFromDevNo(int devNo);

    private ScanHandleImpl buildScanHandle(int fd) {
        final ScanHandleImpl sh = new ScanHandleImpl(fd);

        return sh;
    }
}