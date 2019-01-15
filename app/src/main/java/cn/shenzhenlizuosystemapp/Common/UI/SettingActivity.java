package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
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
import cn.shenzhenlizuosystemapp.Common.Fragment.Select_InputLibrary_Fragment;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.UnlockPort;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.MainPagerAdapter;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.TabItemInfo;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.NoScrollViewPager;
import cn.shenzhenlizuosystemapp.R;

public class SettingActivity extends BaseActivity {

    private TextView Back;
    private TextView TV_UnLockAll;
    private TextView TV_LinkMode;
    private Button SaveIPAddress_Bt;
    private MyProgressDialog myProgressDialog;
    private LinearLayout Ly_Private;
    private LinearLayout Ly_Public;
    private Fragment F_Common;

    private Tools tools;
    private SharedPreferences sharedPreferences;
    private ServerIPSettingObServer serverIPSettingObServer;
    private S_PrivateYunFragment s_privateYunFragment;
    private S_PublicYunFragment s_publicYunFragment;

    private int TabIndex = -1;
    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    @Override
    protected int inflateLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void initData() {
        tools = Tools.getTools();
        if (Myapplication.LinkWayMode.equals("Private")) {
            TabIndex = 0;
            InitFragment(1);
            TV_LinkMode.setVisibility(View.VISIBLE);
            TV_LinkMode.setText("当前连接模式为:私有云");
        } else if (Myapplication.LinkWayMode.equals("Public")) {
            TabIndex = 1;
            InitFragment(0);
            TV_LinkMode.setVisibility(View.VISIBLE);
            TV_LinkMode.setText("当前连接模式为:公有云");
        }
        InitClick();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        TV_UnLockAll = $(R.id.TV_UnLockAll);
        TV_LinkMode = $(R.id.TV_LinkMode);
        SaveIPAddress_Bt = $(R.id.SaveIPAddress_Bt);
        Ly_Private = $(R.id.Ly_Private);
        Ly_Public = $(R.id.Ly_Public);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }

    private void InitFragment(int is) {
        Drawable PitchOnDrawable = getResources().getDrawable(R.drawable.btn_bg_thin_bule);
        Drawable CloseDrawable = getResources().getDrawable(R.drawable.toast);
        if (is == 1) {
            Ly_Public.setBackground(CloseDrawable);
            Ly_Private.setBackground(PitchOnDrawable);
            s_privateYunFragment = S_PrivateYunFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.F_Common, s_privateYunFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
        } else {
            Ly_Private.setBackground(CloseDrawable);
            Ly_Public.setBackground(PitchOnDrawable);
            s_publicYunFragment = S_PublicYunFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.F_Common, s_publicYunFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
        }
    }

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
        Ly_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TabIndex == 0){
                    InitFragment(1);
                }
            }
        });

        Ly_Public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TabIndex != 0){
                    InitFragment(0);
                }
            }
        });

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
