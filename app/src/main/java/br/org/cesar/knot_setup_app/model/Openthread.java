package br.org.cesar.knot_setup_app.model;

public class Openthread {

    private String state;
    private String nodeType;
    private String networkName;
    private String panId;
    private String channel;
    private String xpanId;
    private String meshIpv6;
    private String masterKey;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getPanId() {
        return panId;
    }

    public void setPanId(String panId) {
        this.panId = panId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getXpanId() {
        return xpanId;
    }

    public void setXpanId(String xpanId) {
        this.xpanId = xpanId;
    }

    public String getMeshIpv6() {
        return meshIpv6;
    }

    public void setMeshIpv6(String meshIpv6) {
        this.meshIpv6 = meshIpv6;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

}
