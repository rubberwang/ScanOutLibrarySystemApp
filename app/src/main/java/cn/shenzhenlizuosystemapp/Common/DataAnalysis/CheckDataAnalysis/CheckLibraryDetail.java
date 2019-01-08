package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckLibraryDetail {
    private String FGuid;
    private String FCode;
    private String FDate;
    private String FStock;
    private String FStock_Name;
    private String FAllowOtherMaterial;

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFCode(String FCode) {
        this.FCode = FCode;
    }

    public void setFStock(String FStock) {
        this.FStock = FStock;
    }

    public void setFStock_Name(String FStock_Name) {
        this.FStock_Name = FStock_Name;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFCode() {
        return FCode;
    }

    public String getFStock() {
        return FStock;
    }

    public String getFStock_Name() {
        return FStock_Name;
    }

    public String getFAllowOtherMaterial() {
        return FAllowOtherMaterial;
    }

    public void setFAllowOtherMaterial(String FAllowOtherMaterial) {
        this.FAllowOtherMaterial = FAllowOtherMaterial;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }
}
