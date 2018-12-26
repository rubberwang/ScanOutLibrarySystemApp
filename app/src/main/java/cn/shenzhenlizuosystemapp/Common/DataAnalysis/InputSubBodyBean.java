package cn.shenzhenlizuosystemapp.Common.DataAnalysis;

public class InputSubBodyBean {
    private String FBillBodyID;
    private String FBarcodeLib;
    private String InputLibrarySum;
    private String FStockCellID;

    public String getFStockCellID() {
        return FStockCellID;
    }

    public void setFStockCellID(String FStockCellID) {
        this.FStockCellID = FStockCellID;
    }

    public String getInputLibrarySum() {
        return InputLibrarySum;
    }

    public void setInputLibrarySum(String inputLibrarySum) {
        InputLibrarySum = inputLibrarySum;
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
