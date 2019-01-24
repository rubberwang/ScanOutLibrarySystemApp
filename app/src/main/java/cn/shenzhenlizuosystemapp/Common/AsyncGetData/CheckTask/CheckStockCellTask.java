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
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckStockXmlAnalysis;

public class CheckStockCellTask extends AsyncTask<String, Void, String> {

    private CheckStockCellPort directStockCellPort;
    private WebService webService;
    private String StockID;

    public CheckStockCellTask(CheckStockCellPort directStockCellPort, WebService webService, String StockID) {
        this.directStockCellPort = directStockCellPort;
        this.webService = webService;
        this.StockID = StockID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetStocksCell(ConnectStr.ConnectionToString, StockID);
            ViseLog.i("CheckStockCellTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<CheckAdapterReturn> statureslist = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
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
                InputStream IS_XMl = new ByteArrayInputStream(result.getBytes("UTF-8"));
                directStockCellPort.OnRes(CheckStockXmlAnalysis.getSingleton().GetXmlStockInfo(IS_XMl));
            } catch (Exception e) {
                ViseLog.i("CheckStockCellTask result Exception = " + result);
            }
        }
    }
}
