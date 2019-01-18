package cn.shenzhenlizuosystemapp.Common.UI.InputActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.InputLibraryBill;
import cn.shenzhenlizuosystemapp.Common.Fragment.InputFragment.Item_InputLibrary_Fragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.InputFragment.Select_InputLibrary_Fragment;
import cn.shenzhenlizuosystemapp.R;

public class Input_NotificationActivity extends BaseActivity {

    private ProgressDialog PD;
    private TextView TV_CastAbout;
    private EditText ET_CastAbout;
    private TextView Back;
    private TextView TV_Refresh;

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    private LinearLayout LL_BackMainTable;
    private Select_InputLibrary_Fragment selectInputLibraryFragment;
    private List<InputLibraryBill> inputLibraryBills;
    private List<InputLibraryBill> SearchResultList;
    private Tools tools;
    private Input_NotificationActivity MContect = null;

    @Override
    protected int inflateLayout() {
        return R.layout.select_inputlibrary_layout;
    }

    @Override
    public void initData() {
        MContect = new WeakReference<>(Input_NotificationActivity.this).get();
        SearchResultList = new ArrayList<>();
        tools = new Tools();
        InitFragment();
        InitClick();
    }

    @Override
    public void initView() {
        PD = new ProgressDialog(this);
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        LL_BackMainTable = $(R.id.LL_BackMainTable);
        TV_CastAbout = $(R.id.TV_CastAbout);
        ET_CastAbout = $(R.id.ET_CastAbout);
        Back = $(R.id.Back);
        TV_Refresh = $(R.id.TV_Refresh);
    }

    private void InitClick() {
        TV_CastAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLoad(ET_CastAbout.getText().toString());
                ViseLog.i("搜索内容" + ET_CastAbout.getText().toString());
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(Input_NotificationActivity.this);
            }
        });
        TV_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitFragment();
            }
        });
    }

    private void InitFragment() {
        if (LL_BackMainTable.getVisibility() == View.VISIBLE) {
            LL_BackMainTable.setVisibility(View.GONE);
        }
        selectInputLibraryFragment = Select_InputLibrary_Fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.F_SelectOut, selectInputLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
    }

    public void ToLoad(String ChracterSearch) {
        ToLoadOutLibraryBillsAsyncTask toLoadOutLibraryBillsAsyncTask = new ToLoadOutLibraryBillsAsyncTask(ChracterSearch);
        toLoadOutLibraryBillsAsyncTask.execute();
    }

    private int LookForBranch(String ChracterSearch) {
        try {
            inputLibraryBills = selectInputLibraryFragment.GetSelectBills();
            if (inputLibraryBills.size() >= 0) {
                SearchResultList.clear();
                for (int i = 0; i < inputLibraryBills.size(); i++) {
                    String BoxNumber = inputLibraryBills.get(i).getFCode();
                    Log.i("huangmin", "BoxNumber " + BoxNumber.charAt(0));
                    if (BoxNumber.equals(ChracterSearch) || BoxNumber.contains(ChracterSearch)) {
                        InputLibraryBill inputLibraryBill = new InputLibraryBill();
                        inputLibraryBill.setFCode(inputLibraryBills.get(i).getFCode());
                        inputLibraryBill.setFDate(inputLibraryBills.get(i).getFDate());
                        inputLibraryBill.setFTransactionType_Name(inputLibraryBills.get(i).getFTransactionType_Name());
                        inputLibraryBill.setFTransactionType(inputLibraryBills.get(i).getFTransactionType());
                        inputLibraryBill.setFStock_Name(inputLibraryBills.get(i).getFStock_Name());
                        inputLibraryBill.setFStock(inputLibraryBills.get(i).getFStock());
                        inputLibraryBill.setFGuid(inputLibraryBills.get(i).getFGuid());
                        SearchResultList.add(inputLibraryBill);
                    }
                }
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            ViseLog.d("LookForBranchException " + e);
        }
        return 0;
    }


    public class ToLoadOutLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private String ChracterSearch;

        public ToLoadOutLibraryBillsAsyncTask(String ChracterSearch) {
            super();
            this.ChracterSearch = ChracterSearch;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int InputBills = 0;
            try {
                InputBills = LookForBranch(ChracterSearch);
            } catch (Exception e) {
                ViseLog.d("SelectOutLibraryGetOutLibraryBillsException " + e);
            }
            return InputBills;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(Integer result) {
            try {
                PD.dismiss();
                if (result == 1) {
                    Item_InputLibrary_Fragment itemInputLibraryFragment = Item_InputLibrary_Fragment.newInstance(SearchResultList);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.F_SelectOut, itemInputLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
                    LL_BackMainTable.setVisibility(View.VISIBLE);
                    TextView TV_BackMainTable = (TextView) LL_BackMainTable.findViewById(R.id.TV_BackMainTable);
                    TV_BackMainTable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            InitFragment();
                        }
                    });
                    ViseLog.d("搜索成功");
                } else {
                    tools.ShowDialog(MContect, "搜索错误请重新搜索");
                    ViseLog.d("搜索失败");
                }
            } catch (Exception e) {
                ViseLog.d("搜索错误" + e);
                tools.ShowDialog(MContect, "搜索错误请重新搜索");
            }
            ViseLog.i("搜索结果" + result);
        }

        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            PD.setTitle("正在搜索...");
            PD.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                ViewManager.getInstance().finishActivity(this);
                return true;
            }
        }
        return true;
    }
}
