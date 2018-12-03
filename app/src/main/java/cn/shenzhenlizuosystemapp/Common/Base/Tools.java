package cn.shenzhenlizuosystemapp.Common.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.log.ViseLog;

import cn.shenzhenlizuosystemapp.R;

import static android.content.Context.MODE_PRIVATE;

public class Tools {

    private static Toast toast;
    private static Tools mtools;
    private static Dialog dialog = null;
    private boolean IsShowDialog = false;

    public static Tools getTools() {
        if (mtools == null) {
            synchronized (Tools.class) {
                if (mtools == null) {
                    mtools = new Tools();
                }
            }
        }
        return mtools;
    }

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
        View view = layoutInflater.inflate(R.layout.warning_layout, null);
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
        View view = layoutInflater.inflate(R.layout.warning_layout, null);
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

    public void ToastCancel() {
        toast.cancel();
    }

    public void ShowDialog(Context context, String msg) {
        if (!IsShowDialog){
            View view = LayoutInflater.from(context).inflate(R.layout.quitwarning_layout, null, false);
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(view);

            TextView Tx_Ensure = view.findViewById(R.id.Tx_Ensure);
            TextView Tx_Msg = view.findViewById(R.id.tv_message_toast);
            Tx_Msg.setText(msg);
            Tx_Ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IsShowDialog = false;
                    dialog.dismiss();
                }
            });
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
            if (screenWidth > 900 && screenHeight > 1600) {
                lp.width = 900; // 宽度
                lp.height = 600; // 高度
            } else {
                lp.width = 520; // 宽度
                lp.height = 400; // 高度
            }
            dialogWindow.setAttributes(lp);
            dialog.setCancelable(false);
            dialog.show();
            IsShowDialog = true;
        }
    }

    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public void ShowOnClickDialog(Context context, String msg, View.OnClickListener OnEnsure, View.OnClickListener OnClose, boolean IsCancel) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialoglayout_cancel, null, false);
        dialog = new Dialog(context);
        dialog.setContentView(view);
        TextView Tv_Ensure = view.findViewById(R.id.Tv_Ensure);
        TextView Tv_Close = view.findViewById(R.id.Tv_Close);
        TextView Tv_Msg = view.findViewById(R.id.TvMessageDialog);
        Tv_Msg.setText(msg);
        Tv_Ensure.setOnClickListener(OnEnsure);
        if (IsCancel) {
            Tv_Close.setVisibility(View.GONE);
        }
        Tv_Close.setOnClickListener(OnClose);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
        if (screenWidth > 900 && screenHeight > 1600) {
            lp.width = 900; // 宽度
            lp.height = 600; // 高度
        } else {
            lp.width = 520; // 宽度
            lp.height = 400; // 高度
        }
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void DisappearDialog() {
        dialog.dismiss();
    }
}