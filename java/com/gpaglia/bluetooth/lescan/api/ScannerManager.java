package com.gpaglia.bluetooth.lescan.api;


/**
 * Scanner is a singleton that will enforce that a given device representing a
 * bluetooth device cannot be opened for scanning multiple times
 * 
 */
public interface ScannerManager {


    /**
     * Opens a ble device on the local amchine returning the scanner to be used for scanning oeprations.
     * 
     * @param device The device number of the local device (i.e. adapter). If negative, the first device found will be used
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    Scanner getScanner(final int device) throws ScannerException;

    /**
     * Opens a ble device on the local machine returning the scanner to be used for scanning oeprations.
     * 
     * @param adapterAddress The MAC address of the local device (i.e. adapter) to open. If null, the first available device is opeend.
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    Scanner getScanner(final DeviceAddress adapterAddress) throws ScannerException;

    /**
     * Opens a ble device on the local machine returning the scanner to be used for scanning oeprations.
     * @param deviceName the name of the device (i.e. adapter) to be used (like hci0, hci1, etc.). If null, the first availabel device is opened.
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    Scanner getScanner(final String deviceName) throws ScannerException;

}