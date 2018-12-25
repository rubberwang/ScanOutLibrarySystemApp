package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.UnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.R;

public class SettingActivity extends BaseActivity {

    private TextView Back;
    private Button SaveIPAddress_Bt;
    private EditText IP_ET;
    private EditText ET_InputPrinterIP;
    private EditText ET_InputPrinterPort;
    private CheckBox IsScanInput;
    private TextView TV_UnLockAll;
    
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
        tools = new Tools();
        sharedPreferences = tools.InitSharedPreferences(this);
        serverIPSettingObServer = new ServerIPSettingObServer();
        getLifecycle().addObserver(serverIPSettingObServer);//Lifecycle管理Activity生命周期
        DetectionSp();
        CursorMovement();
        SaveIPAddress_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                SaveIPAddresss();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
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
        TV_UnLockAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnlockTask unlockTask = new UnlockTask(WebService.getSingleton(SettingActivity.this));
                unlockTask.execute();
            }
        });
        IsScanInput.setChecked(ConnectStr.ISSHOWNONEXECUTION);
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        SaveIPAddress_Bt = $(R.id.SaveIPAddress_Bt);
        IP_ET = $(R.id.IP_ET);
        ET_InputPrinterIP = $(R.id.ET_InputPrinterIP);
        ET_InputPrinterPort = $(R.id.ET_InputPrinterPort);
        IsScanInput = $(R.id.IsScanInput);
        TV_UnLockAll = $(R.id.TV_UnLockAll);
    }

    private void DetectionSp() {
        if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, "ServerIPAddress"))) {
            IP_ET.setText(tools.GetStringData(sharedPreferences, "ServerIPAddress"));
        }
        if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, "PrintIPAddress"))) {
            ET_InputPrinterIP.setText(tools.GetStringData(sharedPreferences, "PrintIPAddress"));
        }
        if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, "ServerIPPort"))) {
            ET_InputPrinterPort.setText(tools.GetStringData(sharedPreferences, "ServerIPPort"));
        }
    }

    private void SaveIPAddresss() {
        if (!TextUtils.isEmpty(IP_ET.getText().toString()) && !TextUtils.isEmpty(ET_InputPrinterIP.getText().toString())
                && !TextUtils.isEmpty(ET_InputPrinterPort.getText().toString())) {
            tools.PutStringData("PrintIPAddress", ET_InputPrinterIP.getText().toString(), sharedPreferences);
            tools.PutStringData("ServerIPAddress", IP_ET.getText().toString(), sharedPreferences);
            tools.PutStringData("ServerIPPort", ET_InputPrinterPort.getText().toString(), sharedPreferences);
            tools.show(this, "保存成功");
            ViseLog.i("ET_InputPrinterIP = "+ET_InputPrinterIP.getText().toString());
            ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
        } else {
            tools.show(this, "请输入地址后再次点击保存");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();//注释掉这行,back键不退出activity
        ViewManager.getInstance().finishActivity(SettingActivity.this);//直接移除栈
    }

    private void CursorMovement() {
        if (!TextUtils.isEmpty(IP_ET.getText().toString())) {
            IP_ET.setSelection(IP_ET.getText().toString().length());
        }
        if (!TextUtils.isEmpty(ET_InputPrinterIP.getText().toString())) {
            ET_InputPrinterIP.setSelection(ET_InputPrinterIP.getText().toString().length());
        }
    }
}
