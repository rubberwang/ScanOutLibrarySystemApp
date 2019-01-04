package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckMaterialModeBean {
    private String FGuid;
    private String FName;//标签模板名字
    private String FBarCoeeCount;//条码数量

    public String getFBarCoeeCount() {
        return FBarCoeeCount;
    }

    public void setFBarCoeeCount(String FBarCoeeCount) {
        this.FBarCoeeCount = FBarCoeeCount;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFName() {
        return FName;
    }
}
