package cn.shenzhenlizuosystemapp.Common.Port.CheckPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;

public interface CheckGetLabelTempletPort {
    void OnResult(List<ChildCheckTag> childTagList);
    void OnError(String ErrotInfo);
}
