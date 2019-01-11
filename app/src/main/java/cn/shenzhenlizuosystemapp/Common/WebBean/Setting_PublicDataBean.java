package cn.shenzhenlizuosystemapp.Common.WebBean;

public class Setting_PublicDataBean {
    private String FGuid;
    private String FName;
    private String FServer;
    private String FPort;
    private String FVirtualPath;

    public String getFGuid() {
        return FGuid;
    }

    public String getFName() {
        return FName;
    }

    public String getFServer() {
        return FServer;
    }

    public String getFPort() {
        return FPort;
    }

    public String getFVirtualPath() {
        return FVirtualPath;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public void setFServer(String FServer) {
        this.FServer = FServer;
    }

    public void setFPort(String FPort) {
        this.FPort = FPort;
    }

    public void setFVirtualPath(String FVirtualPath) {
        this.FVirtualPath = FVirtualPath;
    }
}
