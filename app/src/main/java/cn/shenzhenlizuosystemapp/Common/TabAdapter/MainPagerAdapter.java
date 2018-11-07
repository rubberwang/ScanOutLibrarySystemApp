package cn.shenzhenlizuosystemapp.Common.TabAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.R;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<TabItemInfo> mTabItems;

    public MainPagerAdapter(Context context, FragmentManager fm, List<TabItemInfo> tabItems) {
        super(fm);
        mContext = context;
        mTabItems = tabItems;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return mTabItems.get(position).getFragmentClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTabItems.size();
    }

    public View getTabView(int position) {
        TabItemInfo itemInfo = mTabItems.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_view_tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_text);
        tv.setText(itemInfo.getNameResource());
        ImageView img = (ImageView) view.findViewById(R.id.tab_image);
        img.setImageResource(itemInfo.getIconResource());
        return view;
    }
}  
