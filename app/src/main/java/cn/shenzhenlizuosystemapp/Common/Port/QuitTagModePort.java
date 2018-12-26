package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildQuitTag;

public interface QuitTagModePort {
     void MainResult(List<ChildQuitTag> childQuitTagList);
}
