package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckTaskRvData {
    private String FGuid;
    private String FMaterial;
    private String FMaterial_Code; //物料编号
    private String FMaterial_Name;//品名
    private String FModel;//规格
    private String FBaseUnit_Name;//基本单位
    private String FUnit_Name;
    private String FAccountQty;//账存数量
    private String FCheckQty; //盘点数量
    private String FDiffQty;//差异数量

    public String getFDiffQty(){
        return FDiffQty;
    }

    public void setFDiffQty(String noSend) {
        FDiffQty = noSend;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFMaterial(String FMaterial) {
        this.FMaterial = FMaterial;
    }

    public void setFMaterial_Code(String FMaterial_Code) {
        this.FMaterial_Code = FMaterial_Code;
    }

    public void setFMaterial_Name(String FMaterial_Name) {
        this.FMaterial_Name = FMaterial_Name;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public void setFBaseUnit_Name(String FBaseUnit_Name) {
        this.FBaseUnit_Name = FBaseUnit_Name;
    }

    public void setFAccountQty(String FAccountQty) {
        this.FAccountQty = FAccountQty;
    }


    public void setFCheckQty(String FCheckQty) {
        this.FCheckQty = FCheckQty;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFMaterial() {
        return FMaterial;
    }

    public String getFMaterial_Code() {
        return FMaterial_Code;
    }

    public String getFMaterial_Name() {
        return FMaterial_Name;
    }

    public String getFModel() {
        return FModel;
    }

    public String getFBaseUnit_Name() {
        return FBaseUnit_Name;
    }

    public String getFAccountQty() {
        return FAccountQty;
    }

    public String getFCheckQty() {
        return FCheckQty;
    }

    public String getFUnit_Name() {
        return FUnit_Name;
    }

    public void setFUnit_Name(String FUnit_Name) {
        this.FUnit_Name = FUnit_Name;
    }
}
