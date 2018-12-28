package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckScanResultData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class CheckLibraryActivity extends BaseActivity {

    private TextView Back;
    private Tools tools = null;
    private Context MContect;
    private RecyclerView RV_GetInfoTable;
    private ScanResult_CheckRvAdapter scanResultRvAdapter;

    private OutLibraryObServer outLibraryObServer;
    private List<CheckScanResultData> checkScanResultData;

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_check_layout;
    }

    @Override
    public void initData() {
        checkScanResultData = new ArrayList<>();
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
        RV_GetInfoTable.setLayoutManager(layoutManager);;
        RV_GetInfoTable.setAdapter(scanResultRvAdapter);
        scanResultRvAdapter.setOnItemClickLitener(new ScanResult_CheckRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                    tools.ShowDialog(MContect, "这张单已扫描完成");
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
                ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
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
        CheckScanResultData scanResult = new CheckScanResultData();
        scanResult.setScanData(event.ScanDataMsg);
        checkScanResultData.add(scanResult);
        scanResultRvAdapter.notifyDataSetChanged();
    }
}
