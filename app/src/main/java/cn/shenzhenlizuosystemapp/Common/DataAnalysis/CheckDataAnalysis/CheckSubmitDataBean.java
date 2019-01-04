package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckSubmitDataBean {
    private String FGuid;
    private String FBillID;
    private String FMaterial;
    private String FUnit;
    private String FQty;
    private String FPrice;

    public String getFPrice() {
        return FPrice;
    }

    public void setFPrice(String FPrice) {
        this.FPrice = FPrice;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFBillID() {
        return FBillID;
    }

    public String getFMaterial() {
        return FMaterial;
    }

    public String getFUnit() {
        return FUnit;
    }

    public String getFQty() {
        return FQty;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFBillID(String FBillID) {
        this.FBillID = FBillID;
    }

    public void setFMaterial(String FMaterial) {
        this.FMaterial = FMaterial;
    }

    public void setFUnit(String FUnit) {
        this.FUnit = FUnit;
    }

    public void setFQty(String FQty) {
        this.FQty = FQty;
    }
}
