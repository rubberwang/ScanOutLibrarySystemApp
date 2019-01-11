package cn.shenzhenlizuosystemapp.Common.AsyncGetData.GetMaterialTreeTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.M_OnRes;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;

public class GetM extends AsyncTask<String, Void, String> {

    private M_OnRes m_onRes;
    private WebService webService;
    private String MID;

    public GetM(M_OnRes m_onRes, WebService webService, String MID) {
        this.webService = webService;
        this.m_onRes = m_onRes;
        this.MID = MID;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetMaterial(ConnectStr.ConnectionToString, MID);
            ViseLog.i("GetM StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            Is_statu.close();
            if (statureslist.get(0).getFStatus().equals("1")) {//判断条码解析接口返回状态是否为1
                ViseLog.i("GetM Info = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();//获取条码接口传来的信息
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("GetM Exception = " + e);//抛出条码解析接口返回异常
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            m_onRes.OnRes(result);
        }
    }
}