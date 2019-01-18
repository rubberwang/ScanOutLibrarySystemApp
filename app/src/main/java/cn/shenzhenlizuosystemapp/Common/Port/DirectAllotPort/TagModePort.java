package cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.MaterialModeBean;

public interface TagModePort {
    void OnTagRes(List<MaterialModeBean> materialModeBeanList);
}
