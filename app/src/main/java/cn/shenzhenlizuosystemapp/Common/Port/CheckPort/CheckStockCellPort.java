package cn.shenzhenlizuosystemapp.Common.Port.CheckPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStockBean;

public interface CheckStockCellPort {
    void OnRes(List<CheckStockBean> checkStockBeanList);
}
