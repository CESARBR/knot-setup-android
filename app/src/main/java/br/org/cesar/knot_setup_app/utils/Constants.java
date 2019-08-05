package br.org.cesar.knot_setup_app.utils;

import java.util.UUID;

public class Constants{
    public static final UUID OT_SETTINGS_SERVICE = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d30");
    public static final UUID CHANNEL_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d31");
    public static final UUID NET_NAME_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d32");
    public static final UUID PAN_ID_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d33"); //Int
    public static final UUID XPANID_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d34");
    public static final UUID MASTER_KEY_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d35");
    public static final UUID IPV6_SERVICE = UUID.fromString("49601183-5db4-498b-b35a-e6ddbe1c1470");
    public static final UUID IPV6_CHARACTERISTIC = UUID.fromString("49601183-5db4-498b-b35a-e6ddbe1c1471");

    public static final UUID OT_SETTINGS_SERVICE_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900e30");
    public static final UUID CHANNEL_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d31");
    public static final UUID NET_NAME_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d32");
    public static final UUID PAN_ID_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d33"); //Int
    public static final UUID XPANID_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d34");
    public static final UUID MASTER_KEY_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d35");
    public static final UUID IPV6_CHARACTERISTIC_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d36");

    //WIFI CONFIGURATION SERVICE AND CHARACTERISTICS
    public static final UUID WIFI_CONFIGURATION_SERVICE_GATEWAY = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900e40");
    public static final UUID WIFI_SSID_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d41");
    public static final UUID WIFI_PASSWORD_CHARACTERISTIC = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d42");

    public static final int CHANNEL_CHARACTERISTIC_BYTE_SIZE = 1;
    public static final int PANID_CHARACTERISTIC_BYTE_SIZE = 2;

    public static final String BASE_URL = "http://172.26.4.240:8080";

    public static final String DNS_SD_SERVICE_TYPE = "_services._dns-sd._udp";
    public static final String DNS_SD_SERVICE_NAME = "KNoT Gateway on";

    public static final String PERSISTENT_PREFERENCE_FILE = "persistent_file";
    public static final String PREFERENCE_FILE = "non_persistent_file";

    //PERMISSION IDS
    public static final int BLUETOOTH_PERMISSION_ID = 123;

    //REQUEST TIMEOUTS
    public static final int GET_OPENTHREAD_TIMEOUT = 30;

    //SCAN OPERATIONS
    public static final int CONFIGURE_THING_OPENTHREAD = 0;
    public static final int CONFIGURE_GATEWAY_WIFI = 1;

    //EXTRAS IDS
    public static final String GATEWAY_ID = "GATEWAY_ID";
    public static final int DEFAULT_GATEWAY_ID = 0;

    //USER PREFERENCES KEYS
    public static final String GATEWAY_IP = "gateway_ip";
    public static final String GATEWAY_PORT = "gateway_port";

    //KEYS FOR BUNDLES
    public static final String OPERATION = "operation";
}
