package cn.shenzhenlizuosystemapp.Common.Fragment.InputFragment;

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

import cn.shenzhenlizuosystemapp.Common.Adapter.InputAdapter.SelectInput_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.InputLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.InputActivity.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class Item_InputLibrary_Fragment extends Fragment {

    private static List<InputLibraryBill> selectInputLibraryList;
    private RecyclerView RV_CastAbout;
    private ProgressDialog PD;
    private Tools tools;
    private WebService webService;

    public static Item_InputLibrary_Fragment newInstance(List<InputLibraryBill> selectInputLibraryData) {
        selectInputLibraryList = new ArrayList<InputLibraryBill>();
        selectInputLibraryList.clear();
        Item_InputLibrary_Fragment.selectInputLibraryList = selectInputLibraryData;
        return new Item_InputLibrary_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.init_select_inputlibrary, container, false);
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
        if (selectInputLibraryList.size() >= 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_CastAbout.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
            RV_CastAbout.setLayoutManager(layoutManager);
            SelectInput_FullAdapter adapter = new SelectInput_FullAdapter(getActivity(), selectInputLibraryList);
            RV_CastAbout.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SelectInput_FullAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(),NewInputLibraryActivity.class);
                    intent.putExtra("FGUID",selectInputLibraryList.get(position).getFGuid());
                    ViseLog.i("FGUID"+selectInputLibraryList.get(position).getFGuid());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }
}
