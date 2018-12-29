package cn.shenzhenlizuosystemapp.Common.View;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;

import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.R;

public class EditSumDialog {

    private volatile static EditSumDialog editSumDialog;
    private boolean Is_Show = false;
    private Dialog dialog = null;

    public static EditSumDialog getSingleton() {
        if (editSumDialog == null) {
            synchronized (EditSumDialog.class) {
                if (editSumDialog == null) {
                    editSumDialog = new EditSumDialog();
                }
            }
        }
        return editSumDialog;
    }

    public void Show(final Context context, String Code, final EditSumPort editSumPort, View.OnClickListener Cancel, String DefaultSum, String SumUnit) {
        if (editSumPort != null) {
            if (!Is_Show) {
                Is_Show = true;
                View view = LayoutInflater.from(context).inflate(R.layout.save_number_dialog, null, false);
                dialog = new Dialog(context);
                dialog.setContentView(view);
                TextView Tv_TheCurrentMaterialCode = view.findViewById(R.id.Tv_TheCurrentMaterialCode);
                final EditText Ed_Sum = view.findViewById(R.id.Ed_Sum);
                TextView TV_Ensure = view.findViewById(R.id.TV_Ensure);
                TextView TV_Cancel = view.findViewById(R.id.TV_Cancel);
                TextView Tv_SumUnit = view.findViewById(R.id.Tv_SumUnit);
                Tv_TheCurrentMaterialCode.setText(Code);
                Tv_SumUnit.setText(SumUnit);
                Ed_Sum.setText(DefaultSum);
                Ed_Sum.setSelection(DefaultSum.length());
                Handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showKeyboard(Ed_Sum, context);
                    }
                }, 300);
                TV_Ensure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editSumPort.OnEnSure(Ed_Sum.getText().toString());
                        Dismiss();
                    }
                });
                TV_Cancel.setOnClickListener(Cancel);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
                if (screenWidth > 710 && screenHeight > 1100) {
                    lp.width = 640; // 宽度
                    lp.height = 420; // 高度
                } else {
                    lp.width = 440; // 宽度
                    lp.height = 320; // 高度
                }
                dialogWindow.setAttributes(lp);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                ViseLog.i("dialog Is_Show = null");
            }
        } else {
            ViseLog.i("editSumPort = null");

        }
    }

    public void Dismiss() {
        if (dialog != null) {
            Is_Show = false;
            dialog.dismiss();
            dialog = null;
        }
    }

    public void showKeyboard(EditText editText, Context context) {
        //其中editText为dialog中的输入框的 EditText 
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }

    Handler Handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
            super.handleMessage(msg);
        }
    };
}