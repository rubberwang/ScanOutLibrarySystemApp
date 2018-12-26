package cn.shenzhenlizuosystemapp.Common.TabAdapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

public class TabItemInfo {
    private Class<? extends Fragment> fragmentClass;
    private int nameResource;
    private int iconResource;

    public TabItemInfo(Class<? extends Fragment> fragmentClass, @StringRes int nameResource,
                       @DrawableRes int iconResource) {
        this.fragmentClass = fragmentClass;
        this.nameResource = nameResource;
        this.iconResource = iconResource;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public int getNameResource() {
        return nameResource;
    }

    public int getIconResource() {
        return iconResource;
    }
}
