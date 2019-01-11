package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.vise.log.ViseLog;

import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.PerpetualWebService;
import cn.shenzhenlizuosystemapp.Common.Port.YunPort;
import cn.shenzhenlizuosystemapp.Common.UI.LoginActivity;
import cn.shenzhenlizuosystemapp.R;

public class S_PrivateYunFragment extends Fragment implements YunPort {

    public static final String ARGS_PAGE = "S_PrivateYunFragment";
    private static SharedPreferences sharedPreferences;
    private static Tools tools;

    private static EditText ET_IP;
    private static EditText ET_Port;
    private View TextView;

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
        TextView = rootView.findViewById(R.id.TextView);
        tools = Tools.getTools();
        sharedPreferences = tools.InitSharedPreferences(getActivity());
        CheckIsSaveData();
        TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Private();
            }
        });
        return rootView;
    }

    private void CheckIsSaveData() {
        //判断是否存在ip port
        if (Tools.IsObjectNull(sharedPreferences)) {
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_IP_Address))) {
                ET_IP.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_IP_Address));
                ViseLog.i("S_PrivateYunFragment 恢复 IP = " + ET_IP.getText().toString());
            }
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_Port_Address))) {
                ET_Port.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Private_Port_Address));
            }
        }
        if (!TextUtils.isEmpty(ET_IP.getText().toString())) {
            ET_IP.setSelection(ET_IP.getText().length());
        }
        if (!TextUtils.isEmpty(ET_Port.getText().toString())) {
            ET_Port.setSelection(ET_Port.getText().length());
        }
    }

    @Override
    public void Public() {

    }

    @Override
    public void Private() {
        if (!TextUtils.isEmpty(ET_IP.getText().toString())) {
            tools.PutStringData(ConnectStr.F_Private_IP_Address, ET_IP.getText().toString().trim(), sharedPreferences);
            ViseLog.i("S_PrivateYunFragment IP = " + ET_IP.getText().toString());
        }
        if (!TextUtils.isEmpty(ET_Port.getText().toString())) {
            tools.PutStringData(ConnectStr.F_Private_Port_Address, ET_Port.getText().toString().trim(), sharedPreferences);
        }
        if (!TextUtils.isEmpty(ET_IP.getText().toString()) && !TextUtils.isEmpty(ET_Port.getText().toString())) {
            tools.PutStringData("LinkMode", "Private", sharedPreferences);
            Myapplication.LinkWayMode = "Private";
        }
    }
}

