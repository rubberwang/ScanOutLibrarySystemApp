package cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CreateAdjustStockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.Port.CreatAdjustStockBillPort;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.DirectAllotLibraryXmlAnalysis;

public class CreatAdjustStockBill extends AsyncTask<String, Void, String> {

    private CreatAdjustStockBillPort creatAdjustStockBillPort;
    private WebService webService;
    private String BillID;
    private String AllotType;
    private String InStockId;
    private String OutStockId;
    private String InStockCellId;
    private String OutStockCellId;
    private List<CreateAdjustStockBean> createAdjustStockBeanList;

    public CreatAdjustStockBill(CreatAdjustStockBillPort creatAdjustStockBillPort, WebService webService, String BillID, String AllotType, String InStockId, String OutStockId,
                                String InStockCellId, String OutStockCellId, List<CreateAdjustStockBean> createAdjustStockBeanList) {
        this.creatAdjustStockBillPort = creatAdjustStockBillPort;
        this.webService = webService;
        this.BillID = BillID;
        this.AllotType = AllotType;
        this.InStockId = InStockId;
        this.OutStockId = OutStockId;
        this.InStockCellId = InStockCellId;
        this.OutStockCellId = OutStockCellId;
        this.createAdjustStockBeanList = createAdjustStockBeanList;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String CreateBillXml = DirectAllotLibraryXmlAnalysis.CreateAdjustStockBillXmlStr(BillID, AllotType, OutStockId, OutStockCellId, InStockId, InStockCellId, createAdjustStockBeanList);
            String StatuResStr = webService.CreatAdjustStockBill(ConnectStr.ConnectionToString, ConnectStr.USERNAME, CreateBillXml);
            ViseLog.i("CreatAdjustStockBill StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            if (statureslist.get(0).getFStatus().equals("1")) {
                ViseLog.i("CreatAdjustStockBill getFInfo = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("CreatAdjustStockBill Exception = " + e);
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            ViseLog.i("CreatAdjustStockBill Result = " + result);
            creatAdjustStockBillPort.OnResult(result);
        }
    }
}

