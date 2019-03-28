package br.org.cesar.knot_setup_app.model;

import android.support.annotation.NonNull;


public class BluetoothDevice implements Comparable<BluetoothDevice> {

    private android.bluetooth.BluetoothDevice device;
    private int rssi;
    private String pin;


    public BluetoothDevice(android.bluetooth.BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }


    public android.bluetooth.BluetoothDevice getDevice() {
        return device;
    }

    public int getRssi() {
        return rssi;
    }


    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public int compareTo(@NonNull BluetoothDevice device) {
        return device.getRssi() - rssi;
    }
}
