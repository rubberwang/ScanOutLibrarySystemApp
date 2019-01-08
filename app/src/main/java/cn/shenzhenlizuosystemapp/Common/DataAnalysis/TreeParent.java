package cn.shenzhenlizuosystemapp.Common.DataAnalysis;

import com.vise.log.inner.Tree;

import java.util.List;

public class TreeParent {
    private String FGuid;
    private String FCode;
    private String FName;
    private String FParent;
    private int Level;
    private List<TreeParent> treeSonList;

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        Level = Level;
    }

    public List<TreeParent> getTreeSonList() {
        return treeSonList;
    }

    public String getFParent() {
        return FParent;
    }

    public void setTreeSonList(List<TreeParent> treeSonList) {
        this.treeSonList = treeSonList;
    }

    public void setFParent(String FParent) {
        this.FParent = FParent;
    }

    public String getFGuid() {
        return FGuid;
    }

    public String getFCode() {
        return FCode;
    }

    public String getFName() {
        return FName;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFCode(String FCode) {
        this.FCode = FCode;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }
}
