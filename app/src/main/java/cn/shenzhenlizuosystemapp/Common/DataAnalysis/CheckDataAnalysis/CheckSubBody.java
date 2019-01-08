package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckSubBody {
    private String FGuid;//子分录ID
    private String FBillBodyID;//分录ID，等于body分录ID
    private String FRowIndex;//子分录行号
    private String FBarcodeLib;//条码库
    private String FBarcodeLib_Name;//条码文本
    private String FBarcodeType;//管控类型ID
    private String FBarcodeType_Name;//管控类型名称
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

    public String getFRowIndex() {
        return FRowIndex;
    }

    public void setFRowIndex(String FRowIndex) {
        this.FRowIndex = FRowIndex;
    }

    public String getFBarcodeType() {
        return FBarcodeType;
    }

    public void setFBarcodeType(String FBarcodeType) {
        this.FBarcodeType = FBarcodeType;
    }

    public String getFBarcodeType_Name() {
        return FBarcodeType_Name;
    }

    public void setFBarcodeType_Name(String FBarcodeType_Name) {
        this.FBarcodeType_Name = FBarcodeType_Name;
    }
}
