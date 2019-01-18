package cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDataAnalysis.DirectAllotDetail;

public interface DirectPortDetailHeadXmlPort {
    
    void OnHead(List<DirectAllotDetail> directAllotDetailList);

}
