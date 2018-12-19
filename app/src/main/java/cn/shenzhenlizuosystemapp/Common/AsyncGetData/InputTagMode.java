package cn.shenzhenlizuosystemapp.Common.AsyncGetData;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.InputTagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputStockAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputTagModeAnalysis;

public class InputTagMode extends AsyncTask<String, Void, List<ChildTag>> {

    private InputTagModePort inputTagModePort;
    private WebService webService;
    private String LabelTempletID;

    public InputTagMode(String LabelTempletID, WebService webService, InputTagModePort inputTagModePort) {
        this.inputTagModePort = inputTagModePort;
        this.webService = webService;
        this.LabelTempletID = LabelTempletID;
    }

    @Override
    protected List<ChildTag> doInBackground(String[] par) {
        List<ChildTag> childTagList = new ArrayList<ChildTag>();
        try {
            String ResStatus = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
            InputStream is_res = new ByteArrayInputStream(ResStatus.getBytes("UTF-8"));
            List<AdapterReturn> list_return = AnalysisReturnsXml.getSingleton().GetReturn(is_res);
            is_res.close();
            if (list_return.get(0).getFStatus().equals("1")) {
                ViseLog.i("InputTagMode Info = " + list_return.get(0).getFInfo());
                InputStream is_info = new ByteArrayInputStream(list_return.get(0).getFInfo().getBytes("UTF-8"));
                childTagList = InputTagModeAnalysis.getSingleton().GetTagMode(is_info);
                is_info.close();
                return childTagList;
            } else {
                return childTagList;
            }
        } catch (Exception e) {
            ViseLog.i("InputTagMode Exception =  " + e);
        }
        return null;
    }

    protected void onPostExecute(List<ChildTag> result) {
        if (result.size() >= 0) {
            inputTagModePort.MainResult(result);
        }
    }
}
