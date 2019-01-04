package cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.CheckBillCreate;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckLibraryXmlAnalysis;

public class CheckBillCreateTask extends AsyncTask<String, Void, String> {

    private WebService webService = null;
    private String BodyID = "";
    private CheckBillCreate quitBillCreate;
    private MyProgressDialog myProgressDialog;
    private String FStockID;
    private String FStockCallID;
    private List<CheckSubBodyBean> quitSubBodyBeanList;

    public CheckBillCreateTask(String FGuid, String FStockID, String FStockCallID, List<CheckSubBodyBean> quitSubBodyBeanList, WebService webService
            , MyProgressDialog myProgressDialog, CheckBillCreate quitBillCreate) {
        this.webService = webService;
        this.BodyID = FGuid;
        this.quitBillCreate = quitBillCreate;
        this.myProgressDialog = myProgressDialog;
        this.quitSubBodyBeanList = quitSubBodyBeanList;
        this.FStockID = FStockID;
        this.FStockCallID = FStockCallID;
    }

    @Override
    protected void onPreExecute() {
        if (Tools.IsObjectNull(myProgressDialog)) {
            myProgressDialog.ShowPD("正在提交...");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String DetailedListXml = CheckLibraryXmlAnalysis.getSingleton().CreateCheckXmlStr(BodyID, FStockID, FStockCallID, quitSubBodyBeanList);
            ViseLog.i("入库最后上传XML = " + DetailedListXml + "," + BodyID + FStockID);
            String StatuResult = webService.CreateCheckStockBill(ConnectStr.ConnectionToString, ConnectStr.USERNAME, DetailedListXml);
            ViseLog.i("入库最后Result = " + StatuResult);
            InputStream Is_StatusResult = new ByteArrayInputStream(StatuResult.getBytes("UTF-8"));
            List<CheckAdapterReturn> adapterReturnList = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_StatusResult);
            if (adapterReturnList.get(0).getFStatus().equals("1")) {
                return adapterReturnList.get(0).getFInfo();
            } else {
                return "EX" + adapterReturnList.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("QuitBillCreateTask Exception = " + e);
            return "";
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            myProgressDialog.dismiss();
            ViseLog.i("QuitBillCreateTask OnResult" + result);
            quitBillCreate.onResult(result);
        }
    }
}
