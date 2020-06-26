package com.gpaglia.bluetooth.lescan.api;

public enum ScanType {
    PASSIVE(0x00),
    ACTIVE(0x01);

    private final int scanType;

    private ScanType(int type) {
        this.scanType = type;
    }

    public int getScanTypeCode() { return scanType; }
    
}