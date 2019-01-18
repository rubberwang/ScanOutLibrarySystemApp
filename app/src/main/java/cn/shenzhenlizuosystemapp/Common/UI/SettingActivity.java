package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.Fragment.SettingFragment.S_PrivateYunFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.SettingFragment.S_PublicYunFragment;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.R;

public class SettingActivity extends BaseActivity {

    private TextView Back;
    private Button SaveIPAddress_Bt;
    private MyProgressDialog myProgressDialog;
    private LinearLayout Ly_Private;
    private LinearLayout Ly_Public;
    private Fragment F_Common;
    private CheckBox CB_Public;
    private CheckBox CB_Private;
    private CheckBox IsScanInput;
    private TextView TV_LinkMode;
    private TextView TV_SN;

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
        sharedPreferences = tools.InitSharedPreferences(this);
        if (Myapplication.LinkWayMode.equals("Private")) {
            InitFragment(1);
        } else if (Myapplication.LinkWayMode.equals("Public")) {
            InitFragment(0);
        } else {
            InitFragment(0);
        }
        if (tools.GetStringData(sharedPreferences, "IsScanInput").equals("true")) {
            IsScanInput.setChecked(true);
        }
        if (!TextUtils.isEmpty(tools.getSerialNumber())) {
            TV_SN.setText("设备SN号 ： " + tools.getSerialNumber());
        } else {
            TV_SN.setText("未能获取到SN号");
        }
        InitClick();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        SaveIPAddress_Bt = $(R.id.SaveIPAddress_Bt);
        Ly_Private = $(R.id.Ly_Private);
        Ly_Public = $(R.id.Ly_Public);
        CB_Public = $(R.id.CB_Public);
        CB_Private = $(R.id.CB_Private);
        TV_LinkMode = $(R.id.TV_LinkMode);
        IsScanInput = $(R.id.IsScanInput);
        TV_SN = $(R.id.TV_SN);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }

    private void InitFragment(int is) {
        Drawable PitchOnDrawable = getResources().getDrawable(R.drawable.btn_bg_thin_bule);
        Drawable CloseDrawable = getResources().getDrawable(R.drawable.toast);
        if (is == 1) {
            TabIndex = 0;
            TV_LinkMode.setText("当前接入方式为：私有云");
            CB_Private.setChecked(true);
            Ly_Public.setBackground(CloseDrawable);
            Ly_Private.setBackground(PitchOnDrawable);
            s_privateYunFragment = S_PrivateYunFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.F_Common, s_privateYunFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
        } else {
            TabIndex = 1;
            TV_LinkMode.setText("当前接入方式为：公有云");
            CB_Public.setChecked(true);
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
        CB_Public.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CB_Private.isChecked()) {
                        CB_Private.setChecked(false);
                    }
                    InitFragment(0);
                }
            }
        });
        CB_Private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (CB_Public.isChecked()) {
                        CB_Public.setChecked(false);
                    }
                    InitFragment(1);
                }
            }
        });
        IsScanInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tools.PutStringData("IsScanInput", "true", sharedPreferences);
                } else {
                    tools.PutStringData("IsScanInput", "false", sharedPreferences);
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
            }
        });
        Ly_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitFragment(1);
            }
        });

        Ly_Public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitFragment(0);
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
