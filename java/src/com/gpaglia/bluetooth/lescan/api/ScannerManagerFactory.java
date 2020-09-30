package com.gpaglia.bluetooth.lescan.api;

import java.util.Optional;
import java.util.ServiceLoader;


public final class ScannerManagerFactory {
    @SuppressWarnings("java:S3008")
    private static ScannerManager INSTANCE = null;

    private ScannerManagerFactory() {}

    public static synchronized ScannerManager getScannerManager() {
        if (INSTANCE == null) {
            final Optional<ScannerManager> found = ServiceLoader
                .load(ScannerManager.class)
                .findFirst();

            if (found.isPresent()) {
                INSTANCE = found.get();
            } else {
                throw new IllegalStateException(
                    "No implementations of " 
                    + ScannerManager.class.toString() 
                    + " found!!");

            }
        }

        return INSTANCE;
    }
}