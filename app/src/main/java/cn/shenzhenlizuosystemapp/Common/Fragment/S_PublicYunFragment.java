package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.PerpetualWebService;
import cn.shenzhenlizuosystemapp.Common.Port.YunPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.SettingPublicYunAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.LoginActivity;
import cn.shenzhenlizuosystemapp.Common.WebBean.Setting_PublicDataBean;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.Setting_F_FragmentXml;
import cn.shenzhenlizuosystemapp.R;

public class S_PublicYunFragment extends Fragment implements YunPort {

    public static final String ARGS_PAGE = "S_PublicYunFragment";
    private static SharedPreferences sharedPreferences;
    private static Tools tools;
    private static PerpetualWebService perpetualWebService;

    private static EditText ET_U;
    private static EditText ET_P;
    private static Spinner Sp_SelectService;
    private static List<Setting_PublicDataBean> setting_publicDataBeanList;

    private static int Sp_SelectServiceIndex = -1;

    private int GetSpinnerPos(List<Setting_PublicDataBean> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFGuid().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    public static S_PublicYunFragment newInstance() {
        S_PublicYunFragment fragment = new S_PublicYunFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_public_yun_setting, container, false);
        ET_U = rootView.findViewById(R.id.ET_U);
        ET_P = rootView.findViewById(R.id.ET_P);
        Sp_SelectService = rootView.findViewById(R.id.Sp_SelectService);
        tools = Tools.getTools();
        sharedPreferences = tools.InitSharedPreferences(getContext());
        perpetualWebService = PerpetualWebService.getSingleton(getContext());
        CheckIsSaveData();
        InitEvent();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        CursorLocation();
    }
    
    private void CursorLocation() {
        if (!TextUtils.isEmpty(ET_U.getText().toString())) {
            ET_P.setFocusable(true);
            ET_P.setFocusableInTouchMode(true);
            ET_P.requestFocus();
            if (!TextUtils.isEmpty(ET_P.getText())) {
                ET_P.setSelection(ET_P.getText().toString().length());
                ViseLog.i("ET_P.gettext.length = " + ET_P.getText().toString().length());
            }
        } else {
            ET_U.setFocusable(true);
            ET_U.setFocusableInTouchMode(true);
            ET_U.requestFocus();
            ViseLog.i("ET_U.gettext.length = " + ET_U.getText().toString().length());
        }
    }


    private void CheckIsSaveData() {
        if (Tools.IsObjectNull(sharedPreferences)) {
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_User))) {
                ET_U.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_User));
            }
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_PassWord))) {
                ET_P.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_PassWord));
            }
            CheckUserPW();
        } else {
            ViseLog.i(ARGS_PAGE + "sharedPreferences = null");
        }
    }


    private void InitEvent() {
        ET_U.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(ET_P.getText().toString())) {
                    ET_P.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ET_P.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟600ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
        });

    }

    private Handler handler = new Handler();
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            CheckUserPW();
        }
    };

    private void CheckUserPW() {
        if (!TextUtils.isEmpty(ET_U.getText().toString()) && !TextUtils.isEmpty(ET_P.getText().toString())) {
            GetSTask getSThread = new GetSTask(ET_U.getText().toString(), ET_P.getText().toString());
            getSThread.execute();
        }
    }

    private class GetSTask extends AsyncTask<String, Void, String> {

        private String User;
        private String PassWord;

        public GetSTask(String User, String PassWord) {
            this.User = User;
            this.PassWord = PassWord;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String Res = perpetualWebService.GetCloudServers(User, PassWord);
                ViseLog.i("GetSTask RES = " + Res);
                InputStream Is_Res = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                List<AdapterReturn> adapterReturnList = AnalysisReturnsXml.getSingleton().GetReturn(Is_Res);
                Is_Res.close();
                if (adapterReturnList.get(0).getFStatus().equals("1")) {
                    return "Ss" + adapterReturnList.get(0).getFInfo();
                } else {
                    return "Er" + adapterReturnList.get(0).getFInfo();
                }
            } catch (Exception e) {
                ViseLog.i("GetSTask Exception = " + e);
                return "Ex";
            }
        }

        protected void onPostExecute(String result) {
            switch (result.substring(0, 2)) {
                case "Ss": {
                    try {
                        ViseLog.i("onPostExecute Result = " + result.substring(2, result.length()));
                        InputStream IsRes = new ByteArrayInputStream(result.substring(2, result.length()).getBytes("UTF-8"));
                        setting_publicDataBeanList = Setting_F_FragmentXml.getSingleton().GetTable0(IsRes);
                        IsRes.close();
                        InitSp();
                        if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_Service)) && Tools.IsObjectNull(setting_publicDataBeanList)) {
                            int Pos = GetSpinnerPos(setting_publicDataBeanList, tools.GetStringData(sharedPreferences, ConnectStr.F_Public_Service));
                            Sp_SelectService.setSelection(Pos);
                            Sp_SelectServiceIndex = Pos;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ViseLog.i("GetSTask  onPostExecute Ss Exception = " + e);
                    }
                    break;
                }
                case "Er": {
                    tools.ShowDialog(getActivity(), result.substring(2, result.length()));
                    break;
                }
                case "Ex": {
                    tools.ShowDialog(getActivity(), "解析数据异常");
                    break;
                }
            }
        }
    }

    private void InitSp() {
        if (setting_publicDataBeanList.size() >= 0 && Tools.IsObjectNull(setting_publicDataBeanList)) {
            SettingPublicYunAdapter QuitStockAdapter = new SettingPublicYunAdapter(setting_publicDataBeanList, getActivity());
            Sp_SelectService.setAdapter(QuitStockAdapter);
            Sp_SelectService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (Sp_SelectServiceIndex != i) {
                        Sp_SelectServiceIndex = i;
                    }
                    tools.PutStringData(ConnectStr.F_Public_Service, setting_publicDataBeanList.get(i).getFGuid(), sharedPreferences);
                    tools.PutStringData(ConnectStr.F_Private_IP_Address, setting_publicDataBeanList.get(Sp_SelectServiceIndex).getFServer(), sharedPreferences);
                    tools.PutStringData(ConnectStr.F_Private_Port_Address, setting_publicDataBeanList.get(Sp_SelectServiceIndex).getFPort(), sharedPreferences);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @Override
    public void Public() {
        if (!TextUtils.isEmpty(ET_P.getText().toString().trim())) {
            tools.PutStringData(ConnectStr.F_Public_PassWord, ET_P.getText().toString(), sharedPreferences);
        }
        if (!TextUtils.isEmpty(ET_U.getText().toString().trim())) {
            tools.PutStringData(ConnectStr.F_Public_User, ET_U.getText().toString(), sharedPreferences);
        }
        if (setting_publicDataBeanList.size() > 0) {
            tools.PutStringData(ConnectStr.F_Public_Service, setting_publicDataBeanList.get(Sp_SelectServiceIndex).getFGuid(), sharedPreferences);
        }
        if (!TextUtils.isEmpty(ET_P.getText().toString()) && !TextUtils.isEmpty(ET_U.getText().toString()) && setting_publicDataBeanList.size() > 0) {
            tools.PutStringData("LinkMode", "Public", sharedPreferences);
            Myapplication.LinkWayMode = "Public";
        }
    }

    @Override
    public void Private() {

    }

}
