package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.PerpetualWebService;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.UI.SettingActivity;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.R;

public class S_PublicYunFragment extends Fragment {

    public static final String ARGS_PAGE = "S_PublicYunFragment";
    private SharedPreferences sharedPreferences;
    private Tools tools;
    private PerpetualWebService perpetualWebService;

    private EditText ET_U;
    private EditText ET_P;
    private Spinner Sp_SelectService;

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
        return rootView;
    }

    private void CheckIsSaveData() {
        if (Tools.IsObjectNull(sharedPreferences)) {
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_User))) {
                ET_U.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_User));
            }
            if (!TextUtils.isEmpty(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_PassWord))) {
                ET_P.setText(tools.GetStringData(sharedPreferences, ConnectStr.F_Public_PassWord));
            }
        } else {
            ViseLog.i(ARGS_PAGE + "sharedPreferences = null");
        }
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
                perpetualWebService.GetCloudServers(User, PassWord);
            } catch (Exception e) {

                return "" + e;
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {

            }
        }
    }
}
