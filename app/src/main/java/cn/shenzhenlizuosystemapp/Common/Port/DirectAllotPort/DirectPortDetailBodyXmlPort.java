package cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDataAnalysis.DirectAllotTaskRvData;

public interface DirectPortDetailBodyXmlPort {
    
    void OnBody(List<DirectAllotTaskRvData> directAllotTaskRvDataList);

}
