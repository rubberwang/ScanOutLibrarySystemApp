package cn.shenzhenlizuosystemapp.Common.UI.QuitActivity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
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
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.QuitLibraryBill;
import cn.shenzhenlizuosystemapp.Common.Fragment.QuitFragment.Item_QuitLibrary_Fragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.QuitFragment.Select_QuitLibrary_Fragment;
import cn.shenzhenlizuosystemapp.R;

public class Quit_NotificationActivity extends BaseActivity {

    private ProgressDialog PD;
    private TextView TV_CastAbout;
    private EditText ET_CastAbout;
    private TextView Back;
    private TextView TV_Refresh;

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";

    private LinearLayout LL_BackMainTable;
    private Select_QuitLibrary_Fragment selectQuitLibraryFragment;
    private List<QuitLibraryBill> quitLibraryBills;
    private List<QuitLibraryBill> SearchResultList;
    private Tools tools;
    private Quit_NotificationActivity MContect = null;

    @Override
    protected int inflateLayout() {
        return R.layout.select_quitlibrary_layout;
    }

    @Override
    public void initData() {
        MContect = new WeakReference<>(Quit_NotificationActivity.this).get();
        SearchResultList = new ArrayList<>();
        tools = new Tools();
        getLifecycle().addObserver(new Quit_NotificationObServer());
        InitClick();
    }

    class Quit_NotificationObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
            InitFragment();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
        }
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
                ViewManager.getInstance().finishActivity(Quit_NotificationActivity.this);
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
         selectQuitLibraryFragment = Select_QuitLibrary_Fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.F_SelectOut1, selectQuitLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
    }

    public void ToLoad(String ChracterSearch) {
        ToLoadOutLibraryBillsAsyncTask toLoadOutLibraryBillsAsyncTask = new ToLoadOutLibraryBillsAsyncTask(ChracterSearch);
        toLoadOutLibraryBillsAsyncTask.execute();
    }

    private int LookForBranch(String ChracterSearch) {
        try {
            quitLibraryBills = selectQuitLibraryFragment.GetSelectBills();
            if (quitLibraryBills.size() >= 0) {
                SearchResultList.clear();
                for (int i = 0; i < quitLibraryBills.size(); i++) {
                    String BoxNumber = quitLibraryBills.get(i).getFCode();
                    Log.i("huangmin", "BoxNumber " + BoxNumber.charAt(0));
                    if (BoxNumber.equals(ChracterSearch) || BoxNumber.contains(ChracterSearch)) {
                        QuitLibraryBill quitLibraryBill = new QuitLibraryBill();
                        quitLibraryBill.setFCode(quitLibraryBills.get(i).getFCode());
                        quitLibraryBill.setFDate(quitLibraryBills.get(i).getFDate());
                        quitLibraryBill.setFTransactionType_Name(quitLibraryBills.get(i).getFTransactionType_Name());
                        quitLibraryBill.setFTransactionType(quitLibraryBills.get(i).getFTransactionType());
                        quitLibraryBill.setFStock_Name(quitLibraryBills.get(i).getFStock_Name());
                        quitLibraryBill.setFStock(quitLibraryBills.get(i).getFStock());
                        quitLibraryBill.setFGuid(quitLibraryBills.get(i).getFGuid());
                        SearchResultList.add(quitLibraryBill);
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
            int QuitBills = 0;
            try {
                QuitBills = LookForBranch(ChracterSearch);
            } catch (Exception e) {
                ViseLog.d("SelectQuitLibraryGetQuitLibraryBillsException " + e);
            }
            return QuitBills;
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
                    Item_QuitLibrary_Fragment itemOutLibraryFragment = Item_QuitLibrary_Fragment.newInstance(SearchResultList);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.F_SelectOut1, itemOutLibraryFragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
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
                    tools.showshort(MContect, "搜索错误请重新搜索");
                    ViseLog.d("搜索失败");
                }
            } catch (Exception e) {
                ViseLog.d("搜索错误" + e);
                tools.showshort(MContect, "搜索错误请重新搜索");
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
