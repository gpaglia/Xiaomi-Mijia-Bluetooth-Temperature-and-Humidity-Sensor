package com.gpaglia.bluetooth.lescan.api;

public class HCIException extends ScannerException {

    private static final long serialVersionUID = 1L;

    private int errno = 0;

    public HCIException(String msg) {
        super(msg);
    }

    public HCIException(String msg, int errno) {
        super(msg);
        this.errno = errno;
    }

    public HCIException(Throwable cause) {
        super(cause);
    }

    public HCIException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public int getErrno() { return errno; }
    
}