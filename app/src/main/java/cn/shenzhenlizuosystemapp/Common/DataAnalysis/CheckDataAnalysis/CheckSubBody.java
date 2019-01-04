package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckSubBody {
    private String FGuid;
    private String FBillBodyID;
    private String FBarcodeLib;
    private String FBarcodeLib_Name;//条码文本
    private String FAccountQty;//账存数量
    private String FCheckQty; //盘点数量
    private String FDiffQty;//差异数量
    private String FCheckStockStatus;//盘点状态

    public String getFGuid() {
        return FGuid;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFBillBodyID(String FBillBodyID) {
        this.FBillBodyID = FBillBodyID;
    }

    public void setFBarcodeLib(String FBarcodeLib) {
        this.FBarcodeLib = FBarcodeLib;
    }

    public String getFBillBodyID() {
        return FBillBodyID;
    }

    public String getFBarcodeLib() {
        return FBarcodeLib;
    }

    public String getFBarcodeLib_Name() {
        return FBarcodeLib_Name;
    }

    public void setFBarcodeLib_Name(String FBarcodeLib_Name) {
        this.FBarcodeLib_Name = FBarcodeLib_Name;
    }

    public String getFAccountQty() {
        return FAccountQty;
    }

    public void setFAccountQty(String FAccountQty) {
        this.FAccountQty = FAccountQty;
    }

    public String getFCheckQty() {
        return FCheckQty;
    }

    public void setFCheckQty(String FCheckQty) {
        this.FCheckQty = FCheckQty;
    }

    public String getFDiffQty() {
        return FDiffQty;
    }

    public void setFDiffQty(String FDiffQty) {
        this.FDiffQty = FDiffQty;
    }

    public String getFCheckStockStatus() {
        return FCheckStockStatus;
    }

    public void setFCheckStockStatus(String FCheckStockStatus) {
        this.FCheckStockStatus = FCheckStockStatus;
    }
}
