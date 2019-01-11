package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.util.LinkedList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.ClearUnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.Fragment.S_PrivateYunFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.S_PublicYunFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.Select_CheckLibrary_Fragment;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.UnlockPort;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.MainPagerAdapter;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.TabItemInfo;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.R;

public class SettingActivity extends BaseActivity {

    private TextView Back;
    private TabLayout S_Tab_MainTab;
    private ViewPager S_VP_ViewPage;
    private TextView TV_UnLockAll;
    private TextView TV_LinkMode;
    private Button SaveIPAddress_Bt;
    private MyProgressDialog myProgressDialog;

    private Tools tools;
    private SharedPreferences sharedPreferences;
    private ServerIPSettingObServer serverIPSettingObServer;
    private S_PrivateYunFragment s_privateYunFragment;
    private S_PublicYunFragment s_publicYunFragment;
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔
    private int TabIndex = -1;


    @Override
    protected int inflateLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void initData() {
        tools = Tools.getTools();
        List<TabItemInfo> tabItems = new LinkedList<>();
        s_privateYunFragment = new S_PrivateYunFragment().newInstance();
        s_publicYunFragment = new S_PublicYunFragment().newInstance();
        tabItems.add(new TabItemInfo(s_privateYunFragment.getClass(), R.string.PrivateYun, R.drawable.private_yun));
        tabItems.add(new TabItemInfo(s_publicYunFragment.getClass(), R.string.PublicYun, R.drawable.public_yun));

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager(), tabItems);
        S_VP_ViewPage.setAdapter(pagerAdapter);
        S_Tab_MainTab.setupWithViewPager(S_VP_ViewPage);
        for (int i = 0; i < S_Tab_MainTab.getTabCount(); i++) {
            TabLayout.Tab tab = S_Tab_MainTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(mTabOnClickListener);
            }
        }
        if (Myapplication.LinkWayMode.equals("Private")) {
            S_Tab_MainTab.getTabAt(0).select();
            TabIndex = 0;
            TV_LinkMode.setVisibility(View.VISIBLE);
            TV_LinkMode.setText("当前连接模式为:私有云");
        } else if (Myapplication.LinkWayMode.equals("Public")) {
            S_Tab_MainTab.getTabAt(1).select();
            TabIndex = 1;
            TV_LinkMode.setVisibility(View.VISIBLE);
            TV_LinkMode.setText("当前连接模式为:公有云");
        }
        InitClick();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        S_Tab_MainTab = $(R.id.S_Tab_MainTab);
        S_VP_ViewPage = $(R.id.S_VP_ViewPage);
        TV_UnLockAll = $(R.id.TV_UnLockAll);
        TV_LinkMode = $(R.id.TV_LinkMode);
        SaveIPAddress_Bt = $(R.id.SaveIPAddress_Bt);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TabIndex = position;
        }
    };

    class ServerIPSettingObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            ViewManager.getInstance().finishActivity(SettingActivity.this);
        }
    }

    private void InitClick() {
        TV_UnLockAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProgressDialog.ShowPD("正在清除...");
                UnlockPort unlockPort = new UnlockPort() {
                    @Override
                    public void onResult(String res) {
                        if (res.equals("success")) {
                            tools.ShowDialog(SettingActivity.this, "清除成功");
                            myProgressDialog.dismiss();
                        } else {
                            tools.ShowDialog(SettingActivity.this, "清除失败:" + res);
                            myProgressDialog.dismiss();
                        }
                    }
                };
                ClearUnlockTask clearUnlockTask = new ClearUnlockTask(WebService.getSingleton(SettingActivity.this), unlockPort);
                clearUnlockTask.execute();
            }
        });
        SaveIPAddress_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TabIndex == 0) {
                    s_privateYunFragment.Private();
                    ViewManager.getInstance().finishActivity(SettingActivity.this);
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                } else if (TabIndex == 1) {
                    s_publicYunFragment.Public();
                    ViewManager.getInstance().finishActivity(SettingActivity.this);
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();//注释掉这行,back键不退出activity
        ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
    }
}
