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
import cn.shenzhenlizuosystemapp.Common.Port.CheckBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;

public class CheckBarCodeCheckTask extends AsyncTask<String, Void, String> {

    private CheckBarCodeCheckPort barCodeCheckPort;
    private WebService webService;
    private String MaterialID;
    private String LabelTempletID;
    private String Barcodes;

    public CheckBarCodeCheckTask(CheckBarCodeCheckPort barCodeCheckPort, WebService webService, String MaterialID
            , String LabelTempletID, String Barcodes) {
        this.barCodeCheckPort = barCodeCheckPort;
        this.webService = webService;
        this.MaterialID = MaterialID;
        this.LabelTempletID = LabelTempletID;
        this.Barcodes = Barcodes;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            ViseLog.i("BarCodeCheckTask parms = " + ConnectStr.ConnectionToString + "," + MaterialID + "," + LabelTempletID + "," + Barcodes + "," + true);
            String StatuResStr = webService.GetBarcodeAnalyze(ConnectStr.ConnectionToString,MaterialID, LabelTempletID, Barcodes, false);
            ViseLog.i("QuitBarCodeCheckTask QuitStatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<CheckAdapterReturn> statureslist = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            if (statureslist.get(0).getFStatus().equals("1")) {
                ViseLog.i("QuitBarCodeCheckTask QuitStatuResStr = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();
            } else {
                return "EX"+statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("QuitBarCodeCheckTask Exception =  " + e);
            return "" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            barCodeCheckPort.onData(result);
        }
    }
}
