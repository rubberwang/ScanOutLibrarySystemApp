package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.InputTagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputStockAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputTagModeAnalysis;

public  class BarCodeCheckTask extends AsyncTask<String, Void, String> {

    private BarCodeCheckPort barCodeCheckPort;
    private WebService webService;
    private String MaterialID;
    private String LabelTempletID;
    private String Barcodes;
    
    public BarCodeCheckTask(BarCodeCheckPort barCodeCheckPort, WebService webService, String MaterialID
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
//            ViseLog.i("BarCodeCheckTask parms = " + ConnectStr.ConnectionToString + "," + MaterialID + "," + LabelTempletID + "," + Barcodes + "," + true);
            String StatuResStr = webService.GetBarcodeAnalyze(ConnectStr.ConnectionToString, MaterialID, LabelTempletID, Barcodes, true);
            ViseLog.i("BarCodeCheckTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
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
