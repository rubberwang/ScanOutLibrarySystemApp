package cn.shenzhenlizuosystemapp.Common.DataAnalysis;

public class ChildTag {

    private String OneChildTag;
    private String TwoChildTag;
    private String Name;
    private String Qty;

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setOneChildTag(String oneChildTag) {
        OneChildTag = oneChildTag;
    }

    public void setTwoChildTag(String twoChildTag) {
        TwoChildTag = twoChildTag;
    }

    public String getOneChildTag() {
        return OneChildTag;
    }

    public String getTwoChildTag() {
        return TwoChildTag;
    }
}
