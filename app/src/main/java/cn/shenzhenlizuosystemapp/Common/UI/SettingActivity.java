package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.log.ViseLog;

import java.util.LinkedList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.ClearUnlockTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.UnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.Fragment.MES_Fragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.S_PrivateYunFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.S_PublicYunFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.WMS_Fragment;
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
    private MyProgressDialog myProgressDialog;

    private Tools tools;
    private SharedPreferences sharedPreferences;
    private ServerIPSettingObServer serverIPSettingObServer;
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔

    @Override
    protected int inflateLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void initData() {
        List<TabItemInfo> tabItems = new LinkedList<>();
        tabItems.add(new TabItemInfo(S_PrivateYunFragment.class, R.string.PrivateYun, R.drawable.private_yun));
        tabItems.add(new TabItemInfo(S_PublicYunFragment.class, R.string.PublicYun, R.drawable.public_yun));

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager(), tabItems);
        S_VP_ViewPage.setAdapter(pagerAdapter);
        S_Tab_MainTab.setupWithViewPager(S_VP_ViewPage);
        for (int i = 0; i < S_Tab_MainTab.getTabCount(); i++) {
            TabLayout.Tab tab = S_Tab_MainTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        S_Tab_MainTab = $(R.id.S_Tab_MainTab);
        S_VP_ViewPage = $(R.id.S_VP_ViewPage);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();//注释掉这行,back键不退出activity
        ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
    }

}
