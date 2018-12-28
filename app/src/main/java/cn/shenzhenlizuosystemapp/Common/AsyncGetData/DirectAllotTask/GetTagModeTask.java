package cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.TagModePort;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;

public class GetTagModeTask extends AsyncTask<String, Void, String> {

    private TagModePort tagModePort;
    private WebService webService;
    private String MaterialID;

    public GetTagModeTask(TagModePort tagModePort, WebService webService, String MaterialID) {
        this.tagModePort = tagModePort;
        this.webService = webService;
        this.MaterialID = MaterialID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, MaterialID);
            ViseLog.i("GetTagModeTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            Is_statu.close();
            if (statureslist.get(0).getFStatus().equals("1")) {
                return statureslist.get(0).getFInfo();
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("Out_StockCellTask Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            ViseLog.i("Out_StockCellTask result = " + result);
            try {
                InputStream Is_Xml = new ByteArrayInputStream(result.getBytes("UTF-8"));
                tagModePort.OnTagRes(AnalysisMaterialModeXml.getSingleton().GetMaterialModeInfo(Is_Xml));
            } catch (Exception e) {
                ViseLog.i("Out_StockCellTask result Exception = " + result);
            }
        }
    }
}