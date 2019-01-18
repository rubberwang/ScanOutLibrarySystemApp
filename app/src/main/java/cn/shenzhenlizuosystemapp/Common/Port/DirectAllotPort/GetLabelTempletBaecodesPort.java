package cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.ChildTag;

public interface GetLabelTempletBaecodesPort {
    void OnResult(List<ChildTag> childTagList);
    void OnError(String ErrotInfo);
}
