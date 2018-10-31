package cn.shenzhenlizuosystemapp.Common.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.vise.log.ViseLog;

import static android.content.Context.MODE_PRIVATE;

public class Tools {
    private static Toast toast;
    private static Tools mToastUtils;
    public Tools tools;

    public SharedPreferences InitSharedPreferences(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("FengBoxData", MODE_PRIVATE);
        return sharedPreferences;
    }

    public void PutStringData(String key, String value, SharedPreferences SP) {
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String GetStringData(SharedPreferences SP, String key) {
        String result = SP.getString(key, "");
        return result;
    }

    public void PutIntData(String key, int value, SharedPreferences SP) {
        SharedPreferences.Editor editor = SP.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int GetIntData(SharedPreferences SP, String key) {
        int result = SP.getInt(key, 0);
        return result;
    }

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

  

    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static float pxTodp(Context context, int px) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }

   
    public void PreventQuickClick() {
        long lastClickTime = 0L;
        int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔
        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
            ViseLog.i("点快了");
            return ;
        }
        lastClickTime = System.currentTimeMillis();
    }
}
