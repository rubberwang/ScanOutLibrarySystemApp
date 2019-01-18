package cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.DirectAllotPort.GetLabelTempletBaecodesPort;
import cn.shenzhenlizuosystemapp.Common.Xml.InputXml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputXml.InputTagModeAnalysis;

public class GetLabelTempletBaecodesTask extends AsyncTask<String, Void, String> {

    private GetLabelTempletBaecodesPort getLabelTempletBaecodesPort;
    private WebService webService;
    private String LabelTempletID;

    public GetLabelTempletBaecodesTask(GetLabelTempletBaecodesPort getLabelTempletBaecodesPort, WebService webService, String LabelTempletID) {
        this.getLabelTempletBaecodesPort = getLabelTempletBaecodesPort;
        this.webService = webService;
        this.LabelTempletID = LabelTempletID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
            ViseLog.i("GetLabelTempletBaecodesTask StatuResStr = " + ConnectStr.ConnectionToString + "," + LabelTempletID);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            Is_statu.close();
            if (statureslist.get(0).getFStatus().equals("1")) {
                return statureslist.get(0).getFInfo();
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("GetLabelTempletBaecodesTask Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            if (result.substring(0, 2).equals("EX")) {
                ViseLog.i("GetLabelTempletBaecodesTask result = " + result);
                getLabelTempletBaecodesPort.OnError(result.substring(2, result.length()));
            } else {
                ViseLog.i("GetLabelTempletBaecodesTask result = " + result);
                try {
                    InputStream Is_Xml = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    getLabelTempletBaecodesPort.OnResult(InputTagModeAnalysis.getSingleton().GetTagMode(Is_Xml));
                } catch (Exception e) {
                    ViseLog.i("GetLabelTempletBaecodesTask result Exception = " + result);
                }
            }
        }
    }
}