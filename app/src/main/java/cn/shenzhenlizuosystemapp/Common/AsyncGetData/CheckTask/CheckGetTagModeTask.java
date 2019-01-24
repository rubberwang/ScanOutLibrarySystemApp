package cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckTagModePort;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;

public class CheckGetTagModeTask extends AsyncTask<String, Void, String> {

    private CheckTagModePort checkTagModePort;
    private WebService webService;
    private String MaterialID;

    public CheckGetTagModeTask(CheckTagModePort checkTagModePort, WebService webService, String MaterialID) {
        this.checkTagModePort = checkTagModePort;
        this.webService = webService;
        this.MaterialID = MaterialID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, MaterialID);
            ViseLog.i("CheckGetTagModeTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<CheckAdapterReturn> statureslist = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            Is_statu.close();
            if (statureslist.get(0).getFStatus().equals("1")) {
                return statureslist.get(0).getFInfo();
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("CheckStockCellTask Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            ViseLog.i("CheckStockCellTask result = " + result);
            try {
                InputStream Is_Xml = new ByteArrayInputStream(result.getBytes("UTF-8"));
                checkTagModePort.OnTagRes(CheckAnalysisMaterialModeXml.getSingleton().GetMaterialModeInfo(Is_Xml));
            } catch (Exception e) {
                ViseLog.i("CheckStockCellTask result Exception = " + result);
            }
        }
    }
}