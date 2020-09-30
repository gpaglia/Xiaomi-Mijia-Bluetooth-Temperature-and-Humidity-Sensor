package com.gpaglia.bluetooth.lescan.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractScanner implements Scanner {
    private final int device;
    private boolean scanActive = false;

    // configuration parameter for the scan
    private int interval = DEFAULT_INTERVAL;
    private int window = DEFAULT_WINDOW;
    private Set<DeviceAddress> whitelist = new HashSet<>();
    private DiscoveryType discoveryType = DEFAULT_DISCOVERY_TYPE;
    private ScanType scanType = DEFAULT_SCAN_TYPE;
    private boolean duplicates = DEFAULT_DUPLICATES_ENABLED;
    private boolean publicAddr = DEFAULT_PUBLIC_ADDRESS_ENABLED;

    protected AbstractScanner(final int device) {
        this.device = device;
    }

    @Override
    public int getDevice() { 
        return this.device; 
    }

    @Override
    public synchronized boolean isScanActive() {
        return scanActive;
    }

    @Override
    public synchronized void startScan(int timeout, ScanInfoCallback callback) throws ScannerException {
        ensureScanInactive();
        scanActive = true;
        startScanLocal(timeout, callback);

    }

    @Override
    public synchronized void stopScan() throws ScannerException {
        if (isScanActive()) {
            stopScanLocal();
            scanActive = false;
        }
    }

    @Override
    public synchronized DiscoveryType getDiscoveryType() {
        return discoveryType;
    }

    @Override
    public synchronized int getInterval() {
        return interval;
    }

    @Override
    public synchronized ScanType getScanType() {
        return scanType;
    }

    @Override
    public synchronized Set<DeviceAddress> getWhilelist() {
        return new HashSet<>(whitelist);
    }

    @Override
    public synchronized int getWindow() {
        return window;
    }

    @Override
    public synchronized boolean isDuplicatesEnabled() {
        return duplicates;
    }

    @Override
    public synchronized boolean isPublicAddressEnabled() {
        return publicAddr;
    }

    // setters - will throw exception if called when scan session is active

    @Override
    public synchronized void setDiscoveryType(DiscoveryType type) throws ScannerException {
        ensureScanInactive();
        this.discoveryType = type;
    }

    @Override
    public synchronized void setDuplicatesEnabled(boolean flag) throws ScannerException {
        ensureScanInactive();
        this.duplicates = flag;
    }

    @Override
    public synchronized void setInterval(int interval) throws ScannerException {
        ensureScanInactive();
        if (interval > 0 && interval <= MAX_INTERVAL) {
            this.interval = interval;
        } else {
            throw new IllegalArgumentException("Invalid interval value (0x0001 to 0x4000)");
        }
    }

    @Override
    public synchronized void setPublicAddressEnabled(boolean flag) throws ScannerException {
        ensureScanInactive();
        this.publicAddr = flag;
    }

    @Override
    public synchronized void setScanType(ScanType scanType) throws ScannerException {
        ensureScanInactive();
        this.scanType = scanType;
    }

    @Override
    public synchronized void setWhitelist(Set<DeviceAddress> addresses) throws ScannerException {
        ensureScanInactive();
        this.whitelist = new HashSet<>(addresses);
    }

    @Override
    public synchronized void setWhiteList(DeviceAddress... addresses) throws ScannerException {
        ensureScanInactive();
        this.whitelist = new HashSet<>(Arrays.asList(addresses));
    }

    @Override
    public synchronized void setWindow(int window) throws ScannerException {
        ensureScanInactive();
        if (window > 0 && window <= MAX_INTERVAL) {
            this.window = window;
        } else {
            throw new IllegalArgumentException("Invalid window value (0x0001 to 0x4000)");
        }

    }

    // abstract methods to be implemented in subclasses
    protected abstract void startScanLocal(int timeout, ScanInfoCallback callback) throws ScannerException;

    protected abstract void stopScanLocal() throws ScannerException;

    // protected methods to be used by subclasses

    protected void reportScanCompleted() {
        this.scanActive = false;
    }

    // private and utility methods

    private void ensureScanInactive() throws ScannerException {
        if (scanActive) {
            throw new ScannerException("Scan session already active!!");
        }
    }

}
