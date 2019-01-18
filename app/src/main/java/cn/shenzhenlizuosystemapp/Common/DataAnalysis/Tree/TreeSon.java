package cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree.TreeParent;

public class TreeSon {
    private List<TreeParent> treeParents;

    public List<TreeParent> getTreeParents() {
        return treeParents;
    }

    public void setTreeParents(List<TreeParent> treeParents) {
        this.treeParents = treeParents;
    }
}
