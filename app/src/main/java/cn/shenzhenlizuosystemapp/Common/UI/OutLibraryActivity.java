package cn.shenzhenlizuosystemapp.Common.UI;

import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResultRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.SelectOutFullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.BroadcastReceiver.ReceiveData_Recevier;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class OutLibraryActivity extends BaseActivity {

    private TextView Back;
    private RecyclerView RV_GetInfoTable;
    private ScanResultRvAdapter scanResultRvAdapter;

    private OutLibraryObServer outLibraryObServer;
    private List<ScanResultData> scanResultData;

    @Override
    protected int inflateLayout() {
        return R.layout.scanoutlibrary_layout;
    }

    @Override
    public void initData() {
        scanResultData = new ArrayList<>();
        outLibraryObServer = new OutLibraryObServer();
        getLifecycle().addObserver(outLibraryObServer);
        EventBus.getDefault().register(this);
        BackFinish();
        InitRecycler();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        RV_GetInfoTable = $(R.id.RV_GetInfoTable);
    }

    private void InitRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_GetInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_GetInfoTable.setLayoutManager(layoutManager);
        scanResultRvAdapter = new ScanResultRvAdapter(this, scanResultData);
        RV_GetInfoTable.setAdapter(scanResultRvAdapter);
        scanResultRvAdapter.setOnItemClickLitener(new ScanResultRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }


    public void BackFinish() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(OutLibraryActivity.this);
            }

        });
    }

    class OutLibraryObServer implements LifecycleObserver {
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
            EventBus.getDefault().unregister(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(EventBusScanDataMsg event) {
        ViseLog.i("EventBus = " + event.ScanDataMsg);
        ScanResultData scanResult = new ScanResultData();
        scanResult.setScanData(event.ScanDataMsg);
        scanResultData.add(scanResult);
        scanResultRvAdapter.notifyDataSetChanged();
    }
}
