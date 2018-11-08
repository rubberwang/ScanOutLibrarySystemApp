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

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectOutFullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class SearchOutLibraryFragment extends Fragment {

    private static List<OutLibraryBill> selectOutLibraryList;
    private RecyclerView RV_CastAbout;
    private ProgressDialog PD;
    private Tools tools;
    private WebService webService;

    public static SearchOutLibraryFragment newInstance(List<OutLibraryBill> selectOutLibraryData) {
        selectOutLibraryList = new ArrayList<OutLibraryBill>();
        selectOutLibraryList.clear();
        SearchOutLibraryFragment.selectOutLibraryList = selectOutLibraryData;
        return new SearchOutLibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.initselectoutlibrary_fragment, container, false);
        RV_CastAbout = view.findViewById(R.id.RV_InitSelectFull);
        tools = new Tools();
        webService = WebService.getSingleton();
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
        if (selectOutLibraryList.size() >= 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_CastAbout.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
            RV_CastAbout.setLayoutManager(layoutManager);
            SelectOutFullAdapter adapter = new SelectOutFullAdapter(getActivity(), selectOutLibraryList);
            RV_CastAbout.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SelectOutFullAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }
}
