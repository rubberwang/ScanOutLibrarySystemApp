package cn.shenzhenlizuosystemapp.Common.View;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;

import cn.shenzhenlizuosystemapp.Common.Base.SoftKeyBoardListener;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.R;

public class EditSumDialog {

    private volatile static EditSumDialog editSumDialog;
    private boolean Is_Show = false;
    private static Dialog dialog = null;
    private TextView Tv_ErrorHint;
    private EditText Ed_Sum;
    private Activity activity;

    public static EditSumDialog getSingleton(Activity activity) {
        if (editSumDialog == null) {
            synchronized (EditSumDialog.class) {
                if (editSumDialog == null) {
                    editSumDialog = new EditSumDialog(activity);
                }
            }
        }
        return editSumDialog;
    }

    public EditSumDialog(Activity activity) {
        this.activity = activity;
    }
    
    public void Show(final Context context, String Code, final EditSumPort editSumPort, View.OnClickListener Cancel, String DefaultSum, String SumUnit
            , String BatchStr, String ProductNameMode) {
        if (editSumPort != null) {
            if (!Is_Show) {
                Is_Show = true;
                View view = LayoutInflater.from(context).inflate(R.layout.submit_number_dialog, null, false);
                dialog = new Dialog(context);
                dialog.setContentView(view);
                TextView Tv_TheCurrentMaterialCode = view.findViewById(R.id.Tv_TheCurrentMaterialCode);
                Ed_Sum = view.findViewById(R.id.Ed_Sum);
                TextView TV_Ensure = view.findViewById(R.id.TV_Ensure);
                TextView TV_Cancel = view.findViewById(R.id.TV_Cancel);
                TextView Tv_SumUnit = view.findViewById(R.id.Tv_SumUnit);
                TextView Tv_Batch = view.findViewById(R.id.Tv_Batch);
                TextView Tv_ProductNameMode = view.findViewById(R.id.Tv_ProductNameMode);
                if (!TextUtils.isEmpty(BatchStr)){
                    Tv_Batch.setText(BatchStr);
                }else {
                    Tv_Batch.setVisibility(View.GONE);
                }
                Tv_ProductNameMode.setText(ProductNameMode);
                Tv_ErrorHint = view.findViewById(R.id.Tv_ErrorHint);
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
                    }
                });
                TV_Cancel.setOnClickListener(Cancel);
                Window dialogWindow = dialog.getWindow();
                final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
//                Rect r = new Rect();
//                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                if (screenWidth > 710 && screenHeight > 1100) {
                    lp.width = 720; // 宽度
                    lp.height = 600; // 高度
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

    public void ShowErrorInfo(String ErrorInfo) {
        if (Tools.IsObjectNull(Tv_ErrorHint)) {
            Tv_ErrorHint.setText(ErrorInfo);
            Tv_ErrorHint.setVisibility(View.VISIBLE);
        }
        if (Tools.IsObjectNull(Ed_Sum)) {
            Ed_Sum.setText("");
        }
    }

    public void Dismiss() {
        if (dialog != null) {
            Is_Show = false;
            dialog.dismiss();
            dialog = null;
            Ed_Sum = null;
            Tv_ErrorHint = null;
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