package cn.shenzhenlizuosystemapp.Common.Port.QuitPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.ChildQuitTag;

public interface QuitTagModePort {
     void MainResult(List<ChildQuitTag> childQuitTagList);
}
