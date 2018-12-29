package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotTaskRvData;

public interface DirectPortDetailBodyXmlPort {
    
    void OnBody(List<DirectAllotTaskRvData> directAllotTaskRvDataList);

}
