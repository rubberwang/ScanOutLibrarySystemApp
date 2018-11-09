package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.WmsRvAdapter;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.WmsSelectData;
import cn.shenzhenlizuosystemapp.Common.UI.InputNotificationActivity;
import cn.shenzhenlizuosystemapp.Common.UI.OutLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.UI.OutNotificationActivity;
import cn.shenzhenlizuosystemapp.Common.View.ItemDecoration;
import cn.shenzhenlizuosystemapp.R;

public class WMS_Fragment extends Fragment {

    public static final String ARGS_PAGE = "WMS_Page";
    private RecyclerView Rv_WmsModuleSelect;
    private TextView Back;


    private ArrayList<WmsSelectData> List_wmsSelectData;
    private String[] Describe = {"入库作业", "出库作业", "调拨作业", "盘点作业"};
    private int[] R_Img = {R.drawable.gethouse, R.drawable.puthouse, R.drawable.changehouse, R.drawable.pdacheck};

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
        View rootView = inflater.inflate(R.layout.fragment_wms, container, false);
        Rv_WmsModuleSelect = rootView.findViewById(R.id.Rv_WmsModuleSelect);
        Back = rootView.findViewById(R.id.Back);
        List_wmsSelectData = new ArrayList<>();
        InitRecycler();
        InitClick();
        return rootView;
    }

    private void InitClick(){
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                cn.shenzhenlizuosystemapp.Common.Base.ViewManager.getInstance().finishActivity(getActivity());
            }
        });
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
        WmsRvAdapter adapter = new WmsRvAdapter(getActivity(), List_wmsSelectData);
        Rv_WmsModuleSelect.setItemAnimator(new DefaultItemAnimator());
        Rv_WmsModuleSelect.setAdapter(adapter);
        adapter.setOnItemClickLitener(new WmsRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:{
                        startActivity(new Intent(getActivity(), InputNotificationActivity.class));
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(getActivity(), OutNotificationActivity.class));
                        break;
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
}
