package com.gpaglia.bluetooth.lescan.hci;

import java.util.HashMap;
import java.util.Map;

import com.gpaglia.bluetooth.lescan.api.DeviceAddress;
import com.gpaglia.bluetooth.lescan.api.Scanner;
import com.gpaglia.bluetooth.lescan.api.ScannerManager;
import com.gpaglia.bluetooth.lescan.api.ScannerException;

/**
 * ScannerImpl is a singleton that will enforce that a given deviceNo representing a
 * bluetooth device cannot be opened for scanning multiple times
 * 
 */
public class HciScannerManager implements ScannerManager {
    private Map<Integer, Scanner> openDevices = new HashMap<>();

    @Override
    public synchronized Scanner getScanner(final int device) throws ScannerException {
        final int devNo = device < 0
            ? getDefaultDeviceNo()
            : device;

        if (openDevices.containsKey(devNo)) {
            throw new HCIException("Device " + devNo + " is already open");
        }

        final Scanner sh = buildHciScanner(device);
        openDevices.put(devNo, sh);
        return sh;
        
    }

    @Override
    public Scanner getScanner(final DeviceAddress adapterAddress) throws ScannerException {
        final int devNo = getDeviceNoFromAddress(adapterAddress.getAddress());
        if (devNo < 0) {
            throw new HCIException("Error translating device address to device number", -devNo);
        }
        return getScanner(devNo);
    }

    @Override
    public Scanner getScanner(final String deviceName) throws ScannerException {
        final int devNo = getDeviceNoFromName(deviceName);
        if (devNo < 0) {
            throw new HCIException("Error translating device name to device number", -devNo);
        }
        return getScanner(devNo);
    }
    
    private native int getDeviceNoFromAddress(final String addr);

    private native int getDeviceNoFromName(final String name);

    private native int getDefaultDeviceNo();

    private HciScanner buildHciScanner(int device) {
        return new HciScanner(device);
    }
}