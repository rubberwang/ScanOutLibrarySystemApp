package cn.shenzhenlizuosystemapp.Common.UI;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryBill;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStock_Return;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.LoginResInfo;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.RFIDInfo;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.ItemData;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.LoginAdapter;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckBean.CheckAllBean;
import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.GetLoginXml;
import cn.shenzhenlizuosystemapp.Common.Xml.LoginGetResXml;
import cn.shenzhenlizuosystemapp.Common.Xml.RFIDXmlAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class LoginActivity extends BaseActivity {

    private Tools tools;
    private WebService httpRequest;
    private long lastClickTime = 0L;
    private LoginSyncThread loginSyncThread;
    private LogInObServer logInObServer;
    private boolean IsNetWork = false;
    private static final int FAST_CLICK_DELAY_TIME = 500;//快速点击间隔
    private SharedPreferences sharedPreferences;
    private LoginAdapter loginAdapter;

    private AlertDialog alertDialog;
    private EditText Edit_UserName;
    private EditText Edit_PassWord;
    private CheckBox IsUserName_CB;
    private CheckBox IsPassWord_CB;
    private Button LogIn_But;
    private Button Quit_But;
    private ImageView SettingServer_Img;
    private ImageView Img_Setting;
    private TextView Tv_Setting;

    private Spinner SpinnerProjet;
    private Button btnLogin;
    private HashMap<String, String> ProjectNameAndConnectMap;
    private final String TAG = "MainActivity";
    private String SelectProjectStr = "";
    private List<ItemData> itemData;
    private List<RFIDInfo>rfidInfos;
    private MyProgressDialog myProgressDialog;

    private int GetSpinnerPos(String value) {
        for (int i = 0; i < itemData.size(); i++) {
            if (itemData.get(i).getData().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    protected int inflateLayout() {
        return R.layout.login_layout;
    }

    public void initData() {
        tools = new Tools();
        logInObServer = new LogInObServer();
        getLifecycle().addObserver(logInObServer);
        sharedPreferences = tools.InitSharedPreferences(this);
        DetectionHistoryUser();
        DetectionHistoryUserPwd();
        IsPassWord_CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                if (b) {
                    if (TextUtils.isEmpty(Edit_PassWord.getText().toString()) || TextUtils.isEmpty(Edit_UserName.getText().toString())) {
                        //tools.show(LoginActivity.this, "请输入用户名 密码后在勾选保存");
                        //IsPassWord_CB.setChecked(false);
                    } else {
                        IsUserName_CB.setChecked(true);
                    }
                } else {
                    tools.PutStringData("Paw", "", sharedPreferences);
                }
            }
        });
        IsUserName_CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                if (b) {
                    if (TextUtils.isEmpty(Edit_UserName.getText().toString())) {
                        //tools.show(LoginActivity.this, "请输入用户名后在勾选保存");
                        //IsUserName_CB.setChecked(false);
                    } else {

                    }
                } else {
                    if (TextUtils.isEmpty(Edit_UserName.getText().toString())) {

                    }
                    tools.PutStringData("User", "", sharedPreferences);
                }
            }
        });

        LogIn_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsUserName_CB.isChecked()) {
                    if (IsPassWord_CB.isChecked()) {
                        tools.PutStringData("User", Edit_UserName.getText().toString(), sharedPreferences);
                        tools.PutStringData("Paw", Edit_PassWord.getText().toString(), sharedPreferences);
                    } else {
                        tools.PutStringData("User", Edit_UserName.getText().toString(), sharedPreferences);
                        IsUserName_CB.setChecked(true);
                    }
                } else {
                    if (IsPassWord_CB.isChecked()) {
                        tools.PutStringData("User", Edit_UserName.getText().toString(), sharedPreferences);
                        tools.PutStringData("Paw", Edit_PassWord.getText().toString(), sharedPreferences);
                    } else {
                        tools.PutStringData("User", "", sharedPreferences);
                        tools.PutStringData("Paw", "", sharedPreferences);
                    }
                }
                LogInUser();
            }
        });
        Quit_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsExitApp();
            }
        });
        Img_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SettingActivity.class));
            }
        });
        Tv_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SettingActivity.class));
            }
        });
        ProjectNameAndConnectMap = new HashMap();
    }

    private void foucs() {
        if (TextUtils.isEmpty(Edit_UserName.getText().toString())) {
            Edit_UserName.setFocusable(true);
            Edit_UserName.setFocusableInTouchMode(true);
            Edit_UserName.requestFocus();
            ViseLog.i("账户为空");
        } else {
            if (TextUtils.isEmpty(Edit_PassWord.getText().toString())) {
                Edit_PassWord.setFocusable(true);
                Edit_PassWord.setFocusableInTouchMode(true);
                Edit_PassWord.requestFocus();
                ViseLog.i("密码为空");
            } else {
                Edit_PassWord.setSelection(Edit_PassWord.getText().toString().length());
                ViseLog.i("定位到密码edit最后一位" + String.valueOf(Edit_PassWord.getText().toString().length() - 1));
            }
        }
    }


    private void GetProject() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = httpRequest.getProject();
                    InputStream LoginInfo = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    String resultInfo = GetLoginXml.getSingleton().GetInfoTag(LoginInfo);
                    InputStream ProjectsInfo = new ByteArrayInputStream(resultInfo.getBytes("UTF-8"));
                    List<GetProjectResult> projectResult = GetLoginXml.getSingleton().GetAPP_Project(ProjectsInfo);
                    LoginInfo.close();
                    ProjectsInfo.close();
                    for (GetProjectResult getProjectResult : projectResult) {
                        ProjectNameAndConnectMap.put(getProjectResult.getFName(), getProjectResult.getFGuid());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemData = new ArrayList<>();
                            for (int i = 0; i < getSpinnerData().size(); i++) {
                                ItemData itemData1 = new ItemData();
                                itemData1.setData(getSpinnerData().get(i));
                                itemData.add(itemData1);
                            }
                            loginAdapter = new LoginAdapter(itemData, LoginActivity.this);
                            SpinnerProjet.setAdapter(loginAdapter);
                            SpinnerProjet.setSelection(GetSpinnerPos(tools.GetStringData(sharedPreferences, "Project")));
                            SpinnerProjet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    SelectProjectStr = itemData.get(position).getData();
                                    ViseLog.i("选择值" + SelectProjectStr);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });
                        }
                    });
                    Log.i(TAG, result);
                } catch (Exception e) {
                    Log.i("MainActivity", "" + e);
                }
            }
        }).start();
    }

    private List<String> getSpinnerData() {  // 数据源
        List<String> dataList = new ArrayList<>();
        for (String key : ProjectNameAndConnectMap.keySet()) {
            dataList.add(key);
        }
        return dataList;
    }

    private void DetectionHistoryUser() {
        String IS_UserName = tools.GetStringData(sharedPreferences, "User");
        if (TextUtils.isEmpty(IS_UserName)) {
            IsUserName_CB.setChecked(false);
        } else {
            IsUserName_CB.setChecked(true);
            Edit_UserName.setText(IS_UserName);
        }
        foucs();
    }


    private void DetectionHistoryUserPwd() {
        String paw = tools.GetStringData(sharedPreferences, "Paw");
        if (TextUtils.isEmpty(paw)) {
            IsPassWord_CB.setChecked(false);
        } else {
            IsPassWord_CB.setChecked(true);
            String user = tools.GetStringData(sharedPreferences, "User");
            Edit_UserName.setText(user);
            Edit_PassWord.setText(paw);
            ViseLog.i("恢复用户保存的密码");
        }
        foucs();
    }

    public void initView() {
        Edit_UserName = $(R.id.Edit_UserName);
        Edit_PassWord = $(R.id.Edit_PassWord);
        IsUserName_CB = $(R.id.IsUserName_CB);
        IsPassWord_CB = $(R.id.IsPassWord_CB);
        LogIn_But = $(R.id.LogIn_But);
        Quit_But = $(R.id.Quit_But);
        btnLogin = (Button) findViewById(R.id.LogIn_But);
        SpinnerProjet = (Spinner) findViewById(R.id.sp);
        Img_Setting = $(R.id.Img_Setting);
        Tv_Setting = $(R.id.Tv_Setting);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }


    class LogInObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
            httpRequest = new WebService(LoginActivity.this);
            GetProject();
            if (tools.GetStringData(sharedPreferences, "IsScanInput").equals("true")) {//如果设置里面勾选了过滤完成单
                ConnectStr.ISSHOWNONEXECUTION = true;
            } else {
                ConnectStr.ISSHOWNONEXECUTION = false;
            }
            ViseLog.i("ISSHOWNONEXECUTION = " + ConnectStr.ISSHOWNONEXECUTION + "sharedPreferences = " + tools.GetStringData(sharedPreferences, "IsScanInput"));
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {
//            if (Tools.IsObjectNull(itemData) && Tools.IsObjectNull(loginAdapter)) {
//                itemData.clear();
//                loginAdapter.notifyDataSetChanged();
//            }
            ViewManager.getInstance().finishActivity(LoginActivity.this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            ThreadStop();
            if (tools != null) {
                tools = null;
            }
            if (ProjectNameAndConnectMap != null) {
                ProjectNameAndConnectMap = null;
            }
            if (SelectProjectStr != null) {
                SelectProjectStr = null;
            }
            if (itemData != null) {
                itemData = null;
            }
            handler.removeCallbacks(null);
            ViewManager.getInstance().finishActivity(LoginActivity.this);
        }
    }

    private void LogInUser() {
        if (TextUtils.isEmpty(Edit_UserName.getText().toString()) && TextUtils.isEmpty(Edit_PassWord.getText().toString())) {
            tools.show(LoginActivity.this, "请输入用户名 密码");
        } else {
            myProgressDialog.ShowPD("登录中...");
            loginSyncThread = new LoginSyncThread(Edit_UserName.getText().toString(), Edit_PassWord.getText().toString());
            loginSyncThread.start();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!IsNetWork) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.handleMessage(msg);
                    }
                }
            }, 8000);
        }
    }

    private class LoginSyncThread extends Thread {
        String User;
        String Paw;

        LoginSyncThread(String User, String Paw) {
            this.User = User;
            this.Paw = Paw;
        }

        @Override
        public void run() {
            //执行耗时操作
            Message msg = new Message();
            try {
                String string = ProjectNameAndConnectMap.get(SelectProjectStr);
                String Result = httpRequest.LoginIn(string, User, Paw);
                InputStream ResIP = new ByteArrayInputStream(Result.getBytes("UTF-8"));
                List<LoginResInfo> loginResInfoList = LoginGetResXml.getSingleton().GetResInfo(ResIP);
                Log.i("MainActivity", "Res" + Result);
                if (loginResInfoList.get(0).getFStatus().equals("1")) {
                    msg.what = 1;
                    msg.getData().putString("ConnectString", loginResInfoList.get(0).getFInfo());
                    handler.sendMessage(msg);
                } else {
                    msg.what = 2;
                    msg.getData().putString("LoginException", loginResInfoList.get(0).getFInfo());
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ThreadStop();
                msg.what = 2;
                msg.getData().putString("LoginException", e.getMessage());
                handler.sendMessage(msg);
                ViseLog.d("UserModel Exception" + e);
            }
        }

    }

    public void ThreadStop() {
        if (loginSyncThread != null && loginSyncThread.isAlive()) {
            loginSyncThread.interrupt();
            loginSyncThread = null;
        }
    }

    private MyHandler handler = new MyHandler(LoginActivity.this);

    class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<LoginActivity> weakReference;

        public MyHandler(LoginActivity logInActivity) {
            weakReference = new WeakReference<>(logInActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity handlerMemoryActivity = weakReference.get();
            if (handlerMemoryActivity != null) {
                switch (msg.what) {
                    case 1: {
                        myProgressDialog.dismiss();
                        IsNetWork = true;
                        tools.showshort(LoginActivity.this, "登录成功");
                        tools.PutStringData("Project", SelectProjectStr, sharedPreferences);
                        ConnectStr.USERNAME = Edit_UserName.getText().toString();
                        ConnectStr.ConnectionToString = msg.getData().getString("ConnectString");
                        GetRFIDResultAsyncTask getRFIDResultAsyncTask = new GetRFIDResultAsyncTask();
                        getRFIDResultAsyncTask.execute();
                        startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                        break;
                    }
                    case 2: {
                        if(Tools.IsObjectNull(tools)){
                            tools.PutStringData("User", Edit_UserName.getText().toString(), sharedPreferences);
                            tools.PutStringData("Paw", Edit_PassWord.getText().toString(), sharedPreferences);
                            //IsUserName_CB.setChecked(false);
                            //IsPassWord_CB.setChecked(false);
                            myProgressDialog.dismiss();
                            IsNetWork = true;
                            tools.ShowDialog(LoginActivity.this, msg.getData().getString("LoginException"));
                        }
                        break;
                    }
                    case 3: {
                        myProgressDialog.dismiss();
                        tools.ShowDialog(LoginActivity.this, "网络连接超时");
                        break;
                    }
                }
            } else {
                ViseLog.i("没有得到Activity实例不进行操作");
            }
        }
    }

    private void IsExitApp() {
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.exit_app, null, false);
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(view);
        TextView TV_Yes = view.findViewById(R.id.TV_Yes);
        TextView TV_No = view.findViewById(R.id.TV_No);
        TextView Tx_Msg = view.findViewById(R.id.tv_message_toast);
        Tx_Msg.setText("是否退出程序");
        TV_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ViewManager.getInstance().exitApp(LoginActivity.this);
            }
        });
        TV_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
        if (screenWidth > 900 && screenHeight > 1600) {
            lp.width = 900; // 宽度
            lp.height = 500; // 高度
        } else {
            lp.width = 500; // 宽度
            lp.height = 360; // 高度
        }dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public class GetRFIDResultAsyncTask extends AsyncTask<Integer,Integer,List<RFIDInfo>>{

        protected List<RFIDInfo> doInBackground(Integer... params){
            String CheckBills = "";
            try {
                InputStream in_withcode = null;
                    CheckBills = httpRequest.GetRFIDTag(ConnectStr.ConnectionToString);
                ViseLog.i("CheckBills = " + CheckBills);
                in_withcode = new ByteArrayInputStream(CheckBills.getBytes("UTF-8"));
                List<CheckStock_Return> ResultXmlList = RFIDXmlAnalysis.getSingleton().GetRFIDXml(in_withcode);
                in_withcode.close();
                if (ResultXmlList.get(0).getFStatus().equals("1")) {
                    InputStream inputInfoStream = new ByteArrayInputStream(ResultXmlList.get(0).getFInfo().getBytes("UTF-8"));
                    rfidInfos = RFIDXmlAnalysis.getSingleton().GetRFIDInfo(inputInfoStream);
                    inputInfoStream.close();
                } else {
                    rfidInfos.clear();
                }
            } catch (Exception e) {
                ViseLog.d("SelectCheckLibraryGetOutLibraryBillsException " + e);
            }
            return rfidInfos;
        }

        protected void onPostExecute(final List<RFIDInfo> result) {
            try{
                if (Tools.IsObjectNull(result)){
                    ConnectStr.ISRFID = result.get(0).getFValue();
                }
            }catch (Exception e){
                ViseLog.d("返回RFID参数错误" + e);
            }
            ViseLog.i("返回RFID参数" + result);
        }

    }

}