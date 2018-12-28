package cn.shenzhenlizuosystemapp.Common.Port;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;

public interface TagModePort {
    void OnTagRes(List<MaterialModeBean> materialModeBeanList);
}
