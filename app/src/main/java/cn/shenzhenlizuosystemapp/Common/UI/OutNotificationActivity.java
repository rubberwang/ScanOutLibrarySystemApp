package cn.shenzhenlizuosystemapp.Common.UI;

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

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryBill;
import cn.shenzhenlizuosystemapp.Common.Fragment.SearchOutLibraryFragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.SelectOutLibraryFragment;
import cn.shenzhenlizuosystemapp.R;

public class OutNotificationActivity extends BaseActivity {

    private ProgressDialog PD;
    private TextView TV_CastAbout;
    private EditText ET_CastAbout;

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    private LinearLayout LL_BackMainTable;
   private SelectOutLibraryFragment selectOutLibraryFragment;
   private List<OutLibraryBill> outLibraryBills;
    private List<OutLibraryBill> SearchResultList;
   private Tools tools;

    @Override
    protected int inflateLayout() {
        return R.layout.selectoutlibrary_layout;
    }

    @Override
    public void initData() {
        SearchResultList = new ArrayList<>();
        tools  = new Tools();
        InitFragment();
        InitClick();
    }

    @Override
    public void initView() {
        PD = new ProgressDialog(this);
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        LL_BackMainTable = $(R.id.LL_BackMainTable);
        TV_CastAbout=$(R.id.TV_CastAbout);
        ET_CastAbout = $(R.id.ET_CastAbout);
    }

    private void InitClick(){
        TV_CastAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLoad(ET_CastAbout.getText().toString());
                ViseLog.i("搜索内容"+ET_CastAbout.getText().toString());
            }
        });
    }

    private void InitFragment(){
        if (LL_BackMainTable.getVisibility() == View.VISIBLE) {
            LL_BackMainTable.setVisibility(View.GONE);
        }
         selectOutLibraryFragment = SelectOutLibraryFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.F_SelectOut, selectOutLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
    }

    public void ToLoad(String ChracterSearch) {
        ToLoadOutLibraryBillsAsyncTask toLoadOutLibraryBillsAsyncTask = new ToLoadOutLibraryBillsAsyncTask(ChracterSearch);
        toLoadOutLibraryBillsAsyncTask.execute();
    }

    private int LookForBranch(String ChracterSearch) {
        try {
            outLibraryBills = selectOutLibraryFragment.GetSelectBills();
            if (outLibraryBills.size() >= 0) {
                SearchResultList.clear();
                for (int i = 0; i < outLibraryBills.size(); i++) {
                    String BoxNumber = outLibraryBills.get(i).getFCode();
                    Log.i("huangmin", "BoxNumber " + BoxNumber.charAt(0));
//                    ||BoxNumber.substring(2,
                    if (BoxNumber.equals(ChracterSearch) || BoxNumber.contains(ChracterSearch)) {
                        OutLibraryBill outLibraryBill = new OutLibraryBill();
                        outLibraryBill.setFCode(outLibraryBills.get(i).getFCode());
                        outLibraryBill.setFDate(outLibraryBills.get(i).getFDate());
                        outLibraryBill.setFTransactionType_Name(outLibraryBills.get(i).getFTransactionType_Name());
                        outLibraryBill.setFTransactionType(outLibraryBills.get(i).getFTransactionType());
                        outLibraryBill.setFStock_Name(outLibraryBills.get(i).getFStock_Name());
                        outLibraryBill.setFStock(outLibraryBills.get(i).getFStock());
                        outLibraryBill.setFGuid(outLibraryBills.get(i).getFGuid());
                        SearchResultList.add(outLibraryBill);
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
            int OutBills = 0;
            try {
                OutBills = LookForBranch(ChracterSearch);
            } catch (Exception e) {
                ViseLog.d("SelectOutLibraryGetOutLibraryBillsException " + e);
            }
            return OutBills;
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
                    SearchOutLibraryFragment searchOutLibraryFragment = SearchOutLibraryFragment.newInstance(SearchResultList);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.F_SelectOut, searchOutLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
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
                    tools.showshort(OutNotificationActivity.this, "搜索错误请重新搜索");
                    ViseLog.d("搜索失败");
                }
            } catch (Exception e) {
                ViseLog.d("搜索错误" + e);
                tools.showshort(OutNotificationActivity.this, "搜索错误请重新搜索");
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
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getFragmentManager().getBackStackEntryCount()==0){
                ViewManager.getInstance().finishActivity(this);
                return true;
            }
        }
        return true;
    }
}
