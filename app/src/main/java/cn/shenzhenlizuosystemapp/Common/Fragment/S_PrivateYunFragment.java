package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.LoginActivity;
import cn.shenzhenlizuosystemapp.Common.UI.MainTabActivity;
import cn.shenzhenlizuosystemapp.Common.UI.SettingActivity;
import cn.shenzhenlizuosystemapp.R;

public class S_PrivateYunFragment extends Fragment {

    public static final String ARGS_PAGE = "S_PrivateYunFragment";
    private SharedPreferences sharedPreferences;
    private Tools tools;

    private EditText ET_IP;
    private EditText ET_Port;
    private Button SaveIPAddress_Bt;

    public static S_PrivateYunFragment newInstance() {
        S_PrivateYunFragment fragment = new S_PrivateYunFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_privite_yun_setting, container, false);
        ET_IP = rootView.findViewById(R.id.ET_IP);
        ET_Port = rootView.findViewById(R.id.ET_Port);
        SaveIPAddress_Bt = rootView.findViewById(R.id.SaveIPAddress_Bt);
        tools = Tools.getTools();
        sharedPreferences = tools.InitSharedPreferences(getActivity());
        CheckIsSaveData();
        InitSave();
        return rootView;
    }

    private void CheckIsSaveData() {
        //判断是否存在ip port
        if (Tools.IsObjectNull(sharedPreferences)) {
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_IP_Address))) {
                ET_IP.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_IP_Address));
            }
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_Port_Address))) {
                ET_Port.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_Port_Address));
            }
        }
        if (!TextUtils.isEmpty(ET_IP.getText().toString())){
            ET_IP.setSelection(ET_IP.getText().length());
        }
        if (!TextUtils.isEmpty(ET_Port.getText().toString())){
            ET_Port.setSelection(ET_Port.getText().length());
        }
    }

    private void InitSave() {
        SaveIPAddress_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
                ViewManager.getInstance().finishActivity(getActivity());
            }
        });
    }

    private void SaveData() {
        if (!TextUtils.isEmpty(ET_IP.getText().toString())) {
            tools.PutStringData(ConnectStr.F_Private_IP_Address, ET_IP.getText().toString().trim(), sharedPreferences);
        }
        if (!TextUtils.isEmpty(ET_Port.getText().toString())) {
            tools.PutStringData(ConnectStr.F_Private_Port_Address, ET_Port.getText().toString().trim(), sharedPreferences);
        }
    }
}

