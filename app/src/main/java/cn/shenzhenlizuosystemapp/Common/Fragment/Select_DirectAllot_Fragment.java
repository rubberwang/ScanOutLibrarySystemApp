package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectDirectAllot_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.DirectAllot.AllotMainActiivty;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.DirectAllotLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class Select_DirectAllot_Fragment extends Fragment {

    public static final String ARGS_PAGE = "Select_DirectAllot_Page";
    private RecyclerView RV_SelectDirectAllot;
    private WebService webService;
    private Tools tools;
    private ProgressDialog PD;
    private List<DirectAllotLibraryBill> directAllotLibraryBillList;

    public static Select_DirectAllot_Fragment newInstance() {
        Select_DirectAllot_Fragment fragment = new Select_DirectAllot_Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.direct_allot_fragment, container, false);
        RV_SelectDirectAllot = rootView.findViewById(R.id.RV_SelectDirectAllot);
        tools = new Tools();
        webService = new WebService(this.getActivity());
        PD = new ProgressDialog(this.getActivity());
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        GetOutLibraryBills();
        return rootView;
    }

    private void GetOutLibraryBills() {
        GetInputLibraryBillsAsyncTask getInputLibraryBillsAsyncTask = new GetInputLibraryBillsAsyncTask(RV_SelectDirectAllot);
        getInputLibraryBillsAsyncTask.execute();
    }

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<DirectAllotLibraryBill>> {

        private RecyclerView recyclerView;

        public GetInputLibraryBillsAsyncTask(RecyclerView recyclerView) {
            super();
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<DirectAllotLibraryBill> doInBackground(Integer... params) {
            String DirectAllotBills = "";
            try {
                InputStream in_withcode = null;
                DirectAllotBills = webService.GetAdjustStockNoticeBillsList(ConnectStr.ConnectionToString, "0");
                ViseLog.i("DirectAllotBills = " + DirectAllotBills);
                in_withcode = new ByteArrayInputStream(DirectAllotBills.getBytes("UTF-8"));
                List<AdapterReturn> ResultXmlList = AnalysisReturnsXml.getSingleton().GetReturn(in_withcode);
                in_withcode.close();
                if (ResultXmlList.get(0).getFStatus().equals("1")) {
                    InputStream inputInfoStream = new ByteArrayInputStream(ResultXmlList.get(0).getFInfo().getBytes("UTF-8"));
                    ViseLog.i("ResultXmlList.get(0).getFInfo() = " + ResultXmlList.get(0).getFInfo());
                    directAllotLibraryBillList = DirectAllotLibraryXmlAnalysis.getSingleton().GetDirectAllotNotification(inputInfoStream);
                    inputInfoStream.close();
                } else {
                    directAllotLibraryBillList.clear();
                }
            } catch (Exception e) {
                ViseLog.d("GetInputLibraryBillsAsyncTask Exception " + e);
            }
            return directAllotLibraryBillList;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(final List<DirectAllotLibraryBill> result) {
            try {
                PD.dismiss();
                if (result.size() >= 0) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
                    recyclerView.setLayoutManager(layoutManager);
                    SelectDirectAllot_FullAdapter adapter = new SelectDirectAllot_FullAdapter(getActivity(), result);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickLitener(new SelectDirectAllot_FullAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), AllotMainActiivty.class);
                            intent.putExtra("FGUID", result.get(position).getFGuid());
                            ViseLog.i("DirectAllot FGUID" + result.get(position).getFGuid());
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                        }
                    });
                }
            } catch (Exception e) {
                ViseLog.d("DirectAllot Select适配RV数据错误" + e);
                tools.showshort(getActivity(), "出库数据加载错误，请重新打开");
            }
            ViseLog.i("DirectAllot 出库单返回数据" + result);
        }

        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            PD.setTitle("正在获取数据请稍后...");
            PD.show();
        }
    }

    public List<DirectAllotLibraryBill> GetSelectBills() {
        return this.directAllotLibraryBillList;
    }
}
