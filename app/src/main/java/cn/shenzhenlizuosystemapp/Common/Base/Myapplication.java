package cn.shenzhenlizuosystemapp.Common.Base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.R;


//import com.squareup.leakcanary.LeakCanary;

public class Myapplication extends Application {

    private static Context BaseApplication;
    public static Myapplication myapplication;
    public static String LinkWayMode = "";
    private Tools tools;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication = getApplicationContext();

        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("JerryForApp")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
        tools = Tools.getTools();
        sharedPreferences = tools.InitSharedPreferences(BaseApplication);
        if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.LINKMODE))) {
            LinkWayMode = tools.GetStringData(sharedPreferences, ConnectStr.LINKMODE);
        }
    }

    public static Context getContext() {
        return BaseApplication;
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        ViseLog.i("onTerminate");
        super.onTerminate();
    }


}
