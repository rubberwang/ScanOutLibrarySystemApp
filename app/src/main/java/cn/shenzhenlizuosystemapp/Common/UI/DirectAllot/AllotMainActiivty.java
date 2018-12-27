package cn.shenzhenlizuosystemapp.Common.UI.DirectAllot;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.AllotDetailTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;
import cn.shenzhenlizuosystemapp.Common.UI.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.AnalyAllXml;
import cn.shenzhenlizuosystemapp.R;

public class AllotMainActiivty extends BaseActivity {

    private TextView TV_DeliverGoodsNumber;
    private TextView TV_BusType;
    private TextView TV_DirectAllotSumbit;
    private TextView TV_DirectAllotCancel;
    private TextView TV_DirectAllotSave;
    private TextView TV_DirectAllotClear;
    private TextView Tv_DirectAllotNotification_FOutStock;
    private Spinner Sp_DirectAllotNotification_FOutStockCell;
    private TextView Tv_DirectAllotNotification_FInStock;
    private Spinner Sp_DirectAllotNotification_FInStockCell;
    private Spinner Sp_DirectAllotLabel;
    private RecyclerView RV_ScanInfoTable;
    private RecyclerView RV_ResInfoTable;
    private EditText Et_ScanNumber;

    private String BillID;

    private WebService webService;
    private AllotMainActiivty MContect;
    private Tools tools;

    private List<DirectAllotBodyBean> DirectAllotBodyBeanList;

    @Override
    protected int inflateLayout() {
        return R.layout.allot_main_layout;
    }

    @Override
    public void initData() {
        BillID = getIntent().getStringExtra("FGUID");
        MContect = new WeakReference<>(AllotMainActiivty.this).get();
        webService = WebService.getSingleton(MContect);
        tools = Tools.getTools();
        GetAllotDetail();
    }

    @Override
    public void initView() {
        TV_DeliverGoodsNumber = $(R.id.TV_DeliverGoodsNumber);
        TV_BusType = $(R.id.TV_BusType);
        TV_DirectAllotSumbit = $(R.id.TV_DirectAllotSumbit);
        TV_DirectAllotCancel = $(R.id.TV_DirectAllotCancel);
        TV_DirectAllotSave = $(R.id.TV_DirectAllotSave);
        TV_DirectAllotClear = $(R.id.TV_DirectAllotClear);
        Tv_DirectAllotNotification_FInStock = $(R.id.Tv_DirectAllotNotification_FInStock);
        Sp_DirectAllotNotification_FInStockCell = $(R.id.Sp_DirectAllotNotification_FInStockCell);
        Sp_DirectAllotNotification_FOutStockCell = $(R.id.Sp_DirectAllotNotification_FOutStockCell);
        Tv_DirectAllotNotification_FOutStock = $(R.id.Tv_DirectAllotNotification_FOutStock);
        Sp_DirectAllotLabel = $(R.id.Sp_DirectAllotLabel);
        RV_ResInfoTable = $(R.id.RV_ResInfoTable);
        RV_ResInfoTable = $(R.id.RV_ResInfoTable);
        Et_ScanNumber = $(R.id.Et_ScanNumber);
    }

    private void GetAllotDetail() {
        AllotDetailPort allotDetailPort = new AllotDetailPort() {
            @Override
            public void OnResult(String res) {
                if (res.substring(0, 2).equals("EX")) {
                    tools.ShowDialog(MContect, "加载错误：" + res.substring(2, res.length()));
                    ViseLog.i("AllotBadInfo = " + res.substring(2, res.length()));
                } else {
                    try {
                        ViseLog.i("AllotSuccessInfo = " + res);
                        DirectPortDetailBodyXmlPort directPortDetailBodyXmlPort = new DirectPortDetailBodyXmlPort() {
                            @Override
                            public void OnBody(List<DirectAllotBodyBean> directAllotBodyBeanList) {
                                DirectAllotBodyBeanList = directAllotBodyBeanList;
                            }
                        };
                        InputStream IS_DirectAllotBody = new ByteArrayInputStream(res.getBytes("UTF-8"));
                        AnalyAllXml.getSingleton().GetDirectAllotDetailBodyInfo(IS_DirectAllotBody, directPortDetailBodyXmlPort);
                        DirectPortDetailHeadXmlPort directPortDetailHeadXmlPort = new DirectPortDetailHeadXmlPort() {
                            @Override
                            public void OnHead(List<DirectAllotHeadBean> directAllotHeadBeanList) {
                                TV_DeliverGoodsNumber.setText(directAllotHeadBeanList.get(0).getFCode());
                                TV_BusType.setText(directAllotHeadBeanList.get(0).getFTransactionType_Name());
                                Tv_DirectAllotNotification_FOutStock.setText(directAllotHeadBeanList.get(0).getFOutStock_Name());
                                Tv_DirectAllotNotification_FInStock.setText(directAllotHeadBeanList.get(0).getFInStock_Name());
                            }
                        };
                        InputStream IS_DirectAllotHead = new ByteArrayInputStream(res.getBytes("UTF-8"));
                        AnalyAllXml.getSingleton().GetDirectAllotDetailHeadInfo(IS_DirectAllotHead, directPortDetailHeadXmlPort);
                        IS_DirectAllotHead.close();
                        IS_DirectAllotBody.close();
                    } catch (Exception e) {

                    }
                }
            }
        };
        AllotDetailTask allotDetailTask = new AllotDetailTask(allotDetailPort, webService, BillID, "0");
        allotDetailTask.execute();
    }
}
