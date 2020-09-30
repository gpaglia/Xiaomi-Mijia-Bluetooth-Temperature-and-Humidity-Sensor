package com.gpaglia.bluetooth.lescan.hci;

import com.gpaglia.bluetooth.lescan.api.ScannerException;

public class HCIException extends ScannerException {

    private static final long serialVersionUID = 1L;

    public HCIException(String msg, int errno) {
        super(msg, errno);
    }

    public HCIException(String msg) {
        super(msg);
    }

    public HCIException(String msg, Throwable cause, int errno) {
        super(msg, cause, errno);
    }

    public HCIException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public HCIException(Throwable cause, int errno) {
        super(cause, errno);
    }

    public HCIException(Throwable cause) {
        super(cause);
    }

    
}