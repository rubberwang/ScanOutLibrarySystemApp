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
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;

public  class AllotDetailTask extends AsyncTask<String, Void, String> {

    private AllotDetailPort allotDetailPort;
    private WebService webService;
    private String BillID;
    private String AllotType;
    
    public AllotDetailTask(AllotDetailPort allotDetailPort, WebService webService, String BillID, String AllotType) {
        this.allotDetailPort = allotDetailPort;
        this.webService = webService;
        this.BillID = BillID;
        this.AllotType = AllotType;
    }


    @Override
    protected String doInBackground(String... params)  {
        try {
            String StatuResStr = webService.GetAdjustStockNoticeBill(ConnectStr.ConnectionToString, BillID, AllotType);
            ViseLog.i("AllotDetailTask StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            if (statureslist.get(0).getFStatus().equals("1")) {
                ViseLog.i("AllotDetailTask getFInfo = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();
            } else {
                return "EX"+statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("AllotDetailTask Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            allotDetailPort.OnResult(result);
        }
    }
}
