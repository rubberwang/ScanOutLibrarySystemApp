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
import cn.shenzhenlizuosystemapp.Common.Port.DirectStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputStockXmlAnalysis;

public class StockCellTask extends AsyncTask<String, Void, String> {

    private DirectStockCellPort directStockCellPort;
    private WebService webService;
    private String StockID;
    private int Mode;

    public StockCellTask(DirectStockCellPort directStockCellPort, WebService webService, String StockID, int Mode) {
        this.directStockCellPort = directStockCellPort;
        this.webService = webService;
        this.StockID = StockID;
        this.Mode = Mode;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetStocksCell(ConnectStr.ConnectionToString, StockID);
            ViseLog.i("Out_StockCellTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
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
                if (Mode == 1) {
                    InputStream IS_XMl = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    directStockCellPort.OnOutRes(InputStockXmlAnalysis.getSingleton().GetXmlStockInfo(IS_XMl));
                } else {
                    InputStream IS_XMl = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    directStockCellPort.OnInRes(InputStockXmlAnalysis.getSingleton().GetXmlStockInfo(IS_XMl));
                }
            } catch (Exception e) {
                ViseLog.i("Out_StockCellTask result Exception = " + result);
            }
        }
    }
}
