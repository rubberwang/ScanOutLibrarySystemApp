package cn.shenzhenlizuosystemapp.Common.UI;

import android.view.View;
import android.widget.TextView;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.R;

public class SettingActivity extends BaseActivity {

    private TextView Back;

    @Override
    protected int inflateLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void initData() {
        InitClick();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
    }

    public void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(SettingActivity.this);
            }

        });
    }
}
