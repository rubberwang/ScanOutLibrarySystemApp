package cn.shenzhenlizuosystemapp.Common.View;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.ScanResult_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.R;

public class ShowResultListDialog {

    private volatile static ShowResultListDialog editSumDialog;
    private boolean Is_Show = false;
    private Dialog dialog = null;
    private TextView Tv_ErrorHint;
    private EditText Ed_Sum;

    public static ShowResultListDialog getSingleton() {
        if (editSumDialog == null) {
            synchronized (ShowResultListDialog.class) {
                if (editSumDialog == null) {
                    editSumDialog = new ShowResultListDialog();
                }
            }
        }
        return editSumDialog;
    }

    public void Show(final Context context, String Code, final EditSumPort editSumPort, View.OnClickListener Cancel, String DefaultSum, String SumUnit
            , List<ChildCheckTag> childCheckTagList) {
        if (editSumPort != null) {
            if (!Is_Show) {
                Is_Show = true;
                View view = LayoutInflater.from(context).inflate(R.layout.save_number_dialog, null, false);
                dialog = new Dialog(context);
                dialog.setContentView(view);
                TextView Tv_TheCurrentMaterialCode = view.findViewById(R.id.Tv_TheCurrentMaterialCode);
                RecyclerView RV_GetInfoTable1 = view.findViewById(R.id.RV_GetInfoTable1);
                InitRecycler(childCheckTagList, RV_GetInfoTable1, context);
                Ed_Sum = view.findViewById(R.id.Ed_Sum);
                TextView TV_Ensure = view.findViewById(R.id.TV_Ensure);
                TextView TV_Cancel = view.findViewById(R.id.TV_Cancel);
                TextView Tv_SumUnit = view.findViewById(R.id.Tv_SumUnit);
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
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
                if (screenWidth > 710 && screenHeight > 1100) {
                    lp.width = 680; // 宽度
                    lp.height = 700; // 高度
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

    private void InitRecycler(List<ChildCheckTag> childCheckTagList, RecyclerView recyclerView, Context context) {
        if (Tools.IsObjectNull(childCheckTagList) && Tools.IsObjectNull(recyclerView)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(layoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            ScanResult_CheckRvAdapter scanResult_CheckRvAdapter = new ScanResult_CheckRvAdapter(context, childCheckTagList);
            recyclerView.setAdapter(scanResult_CheckRvAdapter);
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