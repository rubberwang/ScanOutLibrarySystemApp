package cn.shenzhenlizuosystemapp.Common.UI.DirectAllot;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDataAnalysis.DirectAllotLibraryBill;
import cn.shenzhenlizuosystemapp.Common.Fragment.DirectAllotFragment.Item_DirectAllot_Fragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.DirectAllotFragment.Select_DirectAllot_Fragment;
import cn.shenzhenlizuosystemapp.R;

public class AllotNotificationActivity extends BaseActivity {

    private ProgressDialog PD;
    private TextView TV_CastAbout;
    private EditText ET_CastAbout;
    private TextView Back;
    private TextView TV_Refresh;
    private TextView BarText;

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    private Select_DirectAllot_Fragment select_directAllot_fragment;
    private List<DirectAllotLibraryBill> directAllotLibraryBillList;
    private List<DirectAllotLibraryBill> SearchResultList;
    private Tools tools;
    private AllotNotificationActivity MContect = null;
    private String Type;

    @Override
    protected int inflateLayout() {
        return R.layout.allot_notification_layout;
    }

    @Override
    public void initData() {
        Type = getIntent().getStringExtra("Type");
        MContect = new WeakReference<>(AllotNotificationActivity.this).get();
        SearchResultList = new ArrayList<>();
        tools = new Tools();
        InitFragment();
        InitClick();
        switch (Type) {
            case "0": {
                BarText.setText(R.string.DircetAllotNotification);
                break;
            }
            case "-1": {
                BarText.setText(R.string.OutAllotNotification);
                break;
            }
            case "1": {
                BarText.setText(R.string.InAllotNotification);
                break;
            }
        }
    }

    @Override
    public void initView() {
        PD = new ProgressDialog(this);
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        TV_CastAbout = $(R.id.TV_CastAbout);
        ET_CastAbout = $(R.id.ET_CastAbout);
        Back = $(R.id.Back);
        TV_Refresh = $(R.id.TV_Refresh);
        BarText = $(R.id.BarText);
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
                ViewManager.getInstance().finishActivity(AllotNotificationActivity.this);
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
        select_directAllot_fragment = Select_DirectAllot_Fragment.newInstance(Type);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.FL_SelectDirectAllot, select_directAllot_fragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
    }

    public void ToLoad(String ChracterSearch) {
        DirectAllotTask toLoadOutLibraryBillsAsyncTask = new DirectAllotTask(ChracterSearch);
        toLoadOutLibraryBillsAsyncTask.execute();
    }

    private int LookForBranch(String ChracterSearch) {
        try {
            directAllotLibraryBillList = select_directAllot_fragment.GetSelectBills();
            if (directAllotLibraryBillList.size() >= 0) {
                SearchResultList.clear();
                for (int i = 0; i < directAllotLibraryBillList.size(); i++) {
                    String BoxNumber = directAllotLibraryBillList.get(i).getFCode();
                    if (BoxNumber.equals(ChracterSearch) || BoxNumber.contains(ChracterSearch)) {
                        DirectAllotLibraryBill directAllotLibraryBill = new DirectAllotLibraryBill();
                        directAllotLibraryBill.setFGuid(directAllotLibraryBillList.get(i).getFGuid());
                        directAllotLibraryBill.setFCode(directAllotLibraryBillList.get(i).getFCode());
                        directAllotLibraryBill.setFDate(directAllotLibraryBillList.get(i).getFDate());
                        directAllotLibraryBill.setFTransactionType_Name(directAllotLibraryBillList.get(i).getFTransactionType_Name());
                        directAllotLibraryBill.setFTransactionType(directAllotLibraryBillList.get(i).getFTransactionType());
                        directAllotLibraryBill.setFOutStock_Name(directAllotLibraryBillList.get(i).getFOutStock_Name());
                        directAllotLibraryBill.setFOutStock(directAllotLibraryBillList.get(i).getFOutStock());
                        directAllotLibraryBill.setFInStock_Name(directAllotLibraryBillList.get(i).getFInStock_Name());
                        directAllotLibraryBill.setFInStock(directAllotLibraryBillList.get(i).getFInStock());
                        SearchResultList.add(directAllotLibraryBill);
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


    public class DirectAllotTask extends AsyncTask<Integer, Integer, Integer> {

        private String ChracterSearch;

        public DirectAllotTask(String ChracterSearch) {
            super();
            this.ChracterSearch = ChracterSearch;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int AllotBills = 0;
            try {
                AllotBills = LookForBranch(ChracterSearch);
            } catch (Exception e) {
                ViseLog.d("SelectOutLibraryGetOutLibraryBillsException " + e);
            }
            return AllotBills;
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                PD.dismiss();
                if (result == 1) {
                    Item_DirectAllot_Fragment itemInputLibraryFragment = Item_DirectAllot_Fragment.newInstance(SearchResultList);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.FL_SelectDirectAllot, itemInputLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
                    ViseLog.d("搜索成功");
                } else {
                    tools.showshort(MContect, "搜索错误请重新搜索");
                    ViseLog.d("搜索失败");
                }
            } catch (Exception e) {
                ViseLog.d("搜索错误" + e);
                tools.showshort(MContect, "搜索错误请重新搜索");
            }
            ViseLog.i("搜索结果" + result);
        }

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
