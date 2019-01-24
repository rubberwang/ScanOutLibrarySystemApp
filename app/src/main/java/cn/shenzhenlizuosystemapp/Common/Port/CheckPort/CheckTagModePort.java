package cn.shenzhenlizuosystemapp.Common.Port.CheckPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckMaterialModeBean;

public interface CheckTagModePort {
    void OnTagRes(List<CheckMaterialModeBean> checkMaterialModeBeanList);
}
