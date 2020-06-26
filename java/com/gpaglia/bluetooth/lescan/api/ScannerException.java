package com.gpaglia.bluetooth.lescan.api;

public class ScannerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ScannerException(String message) {
        super(message);
    }

    public ScannerException(Throwable cause) {
        super(cause);
    }

    public ScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    
    
}