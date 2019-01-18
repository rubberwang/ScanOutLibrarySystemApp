package cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.StockBean;

public interface DirectStockCellPort {
    void OnOutRes(List<StockBean> Out_stockBeanList);

    void OnInRes(List<StockBean> In_stockBeanList);
}
