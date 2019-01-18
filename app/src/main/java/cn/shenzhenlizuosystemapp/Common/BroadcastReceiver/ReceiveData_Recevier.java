package cn.shenzhenlizuosystemapp.Common.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;

public class ReceiveData_Recevier extends BroadcastReceiver {

    String DATA_STRING_TAG = "com.symbol.datawedge.data_string";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectStr.BarCodeAction)) {//判断是否为扫描action
            String ScanResultStr = intent.getStringExtra(DATA_STRING_TAG);//通过intent传输
            EventBus.getDefault().post(new BarCodeMessage(ScanResultStr));//回调？
        }
    }
}
