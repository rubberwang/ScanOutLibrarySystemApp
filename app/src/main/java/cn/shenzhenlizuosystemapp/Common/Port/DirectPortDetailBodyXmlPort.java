package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;

public interface DirectPortDetailBodyXmlPort {
    
    void OnBody(List<DirectAllotBodyBean> directAllotBodyBeanList);

}
