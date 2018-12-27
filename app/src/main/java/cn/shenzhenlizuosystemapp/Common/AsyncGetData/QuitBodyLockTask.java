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
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitAnalysisReturnsXml;

public class QuitBodyLockTask extends AsyncTask<String, Void, String> {

    private WebService webService = null;
    private String BodyID = "";
    private LockResultPort lockResultPort;
    private MyProgressDialog myProgressDialog;

    public QuitBodyLockTask(LockResultPort lockResultPort, WebService webService, String BodyID, MyProgressDialog myProgressDialog) {
        this.webService = webService;
        this.BodyID = BodyID;
        this.lockResultPort = lockResultPort;
        this.myProgressDialog = myProgressDialog;
    }

    @Override
    protected void onPreExecute() {
        if (Tools.IsObjectNull(myProgressDialog)) {
            myProgressDialog.ShowPD("加载数据中...");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResult = webService.LockedBillBody(ConnectStr.ConnectionToString, BodyID);
            InputStream Is_StatusResult = new ByteArrayInputStream(StatuResult.getBytes("UTF-8"));
            List<QuitAdapterReturn> adapterReturnList = QuitAnalysisReturnsXml.getSingleton().GetReturn(Is_StatusResult);
            if (adapterReturnList.get(0).getFStatus().equals("1")) {
                return "Success";
            } else {
                return adapterReturnList.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("QuitBodyLockTask Exception = " + e);
            return "";
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            ViseLog.i("锁定" + result);
            lockResultPort.onStatusResult(result);
        }
    }
}
