package cn.shenzhenlizuosystemapp.Common.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.log.ViseLog;

import cn.shenzhenlizuosystemapp.R;

import static android.content.Context.MODE_PRIVATE;

public class Tools {

    private static Toast toast;
    private static Tools mToastUtils;
    public Tools tools;

    public SharedPreferences InitSharedPreferences(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("LiZhuoData", MODE_PRIVATE);
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

    public void show(Context context, String msg) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.toast_layout, null);
        LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.maintoast);
        TextView text = view.findViewById(R.id.tv_message_toast);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) dpToPx(context, 190), (int) dpToPx(context, 60));
        linearlayout.setLayoutParams(layoutParams);
        text.setText(msg);    //toast内容
        if (toast == null) {
            toast = new Toast(context.getApplicationContext());
        }
        toast.setGravity(Gravity.CENTER, 12, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void showshort(Context context, String msg) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.toast_layout, null);
        LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.maintoast);
        TextView text = view.findViewById(R.id.tv_message_toast);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) dpToPx(context, 190), (int) dpToPx(context, 60));
        linearlayout.setLayoutParams(layoutParams);
        text.setText(msg);    //toast内容
        if (toast == null) {
            toast = new Toast(context.getApplicationContext());
        }
        toast.setGravity(Gravity.CENTER, 12, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

}