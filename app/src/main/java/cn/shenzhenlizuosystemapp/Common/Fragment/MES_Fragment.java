package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.shenzhenlizuosystemapp.R;

public class MES_Fragment extends Fragment {
    public static final String ARGS_PAGE = "MES_Page";

    public static MES_Fragment newInstance() {
        MES_Fragment fragment = new MES_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mes, container, false);
        return rootView;
    }
}
