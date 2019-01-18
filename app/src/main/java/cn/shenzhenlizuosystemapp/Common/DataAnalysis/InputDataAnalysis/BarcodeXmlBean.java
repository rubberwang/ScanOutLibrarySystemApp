package cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis;

public class BarcodeXmlBean {
  
    private String FGuid;
    private String FBarcodeName;
    private String FBarcodeContent;
   
    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFBarcodeName(String FBarcodeName) {
        this.FBarcodeName = FBarcodeName;
    }

    public void setFBarcodeContent(String FBarcodeContent) {
        this.FBarcodeContent = FBarcodeContent;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFBarcodeName() {
        return FBarcodeName;
    }

    public String getFBarcodeContent() {
        return FBarcodeContent;
    }
}
