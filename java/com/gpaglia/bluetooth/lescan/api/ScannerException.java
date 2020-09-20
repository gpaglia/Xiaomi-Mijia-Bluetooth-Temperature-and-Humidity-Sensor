package com.gpaglia.bluetooth.lescan.api;

public class ScannerException extends Exception {

    private static final long serialVersionUID = 1L;
    private final int errno;

    public ScannerException(String message, int errno) {
        super(message);
        this.errno = errno;
    }

    public ScannerException(String message) {
        this(message, -1);
    }

    public ScannerException(Throwable cause, int errno) {
        super(cause);
        this.errno = errno;
    }

    public ScannerException(Throwable cause) {
        this(cause, -1);
    }

    public ScannerException(String message, Throwable cause, int errno) {
        super(message, cause);
        this.errno = errno;
    }

    public ScannerException(String message, Throwable cause) {
        this(message, cause, -1);
    }
   
    public int getErrno() {
        return errno;
    }
    
}