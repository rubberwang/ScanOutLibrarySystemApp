package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;

public interface DirectPortDetailHeadXmlPort {
    
    void OnHead(List<DirectAllotHeadBean> directAllotHeadBeanList);

}
