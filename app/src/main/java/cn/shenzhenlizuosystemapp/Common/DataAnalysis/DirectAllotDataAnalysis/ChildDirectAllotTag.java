package cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDataAnalysis;

public class ChildDirectAllotTag {

    private String Guid;
    private String Name;
    private String BarcodeNumber;
    private String Value;
    private String FIsMust;

    public String getFIsMust() {
        return FIsMust;
    }

    public void setFIsMust(String FIsMust) {
        this.FIsMust = FIsMust;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        BarcodeNumber = barcodeNumber;
    }

    public String getBarcodeNumber() {
        return BarcodeNumber;
    }

    public String getGuid() {

        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getValue() {
        return Value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
