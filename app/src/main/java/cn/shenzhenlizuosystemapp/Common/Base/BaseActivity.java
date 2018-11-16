package cn.shenzhenlizuosystemapp.Common.Base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vise.log.ViseLog;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();//设置全屏和状态栏透明
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ViewManager.getInstance().addActivity(this);
        setContentView(inflateLayout());
        initView();
        initData();
    }

    protected abstract int inflateLayout();

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    public abstract void initData();

    public abstract void initView();

    //防止用户快速点击
    public boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            ViseLog.i("点快了");
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().finishActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
