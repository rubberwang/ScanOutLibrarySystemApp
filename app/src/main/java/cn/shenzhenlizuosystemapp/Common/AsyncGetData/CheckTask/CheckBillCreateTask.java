package cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
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

    private CheckBillCreate checkBillCreate;
    private MyProgressDialog myProgressDialog;
    private List<CheckLibraryDetail> checkHeadList;
    private List<CheckTaskRvData> checkBodyList;
    private List<CheckSubBody> checkSubBodyBeanList;

    public CheckBillCreateTask(List<CheckLibraryDetail> checkHeadList, List<CheckTaskRvData> checkBodyList, List<CheckSubBody> checkSubBodyBeanList, WebService webService
            , MyProgressDialog myProgressDialog, CheckBillCreate checkBillCreate) {
        this.webService = webService;
        this.checkHeadList = checkHeadList;
        this.checkBodyList = checkBodyList;
        this.checkBillCreate = checkBillCreate;
        this.myProgressDialog = myProgressDialog;
        this.checkSubBodyBeanList = checkSubBodyBeanList;

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
            String DetailedListXml = CheckLibraryXmlAnalysis.getSingleton().CreateCheckXmlStr(checkHeadList,checkBodyList,checkSubBodyBeanList);
            ViseLog.i("盘点最后上传XML = " + DetailedListXml);
            String StatuResult = webService.CreateCheckStockBill(ConnectStr.ConnectionToString, ConnectStr.USERNAME, DetailedListXml);
            ViseLog.i("盘点最后Result = " + StatuResult);
            InputStream Is_StatusResult = new ByteArrayInputStream(StatuResult.getBytes("UTF-8"));
            List<CheckAdapterReturn> adapterReturnList = CheckAnalysisReturnsXml.getSingleton().GetReturn(Is_StatusResult);
            if (adapterReturnList.get(0).getFStatus().equals("1")) {
                return adapterReturnList.get(0).getFInfo();
            } else {
                return "EX" + adapterReturnList.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("CheckBillCreateTask Exception = " + e);
            return "";
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            myProgressDialog.dismiss();
            ViseLog.i("CheckBillCreateTask OnResult" + result);
            checkBillCreate.onResult(result);
        }
    }
}
