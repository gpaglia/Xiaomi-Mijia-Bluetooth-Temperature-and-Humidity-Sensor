package com.gpaglia.bluetooth.lescan.api;

public interface ScanInfoCallback {
    void reportAd(String address, byte[] info);
}