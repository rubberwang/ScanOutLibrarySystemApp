package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotTaskRvData;

public interface OutAllotDetailPort {
    void OnRes(String Res);

    void OnError(String ErrorStr);
}
