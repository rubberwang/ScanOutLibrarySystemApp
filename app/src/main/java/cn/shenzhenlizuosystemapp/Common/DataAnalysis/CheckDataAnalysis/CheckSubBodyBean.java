package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

public class CheckSubBodyBean {
    private String FBillBodyID;
    private String FBarcodeLib;
    private String QuitLibrarySum;
    private String FStockCellID;

    public String getFStockCellID() {
        return FStockCellID;
    }

    public void setFStockCellID(String FStockCellID) {
        this.FStockCellID = FStockCellID;
    }

    public String getInputLibrarySum() {
        return QuitLibrarySum;
    }

    public void setInputLibrarySum(String quitLibrarySum) {
        QuitLibrarySum = quitLibrarySum;
    }

    public String getFBillBodyID() {
        return FBillBodyID;
    }

    public String getFBarcodeLib() {
        return FBarcodeLib;
    }

    public void setFBillBodyID(String FBillBodyID) {
        this.FBillBodyID = FBillBodyID;
    }

    public void setFBarcodeLib(String FBarcodeLib) {
        this.FBarcodeLib = FBarcodeLib;
    }
}
