package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;

public interface GetLabelTempletBaecodesPort {
    void OnResult(List<ChildTag> childTagList);
    void OnError(String ErrotInfo);
}
