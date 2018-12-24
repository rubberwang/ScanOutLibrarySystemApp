package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildQuitTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.QuitTagModePort;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitTagModeAnalysis;

public class QuitTagMode extends AsyncTask<String, Void, List<ChildQuitTag>> {

    private QuitTagModePort quitTagModePort;
    private WebService webService;
    private String LabelTempletID;

    public QuitTagMode(String LabelTempletID, WebService webService, QuitTagModePort quitTagModePort) {
        this.quitTagModePort = quitTagModePort;
        this.webService = webService;
        this.LabelTempletID = LabelTempletID;
    }

    @Override
    protected List<ChildQuitTag> doInBackground(String[] par) {
        List<ChildQuitTag> childQuitTagList = new ArrayList<ChildQuitTag>();
        try {
            String ResStatus = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
            InputStream is_res = new ByteArrayInputStream(ResStatus.getBytes("UTF-8"));
            List<AdapterReturn> list_return = AnalysisReturnsXml.getSingleton().GetReturn(is_res);
            is_res.close();
            if (list_return.get(0).getFStatus().equals("1")) {
                ViseLog.i("QuitTagMode Info = " + list_return.get(0).getFInfo());
                InputStream is_info = new ByteArrayInputStream(list_return.get(0).getFInfo().getBytes("UTF-8"));
                childQuitTagList = QuitTagModeAnalysis.getSingleton().GetTagMode(is_info);
                is_info.close();
                return childQuitTagList;
            } else {
                return childQuitTagList;
            }
        } catch (Exception e) {
            ViseLog.i("QuitTagMode Exception =  " + e);
        }
        return null;
    }

    protected void onPostExecute(List<ChildQuitTag> result) {
        if (result.size() >= 0) {
            quitTagModePort.MainResult(result);
        }
    }
}
