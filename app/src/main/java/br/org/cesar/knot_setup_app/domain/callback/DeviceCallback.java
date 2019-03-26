package br.org.cesar.knot_setup_app.domain.callback;

public interface DeviceCallback {

    /**
     * When the pairing with the device was successful
     */
    void onConnect();

    /**
     * When disconnected from device
     */
    void onDisconnect();

    void onServiceDiscoveryComplete();

    void onServiceDiscoveryFail();

    void onCharacteristicWriteComplete();

    void onCharacteristicWriteFail();

    void onReadRssiComplete();

    void onReadRssiFail();

    void onCharacteristicReadComplete();

    void onCharacteristicReadFail();

    void onCharacteristicChanged();

    void onState();
}
