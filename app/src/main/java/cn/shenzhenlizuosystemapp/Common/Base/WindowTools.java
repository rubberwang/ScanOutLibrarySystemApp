package cn.shenzhenlizuosystemapp.Common.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.InputAdapter.ScanTask_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.WindowAdapter.Xg_Rv_WindowAdapter;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.Port.WindowResData;
import cn.shenzhenlizuosystemapp.Common.UI.InputActivity.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class WindowTools {

    public static WindowTools windowTools = null;
    private Context activity;
    private View windowView;
    private WindowManager windowManager;
    private RecyclerView Rv_XG;
    private static List<InputTaskRvData> inputTaskRvDataList = new ArrayList<>();
    private Xg_Rv_WindowAdapter XGAdapter;

    public static WindowTools getWindowTools(Context activity) {
        if (windowTools == null) {
            synchronized (WindowTools.class) {
                if (windowTools == null) {
                    windowTools = new WindowTools(activity);
                }
            }
        }
        return windowTools;
    }

    private WindowTools(Context activity) {
        this.activity = activity;
    }

    public void OpenWindow(List<InputTaskRvData> inputTaskRvDataList, final WindowResData windowRes) {
        try {
            WindowTools.inputTaskRvDataList = inputTaskRvDataList;
            //获取WindowManager实例
            windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            //获取窗口布局参数实例
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            //设置窗口布局参数属性

            DisplayMetrics dm = activity.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
            if (screenWidth > 900 && screenHeight > 1600) {
                params.width = 1000; // 宽度
                params.height = 1500; // 高度
                ViseLog.i("屏幕宽高 > 900 > 1600");
            } else {
                params.width = 720; // 宽度
                params.height = 1100; // 高度
                ViseLog.i("屏幕宽高 < 900 < 1600");
            }
            //设置window的显示特性
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            //设置窗口半透明
            params.format = PixelFormat.TRANSLUCENT;
            //设置窗口类型
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            //获取窗口布局实例
            windowView = View.inflate(activity, R.layout.xg_window_layout, null);
            //获取窗口布局中的按钮实例
            ImageView Iv_Cancle = (ImageView) windowView.findViewById(R.id.Iv_Cancle);
            Rv_XG = (RecyclerView) windowView.findViewById(R.id.RV_XG);
            Iv_Cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    windowRes.Return(GetResData());
                    Cancle();
                }
            });
            InitRecycler(Rv_XG);
            windowView.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                        windowRes.Return(GetResData());
                    Cancle();
                    return false;
                }
            });
            windowManager.addView(windowView, params);
        } catch (Exception e) {
            ViseLog.i("WindowTools OpenWindow Exception = " + e);
        }
    }

    public void InitRecycler(RecyclerView recyclerView) {
        if (Tools.IsObjectNull(recyclerView)) {
            LinearLayoutManager ScanTaskL = new LinearLayoutManager(activity);
            ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
            recyclerView.addItemDecoration(new RvLinearManageDivider(activity, LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(ScanTaskL);
            XGAdapter = new Xg_Rv_WindowAdapter(activity, inputTaskRvDataList);
            recyclerView.setAdapter(XGAdapter);
            XGAdapter.setOnItemClickLitener(new Xg_Rv_WindowAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, final int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }

    public List<InputTaskRvData> GetResData() {
        return XGAdapter.GetData();
    }

    public void Cancle() {
        try {
            windowManager.removeView(windowView);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("WindowTools Cancle Exception = " + e);
        }
    }
}
