package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.QuitBillCreate;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitLibraryXmlAnalysis;

public class QuitBillCreateTask extends AsyncTask<String, Void, String> {

    private WebService webService = null;
    private String BodyID = "";
    private QuitBillCreate quitBillCreate;
    private MyProgressDialog myProgressDialog;
    private String FStockID;
    private String FStockCallID;
    private List<QuitSubBodyBean> quitSubBodyBeanList;

    public QuitBillCreateTask(String FGuid, String FStockID, String FStockCallID, List<QuitSubBodyBean> quitSubBodyBeanList, WebService webService
            , MyProgressDialog myProgressDialog, QuitBillCreate quitBillCreate) {
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
            String DetailedListXml = QuitLibraryXmlAnalysis.getSingleton().CreateQuitXmlStr(BodyID, FStockID, FStockCallID, quitSubBodyBeanList);
            ViseLog.i("入库最后上传XML = " + DetailedListXml + "," + BodyID + FStockID);
            String StatuResult = webService.CreatQuitStockBill(ConnectStr.ConnectionToString, ConnectStr.USERNAME, DetailedListXml);
            ViseLog.i("入库最后Result = " + StatuResult);
            InputStream Is_StatusResult = new ByteArrayInputStream(StatuResult.getBytes("UTF-8"));
            List<QuitAdapterReturn> adapterReturnList = QuitAnalysisReturnsXml.getSingleton().GetReturn(Is_StatusResult);
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
