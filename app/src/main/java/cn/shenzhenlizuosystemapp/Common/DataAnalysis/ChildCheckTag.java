package cn.shenzhenlizuosystemapp.Common.DataAnalysis;

public class ChildCheckTag {

    private String Guid;
    private String Name;
    private String BarcodeNumber;
    private String Value;

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
