package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.R;

public class InputLibraryActivity extends BaseActivity {
    
    private TextView Back;
    private InputLibraryObServer inputLibraryObServer;

    @Override
    protected int inflateLayout() {
        return R.layout.scaninputlibrary_layout;
    }

    @Override
    public void initData() {
        inputLibraryObServer = new InputLibraryObServer();
        getLifecycle().addObserver(inputLibraryObServer);
        EventBus.getDefault().register(this);
        BackFinish();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
    }


    public void BackFinish() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
              ViewManager.getInstance().finishActivity(InputLibraryActivity.this);
            }

        });
    }

    class InputLibraryObServer implements LifecycleObserver {
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
    }
}
