package cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree;

import java.io.Serializable;

public class TreeMBean implements Serializable {
    private String FGuid;
    private String FCode;//物料编号
    private String FName;//物料名称
    private String FModel;//品名规格
    private String FBaseUnit;
    private String FBaseUnit_Name;//单位

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFCode(String FCode) {
        this.FCode = FCode;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public void setFBaseUnit(String FBaseUnit) {
        this.FBaseUnit = FBaseUnit;
    }

    public void setFBaseUnit_Name(String FBaseUnit_Name) {
        this.FBaseUnit_Name = FBaseUnit_Name;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFCode() {
        return FCode;
    }

    public String getFName() {
        return FName;
    }

    public String getFModel() {
        return FModel;
    }

    public String getFBaseUnit() {
        return FBaseUnit;
    }

    public String getFBaseUnit_Name() {
        return FBaseUnit_Name;
    }
}
