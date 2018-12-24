package cn.shenzhenlizuosystemapp.Common.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;

public class ReceiveData_Recevier extends BroadcastReceiver {

    String DATA_STRING_TAG = "com.symbol.datawedge.data_string";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectStr.BarCodeAction)) {
            String ScanResultStr = intent.getStringExtra(DATA_STRING_TAG);
            EventBus.getDefault().post(new BarCodeMessage(ScanResultStr));
        }
    }
}
