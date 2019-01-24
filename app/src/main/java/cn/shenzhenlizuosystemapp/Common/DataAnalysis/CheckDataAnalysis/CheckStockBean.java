package cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis;

import java.util.List;

public class CheckStockBean {
    private String FGuid;
    private String FName;
    private List<CheckSubBody> checkSubBody;
    private List<CheckTaskRvData> checkTaskRvData;

    public String getFGuid() {
        return FGuid;
    }

    public String getFName() {
        return FName;
    }

    public void setFGuid(String FGuid) {
        this.FGuid = FGuid;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public List<CheckSubBody> getCheckSubBody() {
        return checkSubBody;
    }

    public void setCheckSubBody(List<CheckSubBody> checkSubBody) {
        this.checkSubBody = checkSubBody;
    }

    public List<CheckTaskRvData> getCheckTaskRvData() {
        return checkTaskRvData;
    }

    public void setCheckTaskRvData(List<CheckTaskRvData> checkTaskRvData) {
        this.checkTaskRvData = checkTaskRvData;
    }
}
