package com.gpaglia.bluetooth.lescan.api;

public enum DiscoveryType {
    GENERAL('g'), 
    LIMITED('l');

    private final char type;

    private DiscoveryType(final char type) {
        this.type = type;
    }

    public char getTypeCode() { return type; }
}
