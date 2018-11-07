package cn.shenzhenlizuosystemapp.Common.UI;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.LinkedList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Fragment.MES_Fragment;
import cn.shenzhenlizuosystemapp.Common.Fragment.WMS_Fragment;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.MainPagerAdapter;
import cn.shenzhenlizuosystemapp.Common.TabAdapter.TabItemInfo;
import cn.shenzhenlizuosystemapp.R;

public class MainTabActivity extends BaseActivity {

    private TabLayout Tab_MainTab;
    private ViewPager VP_ViewPage;

    @Override
    protected int inflateLayout() {
        return R.layout.activity_maintab;
    }

    @Override
    public void initData() {
        InitTab();
    }

    @Override
    public void initView() {
        Tab_MainTab = $(R.id.Tab_MainTab);
        VP_ViewPage = $(R.id.VP_ViewPage);
    }

    private void InitTab() {
        List<TabItemInfo> tabItems = new LinkedList<>();
        tabItems.add(new TabItemInfo(WMS_Fragment.class, R.string.WMS, R.drawable.wms_icon));
        tabItems.add(new TabItemInfo(MES_Fragment.class, R.string.MES, R.drawable.mes_icon));

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager(), tabItems);
        VP_ViewPage.setAdapter(pagerAdapter);
        Tab_MainTab.setupWithViewPager(VP_ViewPage);
        for (int i = 0; i < Tab_MainTab.getTabCount(); i++) {
            TabLayout.Tab tab = Tab_MainTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
    }
}
