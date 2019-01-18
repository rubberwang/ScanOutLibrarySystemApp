package cn.shenzhenlizuosystemapp.Common.AsyncGetData.GetMaterialTreeTask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeOnRes;
import cn.shenzhenlizuosystemapp.Common.Xml.InputXml.AnalysisReturnsXml;

public class GetTree extends AsyncTask<String, Void, String> {

    private TreeOnRes treeOnRes;
    private WebService webService;

    public GetTree(TreeOnRes treeOnRes, WebService webService) {
        this.webService = webService;
        this.treeOnRes = treeOnRes;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            String StatuResStr = webService.GetMaterialTree(ConnectStr.ConnectionToString);
            ViseLog.i("GetTree StatuResStr = " + StatuResStr);
            InputStream Is_statu = new ByteArrayInputStream(StatuResStr.getBytes("UTF-8"));
            List<AdapterReturn> statureslist = AnalysisReturnsXml.getSingleton().GetReturn(Is_statu);
            if (statureslist.get(0).getFStatus().equals("1")) {//判断条码解析接口返回状态是否为1
                ViseLog.i("GetTree StatuResStr = " + statureslist.get(0).getFInfo());
                return statureslist.get(0).getFInfo();//获取条码接口传来的信息
            } else {
                return "EX" + statureslist.get(0).getFInfo();
            }
        } catch (Exception e) {
            ViseLog.i("GetTree Exception = " + e);//抛出条码解析接口返回异常
            return "EX" + e;
        }
    }

    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            treeOnRes.OnRes(result);
        }
    }
}