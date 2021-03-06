package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.UnlockPort;
import cn.shenzhenlizuosystemapp.Common.Xml.InputXml.AnalysisReturnsXml;

public class ClearUnlockTask extends AsyncTask<String, Void, String> {

    private WebService webService;
    private UnlockPort unlockPort;

    public ClearUnlockTask(WebService webService,UnlockPort unlockPort) {
        this.webService = webService;
        this.unlockPort = unlockPort;
    }

    @Override
    protected void onPreExecute() {
        ViseLog.i("解锁触发");
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String ResStatus = webService.ClearLockedBillBody(ConnectStr.ConnectionToString);
            InputStream Is_Status = new ByteArrayInputStream(ResStatus.getBytes("UTF-8"));
            List<AdapterReturn> adapterReturnList = AnalysisReturnsXml.getSingleton().GetReturn(Is_Status);
            if (adapterReturnList.get(0).getFStatus().equals("1")) {
                return "success";
            } else {
                return adapterReturnList.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("InputBodyLockTask Exception = " + e);
            return "";
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            ViseLog.i("解锁Info" + result);
            unlockPort.onResult(result);
        }
    }
}
