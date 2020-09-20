package com.gpaglia.bluetooth.lescan.api;

import java.util.Set;

@SuppressWarnings("java:S1214")
public interface Scanner extends AutoCloseable {
  static int DEFAULT_WINDOW = 0x0040;
  static int DEFAULT_INTERVAL = 0x0030;
  static int MAX_INTERVAL = 0x4000;
  static ScanType DEFAULT_SCAN_TYPE = ScanType.PASSIVE;
  static DiscoveryType DEFAULT_DISCOVERY_TYPE = DiscoveryType.GENERAL;
  static boolean DEFAULT_DUPLICATES_ENABLED = false;
  static boolean DEFAULT_PUBLIC_ADDRESS_ENABLED = true;

  /**
   * Gets the ble device (i.e. adapter) number that this scanner is configured with
   * @return The ble adapter device number
   */
  int getDevice();

  /**
   * Checks if the scan operation is already active
   *  
   * @return true if scan is active on this handle
   */
  boolean isScanActive();

  /**
   * 
   * @return a {@link Set} of {@link DeviceAddress} representing the current whitelist
   * 
   * @throws HCIException
   */
  Set<DeviceAddress> getWhilelist();

  /**
   * Resets the whitelist and then installs the new set of addresses in the whitelist
   * 
   * @param addresses the {@link Set} of addresses to be installed in the whitelist filter
   * @throws HCIException in case of any problems
   */
  void setWhitelist(final Set<DeviceAddress> addresses) throws ScannerException;

  /**
   * Resets the whitelist and then installs the new set of addresses in the whitelist
   * 
   * @param addresses the addresses to be installed in the whitelist filter
   * @throws HCIException in case of any problems
   */
  @SuppressWarnings("java:S1845")
  void setWhiteList(final DeviceAddress... addresses) throws ScannerException;

  /**
   * Gets the configured scan type
   * 
   * @return The type of scan (ACTIBE or PASSIVE)
   */
  ScanType getScanType();

  /**
   * Sets the configured scan type
   * 
   * @param scanType The {@link ScanType} scan type to be configured (ACTIBE or PASSIVE)
   */
  void setScanType(final ScanType scanType) throws ScannerException;

  /**
   * Check if dupicates are enabled.
   * 
   * @return true if duplicates are enabled.
   */
  boolean isDuplicatesEnabled();

  /**
   * Enables or disables duplicates
   * @param flag if true, duplicates are enabled, otherwise they are disabled
   */
  void setDuplicatesEnabled(final boolean flag) throws ScannerException;

  /**
   * Gets the configured type of discovery 
   * @return The {@link DiscoveryType} type of discovery, GENERAL or LIMITED.
   */
  DiscoveryType getDiscoveryType();

  /**
   * Sets the cofigured type of discovery
   * @param type The {@link DiscoveryType} type of discovery to be configured, GENERAL or LIMITED.
   */
  void setDiscoveryType(final DiscoveryType type) throws ScannerException;

  /**
   * Check if public address is enabled
   * @return true if public address is enabled
   */
  boolean isPublicAddressEnabled();

  /**
   * Enables or disables public address
   * @param flag If true, the public address feature will be enabled, otherwise it will be disabled.
   */
  void setPublicAddressEnabled(final boolean flag) throws ScannerException;

  /**
   * Gets the scan internal in 0.625ms units
   * @return The scan interval in 0.625ms units
   */
  int getInterval();

  /**
   * Sets the scan internal, in 0.625ms units
   * @param interval The interval to be configured, in 0.625ms units
   */
  void setInterval(final int interval) throws ScannerException;

  /**
   * Gets the scan window in 0.625ms units
   * @return The scan window in 0.625ms units
   */
  int getWindow();

  /**
   * Sets the scan window, in 0.625ms units
   * @param interval The window to be configured, in 0.625ms units
   */
  void setWindow(final int window) throws ScannerException;


  /**
   * Starts the scan operation.
   * @param timeout
   * @param callback
   * @throws ScannerException
   */
  void startScan(final int timeout, final ScanInfoCallback callback) throws ScannerException;

  void stopScan() throws ScannerException;

  default void close() throws ScannerException {
    if (isScanActive()) {
      stopScan();
    }
  }
}