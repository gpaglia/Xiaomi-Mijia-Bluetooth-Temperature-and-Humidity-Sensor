package com.gpaglia.bluetooth.lescan.api;

import java.util.Set;

public interface ScanHandle extends AutoCloseable {

  /**
   * Checks if the scan operation is already active
   *  
   * @return true if scan is active on this handle
   */
  boolean isScanActive();

  /**
   * Resets the whitelist and then installs the new set of addresses in the whitelist
   * 
   * @param addresses the addresses to be installed in the whitelist filter
   * @throws HCIException in case of any problems
   */
  void setWhitelist(final Set<DeviceAddress> addresses) throws HCIException;

  /**
   * Resets the whitelist and then installs the new set of addresses in the whitelist
   * 
   * @param addresses the addresses to be installed in the whitelist filter
   * @throws HCIException in case of any problems
   */
  void setWhiteList(final DeviceAddress... addreses)  throws HCIException;

  /**
   * Starts the scan operation.
   * 
   * @param scanType The scan type to be activated (passive or active)
   * @param withWhitelist If true, the whitelist filter will be used, otherwise all adv from all devices will be reported
   * @param withDuplicates If true, report duplicate information, otherwise filter out duplicates
   * @param withPublicAddress If true, presents the adapter with its public address, otherwise will use a random generated address
   * @param interval The scan interval as a multiple of 0.625ms; must not be less than window
   * @param window The scan window as a multiple of 0.625ms; must not be greater than interval
   * @param timeout
   * @param callback
   * @throws HCIException
   */
  void startScan(
    final ScanType scanType, 
    final boolean withWhitelist, 
    final boolean withDuplicates,
    final boolean withPublicAddress, 
    final int interval, 
    final int window, 
    final int timeout,
    final ScanInfoCallback callback) throws HCIException;

    void stopScan() throws HCIException;

    default void close() {
      if (isScanActive()) {
        stopScan();
      }
    }
}