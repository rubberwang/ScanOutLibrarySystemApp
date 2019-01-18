package cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDataAnalysis;

public class CreateAdjustStockBean {
    private String FBillBodyID;
    private String FBarcodeLib;
    private String FAuxQty;

    public void setFBillBodyID(String FBillBodyID) {
        this.FBillBodyID = FBillBodyID;
    }

    public void setFBarcodeLib(String FBarcodeLib) {
        this.FBarcodeLib = FBarcodeLib;
    }

    public void setFAuxQty(String FAuxQty) {
        this.FAuxQty = FAuxQty;
    }

    public String getFBillBodyID() {
        return FBillBodyID;
    }

    public String getFBarcodeLib() {
        return FBarcodeLib;
    }

    public String getFAuxQty() {
        return FAuxQty;
    }
}
