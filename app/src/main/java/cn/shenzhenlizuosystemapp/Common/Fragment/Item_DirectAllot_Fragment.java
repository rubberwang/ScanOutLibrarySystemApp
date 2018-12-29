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

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectDirectAllot_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.DirectAllot.AllotMainActiivty;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class Item_DirectAllot_Fragment extends Fragment {

    private static List<DirectAllotLibraryBill> directAllotLibraryBillList;
    private RecyclerView RV_SearchSelectDirectAllot;
    private ProgressDialog PD;
    private Tools tools;
    private WebService webService;

    public static Item_DirectAllot_Fragment newInstance(List<DirectAllotLibraryBill> SearchDirectAllotList) {
        directAllotLibraryBillList = new ArrayList<DirectAllotLibraryBill>();
        directAllotLibraryBillList.clear();
        Item_DirectAllot_Fragment.directAllotLibraryBillList = SearchDirectAllotList;
        return new Item_DirectAllot_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.direct_allot_fragment, container, false);
        RV_SearchSelectDirectAllot = view.findViewById(R.id.RV_SelectDirectAllot);
        tools = new Tools();
        webService = new WebService(this.getActivity());
        PD = new ProgressDialog(this.getActivity());
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        try {
            ToLoadData();
        } catch (Exception e) {
            ViseLog.d("Item_DirectAllot_Fragment 适配数据异常" + e);
        }
        return view;
    }

    private void ToLoadData() {
        if (directAllotLibraryBillList.size() >= 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_SearchSelectDirectAllot.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
            RV_SearchSelectDirectAllot.setLayoutManager(layoutManager);
            SelectDirectAllot_FullAdapter adapter = new SelectDirectAllot_FullAdapter(getActivity(), directAllotLibraryBillList);
            RV_SearchSelectDirectAllot.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SelectDirectAllot_FullAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(),AllotMainActiivty.class);
                    intent.putExtra("FGUID", directAllotLibraryBillList.get(position).getFGuid());
                    ViseLog.i("FGUID"+ directAllotLibraryBillList.get(position).getFGuid());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    }
}
