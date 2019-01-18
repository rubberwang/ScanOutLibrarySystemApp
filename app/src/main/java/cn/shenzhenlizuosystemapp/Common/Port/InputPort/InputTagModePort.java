package cn.shenzhenlizuosystemapp.Common.Port.InputPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.ChildTag;

public interface InputTagModePort {
     void MainResult(List<ChildTag> childTagList);
}
