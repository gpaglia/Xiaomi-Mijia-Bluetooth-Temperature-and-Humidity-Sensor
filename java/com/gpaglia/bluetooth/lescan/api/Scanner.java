package com.gpaglia.bluetooth.lescan.api;

public interface Scanner {

    /**
     * Opens an hci device on the local amchine returning the handle to be used for scanning oeprations.
     * 
     * @param hciDeviceNo The device number of the local device (i.e. adapter). If null or negative, the first device found will be used
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    ScanHandle openHciDevice(Integer hciDeviceNo) throws HCIException;

    /**
     * Opens an hci device on the local amchine returning the handle to be used for scanning oeprations.
     * 
     * @param adapterAddress The MAC address of the local device (i.e. adapter) to open. If null, the first available device is opeend.
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    ScanHandle openDevice(DeviceAddress adapterAddress) throws HCIException;

    /**
     * Opens an hci device on the local amchine returning the handle to be used for scanning oeprations.
     * @param deviceName the name of the device (i.e. adapter) to be used (like hci0, hci1, etc.). If null, the first availabel device is opened.
     * @return a Handle to be used with any subsequent operations
     * @throws HCIException if the device is not found, does not exist or cannot be opened
     */
    ScanHandle openDevice(String deviceName) throws HCIException;

}