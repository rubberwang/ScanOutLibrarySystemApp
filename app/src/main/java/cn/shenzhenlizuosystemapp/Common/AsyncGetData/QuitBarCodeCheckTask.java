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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildQuitTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitStockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.QuitBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.QuitTagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.QuitStockAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.NewQuitLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitTagModeAnalysis;

public class QuitBarCodeCheckTask extends AsyncTask<String, Void, String> {

    private QuitBarCodeCheckPort barCodeCheckPort;
    private WebService webService;
    private String MaterialID;
    private String LabelTempletID;
    private String Barcodes;

    public QuitBarCodeCheckTask(QuitBarCodeCheckPort barCodeCheckPort, WebService webService, String MaterialID
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
            List<QuitAdapterReturn> statureslist = QuitAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
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
