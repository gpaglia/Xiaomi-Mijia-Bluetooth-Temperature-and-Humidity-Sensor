package com.gpaglia.bluetooth.lescan.api;

public class DeviceAddress {
    private final String address;

    public DeviceAddress(final String str) {
        if (str.matches("[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}")) {
            address = str.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid address " + str);
        }
    }

    public String getAddress() { return address; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceAddress other = (DeviceAddress) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DeviceAddress [address=" + address + "]";
    }

    
}