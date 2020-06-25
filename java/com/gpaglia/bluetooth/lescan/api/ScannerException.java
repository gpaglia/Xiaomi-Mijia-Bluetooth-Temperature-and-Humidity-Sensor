package java.com.gpaglia.bluetooth.lescan.api;

public class ScannerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int errno = 0;

    public ScannerException(String msg) {
        super(msg);
    }

    public ScannerException(String msg, int errno) {
        super(msg);
        this.errno = errno;
    }

    public ScannerException(Throwable cause) {
        super(cause);
    }

    public ScannerException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public int getErrno() { return errno; }
    
}