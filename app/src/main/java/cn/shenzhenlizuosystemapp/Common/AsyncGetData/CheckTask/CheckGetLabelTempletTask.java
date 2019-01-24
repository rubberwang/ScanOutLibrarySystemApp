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
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckGetLabelTempletPort;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckTagModeAnalysis;

public class CheckGetLabelTempletTask extends AsyncTask<String, Void, String> {

    private CheckGetLabelTempletPort checkGetLabelTempletPort;
    private WebService webService;
    private String LabelTempletID;

    public CheckGetLabelTempletTask(CheckGetLabelTempletPort checkGetLabelTempletPort, WebService webService, String LabelTempletID) {
        this.checkGetLabelTempletPort = checkGetLabelTempletPort;
        this.webService = webService;
        this.LabelTempletID = LabelTempletID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
            ViseLog.i("CheckGetLabelTempletPortTask StatuResStr = " + ConnectStr.ConnectionToString + "," + LabelTempletID);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<CheckAdapterReturn> statureslist = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            Is_statu.close();
            if (statureslist.get(0).getFStatus().equals("1")) {
                return statureslist.get(0).getFInfo();
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("CheckGetLabelTempletPortTask Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            if (result.substring(0, 2).equals("EX")) {
                ViseLog.i("CheckGetLabelTempletPortTask result = " + result);
                checkGetLabelTempletPort.OnError(result.substring(2, result.length()));
            } else {
                ViseLog.i("CheckGetLabelTempletPortTask result = " + result);
                try {
                    InputStream Is_Xml = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    checkGetLabelTempletPort.OnResult(CheckTagModeAnalysis.getSingleton().GetTagMode(Is_Xml));
                } catch (Exception e) {
                    ViseLog.i("CheckGetLabelTempletPortTask result Exception = " + result);
                }
            }
        }
    }
}