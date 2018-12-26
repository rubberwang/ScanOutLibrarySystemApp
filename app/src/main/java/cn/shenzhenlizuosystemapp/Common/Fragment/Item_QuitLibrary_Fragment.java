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

//import cn.shenzhenlizuosystemapp.Common.Adapter.SelectInput_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.SelectQuit_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.NewQuitLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class Item_QuitLibrary_Fragment extends Fragment {

    private static List<QuitLibraryBill> selectOutLibraryList;
    private RecyclerView RV_CastAbout;
    private ProgressDialog PD;
    private Tools tools;
    private WebService webService;

    public static Item_QuitLibrary_Fragment newInstance(List<QuitLibraryBill> selectOutLibraryData) {
        selectOutLibraryList = new ArrayList<QuitLibraryBill>();
        selectOutLibraryList.clear();
        Item_QuitLibrary_Fragment.selectOutLibraryList = selectOutLibraryData;
        return new Item_QuitLibrary_Fragment();
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
        if (selectOutLibraryList.size() >= 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_CastAbout.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
            RV_CastAbout.setLayoutManager(layoutManager);
            SelectQuit_FullAdapter adapter = new SelectQuit_FullAdapter(getActivity(), selectOutLibraryList);
            RV_CastAbout.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SelectQuit_FullAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(),NewQuitLibraryActivity.class);
                    intent.putExtra("FGUID",selectOutLibraryList.get(position).getFGuid());
                    ViseLog.i("FGUID"+selectOutLibraryList.get(position).getFGuid());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }
}
