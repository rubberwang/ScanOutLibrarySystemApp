package cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.DirectAllotBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.DirectAllotAnalysisReturnsXml;

public  class DirectAllotBarCodeCheckTask extends AsyncTask<String, Void, String> {

    private DirectAllotBarCodeCheckPort barCodeCheckPort;
    private WebService webService;
    private String MaterialID;
    private String LabelTempletID;
    private String Barcodes;

    public DirectAllotBarCodeCheckTask(DirectAllotBarCodeCheckPort barCodeCheckPort, WebService webService, String MaterialID
            , String LabelTempletID, String Barcodes) {
        this.barCodeCheckPort = barCodeCheckPort;
        this.webService = webService;
        this.MaterialID = MaterialID;
        this.LabelTempletID = LabelTempletID;
        this.Barcodes = Barcodes;
    }


    @Override
    protected String doInBackground(String... params)  {
        try {
            ViseLog.i("BarCodeCheckTask parms = " + ConnectStr.ConnectionToString + "," + MaterialID + "," + LabelTempletID + "," + Barcodes + "," + true);
            String StatuResStr = webService.GetBarcodeAnalyze(ConnectStr.ConnectionToString, MaterialID, LabelTempletID, Barcodes, true);
            ViseLog.i("BarCodeCheckTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<DirectAllotAdapterReturn> statureslist = DirectAllotAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            if (statureslist.get(0).getFStatus().equals("1")) {//判断条码解析接口返回状态是否为1
                ViseLog.i("BarCodeCheckTask StatuResStr = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();//获取条码接口传来的信息
            } else {
                return "EX"+statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("BarCodeCheckTask Exception = " + e);//抛出条码解析接口返回异常
            return "" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            barCodeCheckPort.onData(result);
        }
    }
}
