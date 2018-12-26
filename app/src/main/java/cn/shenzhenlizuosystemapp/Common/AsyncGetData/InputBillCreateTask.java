package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.InputBillCreate;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputLibraryXmlAnalysis;

public class InputBillCreateTask extends AsyncTask<String, Void, String> {

    private WebService webService = null;
    private String BodyID = "";
    private InputBillCreate inputBillCreate;
    private MyProgressDialog myProgressDialog;
    private String FStockID;
    private String FStockCallID;
    private List<InputSubBodyBean> inputSubBodyBeanList;

    public InputBillCreateTask(String FGuid, String FStockID, String FStockCallID, List<InputSubBodyBean> inputSubBodyBeanList, WebService webService
            , MyProgressDialog myProgressDialog, InputBillCreate inputBillCreate) {
        this.webService = webService;
        this.BodyID = FGuid;
        this.inputBillCreate = inputBillCreate;
        this.myProgressDialog = myProgressDialog;
        this.inputSubBodyBeanList = inputSubBodyBeanList;
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
            String DetailedListXml = InputLibraryXmlAnalysis.getSingleton().CreateInputXmlStr(BodyID, FStockID, FStockCallID, inputSubBodyBeanList);
            ViseLog.i("入库最后上传XML = " + DetailedListXml + "," + BodyID + FStockID);
            String StatuResult = webService.CreatInStockBill(ConnectStr.ConnectionToString, ConnectStr.USERNAME, DetailedListXml);
            ViseLog.i("入库最后Result = " + StatuResult);
            InputStream Is_StatusResult = new ByteArrayInputStream(StatuResult.getBytes("UTF-8"));
            List<AdapterReturn> adapterReturnList = AnalysisReturnsXml.getSingleton().GetReturn(Is_StatusResult);
            if (adapterReturnList.get(0).getFStatus().equals("1")) {
                return adapterReturnList.get(0).getFInfo();
            } else {
                return "EX" + adapterReturnList.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("InputBillCreateTask Exception = " + e);
            return "";
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            myProgressDialog.dismiss();
            ViseLog.i("InputBillCreateTask OnResult" + result);
            inputBillCreate.onResult(result);
        }
    }
}
