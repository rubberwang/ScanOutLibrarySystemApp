package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectCheck_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.UI.CheckLibraryActivity;
import cn.shenzhenlizuosystemapp.R;

public class Item_CheckLibrary_Fragment extends Fragment {

    private static List<CheckLibraryBill> selectSumLibraryList;
    private RecyclerView RV_CastAbout;
    private ProgressDialog PD;
    private Tools tools;
    private WebService webService;

    public static Item_CheckLibrary_Fragment newInstance(List<CheckLibraryBill> selectOutLibraryData) {
        selectSumLibraryList = new ArrayList<CheckLibraryBill>();
        selectSumLibraryList.clear();
        Item_CheckLibrary_Fragment.selectSumLibraryList = selectOutLibraryData;
        return new Item_CheckLibrary_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.init_select_quitlibrary, container, false);
        RV_CastAbout = view.findViewById(R.id.RV_InitSelectFull);
        tools = new Tools();
        webService = new WebService(this.getActivity());
        PD = new ProgressDialog(this.getActivity());
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        try {
            ToLoadData();
        } catch (Exception e) {
            ViseLog.d("CastAboutFragment适配数据异常" + e);
        }
        return view;
    }

    private void ToLoadData() {
        if (selectSumLibraryList.size() >= 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_CastAbout.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
            RV_CastAbout.setLayoutManager(layoutManager);
            SelectCheck_FullAdapter adapter = new SelectCheck_FullAdapter(getActivity(), selectSumLibraryList);
            RV_CastAbout.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SelectCheck_FullAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(getActivity(),CheckLibraryActivity.class));
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }
}
