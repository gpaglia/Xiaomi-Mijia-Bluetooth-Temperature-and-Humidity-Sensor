package com.gpaglia.bluetooth.lescan.api;

public enum ScanType {
    PASSIVE(0x00),
    ACTIVE(0x01);

    private final int type;

    private ScanType(final int type) {
        this.type = type;
    }

    public int getTypeCode() { return type; }
    
}