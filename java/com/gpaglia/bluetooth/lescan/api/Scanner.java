package java.com.gpaglia.bluetooth.lescan.api;

public interface Scanner extends AutoCloseable {
    /**
     * 
     * @param deviceNo The device number, must be non negative. If null, the first device found will be used
     * @return a Handle to be used with any subsequent operations
     */
    ScanHandle openDevice(Integer deviceNo);

}