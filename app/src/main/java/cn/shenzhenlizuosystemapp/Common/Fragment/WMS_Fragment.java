package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.Wms_RvAdapter;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.WmsSelectData;
import cn.shenzhenlizuosystemapp.Common.ImageTool.TransformationUtils;
import cn.shenzhenlizuosystemapp.Common.UI.DirectAllot.AllotNotificationActivity;
import cn.shenzhenlizuosystemapp.Common.UI.Input_NotificationActivity;
import cn.shenzhenlizuosystemapp.Common.UI.Quit_NotificationActivity;
import cn.shenzhenlizuosystemapp.Common.UI.Check_NotificationActivity;
import cn.shenzhenlizuosystemapp.Common.View.*;
import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.R;

import static cn.shenzhenlizuosystemapp.Common.Base.Myapplication.myapplication;

public class WMS_Fragment extends Fragment implements OnBannerListener {

    public static final String ARGS_PAGE = "WMS_Page";
    private RecyclerView Rv_WmsModuleSelect;
    private TextView Back;
    private Banner WMS_Banner;

    private ArrayList<WmsSelectData> List_wmsSelectData;
    private String[] Describe = {"入库作业", "出库作业", "直接调拨", "盘点作业", "调拨出库", "调拨入库"};
    private int[] R_Img = {R.drawable.gethouse, R.drawable.puthouse, R.drawable.changehouse, R.drawable.pdacheck, R.drawable.changeput, R.drawable.changeget};
    private ArrayList<Integer> BannerPathList;
    private ArrayList<String> BannerTitleList;

    public static MES_Fragment newInstance() {
        MES_Fragment fragment = new MES_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public class WMS_BannerImageLoader extends ImageLoader {
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).placeholder(R.drawable.loading).error(R.drawable.can).dontAnimate().fitCenter().into(imageView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wms, container, false);
        Rv_WmsModuleSelect = rootView.findViewById(R.id.Rv_WmsModuleSelect);
        WMS_Banner = rootView.findViewById(R.id.B_WMS);
        Back = rootView.findViewById(R.id.Back);
        List_wmsSelectData = new ArrayList<>();
        InitRecycler();
        InitClick();
        InitBanner();
        return rootView;
    }

    private void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn.shenzhenlizuosystemapp.Common.Base.ViewManager.getInstance().finishActivity(getActivity());
            }
        });
    }

    private void InitBanner() {
        BannerPathList = new ArrayList<>();
        BannerTitleList = new ArrayList<>();
        BannerPathList.add(R.drawable.lizi);
        BannerPathList.add(R.drawable.lizi2);
        BannerPathList.add(R.drawable.lizi3);
        BannerTitleList.add("广告图1");
        BannerTitleList.add("广告图2");
        BannerTitleList.add("广告图3");
        WMS_Banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        WMS_Banner.setImageLoader(new WMS_BannerImageLoader());
        WMS_Banner.setImages(BannerPathList);
        WMS_Banner.setBannerAnimation(Transformer.Default);
        WMS_Banner.setBannerTitles(BannerTitleList);
        WMS_Banner.setDelayTime(5 * 1000);
        WMS_Banner.isAutoPlay(true);
        WMS_Banner.setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(this)
                .start();
    }

    private void InitRecycler() {
        for (int i = 0; i < Describe.length; i++) {
            WmsSelectData wmsSelectData = new WmsSelectData();
            wmsSelectData.setDescribeStr(Describe[i]);
            wmsSelectData.setR_Img(R_Img[i]);
            List_wmsSelectData.add(wmsSelectData);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Rv_WmsModuleSelect.setLayoutManager(gridLayoutManager);
        Wms_RvAdapter adapter = new Wms_RvAdapter(getActivity(), List_wmsSelectData);
        Rv_WmsModuleSelect.setItemAnimator(new DefaultItemAnimator());
        Rv_WmsModuleSelect.setAdapter(adapter);
        adapter.setOnItemClickLitener(new Wms_RvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0: {
                        startActivity(new Intent(getActivity(), Input_NotificationActivity.class));//入库通知
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(getActivity(), Quit_NotificationActivity.class));//出库通知
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(getActivity(), AllotNotificationActivity.class));//直接调拨通知
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(getActivity(), Check_NotificationActivity.class));
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    @Override
    public void OnBannerClick(int position) {

    }

}
