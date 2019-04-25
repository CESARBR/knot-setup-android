package br.org.cesar.knot_setup_app.model;

import android.util.Log;

public class Thing {

    /*
     * These attributes are set by the bluetooth connection
     * */
    private Integer ID;
    private String nickname;
    private String channel;
    private String netName;
    private String panID;
    private String xpanID;
    private String masterkey;
    private String ipv6;

    /*These attributes are set by the backend of the webui*/
    private String id;
    private String name;
    private String uuid;
    private boolean online;
    private boolean paired;
    private boolean registered;

    public Thing(Integer id, String name, String channel , String netName , String panID, String xpanID, String masterkey, String ipv6){
        this.ID = id;
        this.nickname = name;
        this.channel = channel;
        this. netName = netName;
        this.panID = panID;
        this.xpanID = xpanID;
        this.masterkey = masterkey;
        this.ipv6 = ipv6;
    }
    public Thing(){}

    public void printThingSettings(){
        Log.d("DEV-LOG",this.nickname);
        Log.d("DEV-LOG",this.channel);
        Log.d("DEV-LOG",this.netName);
        Log.d("DEV-LOG",this.panID);
        Log.d("DEV-LOG",this.xpanID);
        Log.d("DEV-LOG",this.ipv6);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getPanID() {
        return panID;
    }

    public void setPanID(String panID) {
        this.panID = panID;
    }

    public String getXpanID() {
        return xpanID;
    }

    public void setXpanID(String xpanID) {
        this.xpanID = xpanID;
    }

    public String getMasterkey() {
        return masterkey;
    }

    public void setMasterkey(String masterkey) {
        this.masterkey = masterkey;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }
}
