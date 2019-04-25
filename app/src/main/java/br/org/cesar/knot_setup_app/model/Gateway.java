package br.org.cesar.knot_setup_app.model;

public class Gateway {

    /*
     * These attributes are set by the bluetooth connection
    * */
    private Integer ID;
    private String name;
    private String channel;
    private String netName;
    private String panID;
    private String xpanID;
    private String masterkey;
    private String ipv6;

    /*These attributes are acquired by the backend of the webui*/
    private String id;
    private int v;
    private String token;
    private String uuid;

     public Gateway(Integer id, String name, String channel, String netName, String panID, String xpanID, String masterkey, String ipv6){
        this.ID = id;
        this.name = name;
        this.channel = channel;
        this. netName = netName;
        this.panID = panID;
        this.xpanID = xpanID;
        this.masterkey = masterkey;
        this.ipv6 = ipv6;
    }

    public Gateway(){}

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
