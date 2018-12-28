package cn.shenzhenlizuosystemapp.Common.UI.DirectAllot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_DirectAllotRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.AllotDetailTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetLabelTempletBaecodesTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetTagModeTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.StockCellTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.InputBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Port.GetLabelTempletBaecodesPort;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.Port.TagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.DirectAllotStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.MaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.LoginActivity;
import cn.shenzhenlizuosystemapp.Common.UI.MainTabActivity;
import cn.shenzhenlizuosystemapp.Common.UI.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.AnalyAllXml;
import cn.shenzhenlizuosystemapp.R;

public class AllotMainActiivty extends BaseActivity {

    private TextView TV_DeliverGoodsNumber;
    private TextView TV_BusType;
    private TextView TV_DirectAllotSumbit;
    private TextView TV_DirectAllotCancel;
    private TextView Tv_DirectAllotNotification_FOutStock;
    private Spinner Sp_DirectAllotNotification_FOutStockCell;
    private TextView Tv_DirectAllotNotification_FInStock;
    private Spinner Sp_DirectAllotNotification_FInStockCell;
    private Spinner Sp_DirectAllotLabel;
    private RecyclerView RV_ScanInfoTable;
    private RecyclerView RV_ResInfoTable;
    private EditText Et_ScanNumber;
    private MyProgressDialog myProgressDialog;

    private String BillID;
    private int scanTask_directAllotRvIndex = -1;
    private int Sp_DirectAllotLabelIndex = -1;
    private int Sp_DirectAllotNotification_FOutStockCellIndex = -1;
    private int Sp_DirectAllotNotification_FInStockCellIndex = -1;
    private int RefreshStatu = 1;

    private WebService webService;
    private AllotMainActiivty MContect;
    private Tools tools;
    private ScanTask_DirectAllotRvAdapter scanTask_directAllotRvAdapter;
    private ScanResult_InputRvAdapter scanResult_Input_rvAdapter;

    private List<DirectAllotBodyBean> DirectAllotBodyBeanList;
    private List<MaterialModeBean> MaterialModeBeanList;
    private List<DirectAllotHeadBean> DirectAllotHeadBeanList;
    private List<StockBean> OutStockBeanList;
    private List<StockBean> InStockBeanList;
    private List<ChildTag> ChildTagList;

    private int GetSpinnerPos(List<StockBean> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFName().equals(value)) {
                return i;
            }
        }
        return -1;
    }

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
        Tv_DirectAllotNotification_FInStock = $(R.id.Tv_DirectAllotNotification_FInStock);
        Sp_DirectAllotNotification_FInStockCell = $(R.id.Sp_DirectAllotNotification_FInStockCell);
        Sp_DirectAllotNotification_FOutStockCell = $(R.id.Sp_DirectAllotNotification_FOutStockCell);
        Tv_DirectAllotNotification_FOutStock = $(R.id.Tv_DirectAllotNotification_FOutStock);
        Sp_DirectAllotLabel = $(R.id.Sp_DirectAllotLabel);
        RV_ScanInfoTable = $(R.id.RV_ScanInfoTable);
        RV_ResInfoTable = $(R.id.RV_ResInfoTable);
        Et_ScanNumber = $(R.id.Et_ScanNumber);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }

    private void GetAllotDetail() {
        myProgressDialog.ShowPD("加载数据中...");
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
                                InitTaskRv();
                                myProgressDialog.dismiss();
                            }
                        };
                        InputStream IS_DirectAllotBody = new ByteArrayInputStream(res.getBytes("UTF-8"));
                        AnalyAllXml.getSingleton().GetDirectAllotDetailBodyInfo(IS_DirectAllotBody, directPortDetailBodyXmlPort);
                        DirectPortDetailHeadXmlPort directPortDetailHeadXmlPort = new DirectPortDetailHeadXmlPort() {
                            @Override
                            public void OnHead(List<DirectAllotHeadBean> directAllotHeadBeanList) {
                                DirectAllotHeadBeanList = directAllotHeadBeanList;
                                TV_DeliverGoodsNumber.setText(directAllotHeadBeanList.get(0).getFCode());
                                TV_BusType.setText(directAllotHeadBeanList.get(0).getFTransactionType_Name());
                                Tv_DirectAllotNotification_FOutStock.setText(directAllotHeadBeanList.get(0).getFOutStock_Name());
                                Tv_DirectAllotNotification_FInStock.setText(directAllotHeadBeanList.get(0).getFInStock_Name());
                                InitSotckCellSp();
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

    private void InitTaskRv() {
        DirectAllotBodyBeanList = DisposeTaskTableList(DirectAllotBodyBeanList);
        LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_ScanInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_ScanInfoTable.setLayoutManager(ScanTaskL);
        scanTask_directAllotRvAdapter = new ScanTask_DirectAllotRvAdapter(this, DirectAllotBodyBeanList);
        RV_ScanInfoTable.setAdapter(scanTask_directAllotRvAdapter);
        scanTask_directAllotRvAdapter.setOnItemClickLitener(new ScanTask_DirectAllotRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (Tools.StringOfFloat(DirectAllotBodyBeanList.get(position).getFAuxQty()) <= Tools.StringOfFloat(DirectAllotBodyBeanList.get(position).getFThisAuxQty()) +
                        Tools.StringOfFloat(DirectAllotBodyBeanList.get(position).getFExecutedAuxQty())) {
                    tools.ShowDialog(MContect, "这张单已扫描完成");
                } else {
//                    if (!Is_InputNumber_Mode) {
                    if (scanTask_directAllotRvAdapter.getselection() != position) {
                        LockResultPort lockResultPort = new LockResultPort() {
                            @Override
                            public void onStatusResult(String res) {
                                myProgressDialog.dismiss();
                                if (res.equals("Success")) {
                                    if (scanTask_directAllotRvIndex != position) {
                                        scanTask_directAllotRvIndex = position;
                                    }
                                    scanTask_directAllotRvAdapter.setSelection(position);
                                    scanTask_directAllotRvAdapter.notifyDataSetChanged();//选中
                                    InitTagTemplateSp(position);
                                } else {
                                    tools.ShowDialog(MContect, res);
                                }
                            }
                        };
                        InputBodyLockTask inputBodyLockTask = new InputBodyLockTask(lockResultPort, webService, DirectAllotBodyBeanList.get(position).getFGuid(), myProgressDialog);
                        inputBodyLockTask.execute();
                    } else {
//                            childTagList.clear();
//                            scanResult_Input_rvAdapter.notifyDataSetChanged();
                        scanTask_directAllotRvAdapter.setSelection(-1);
                        scanTask_directAllotRvAdapter.notifyDataSetChanged();//取消选中
                    }
//                    } else {
////                        tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或提交");
////                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitSotckCellSp() {
        DirectStockCellPort directStockCellPort = new DirectStockCellPort() {
            @Override
            public void OnOutRes(List<StockBean> Out_StockBeanList) {
                if (Out_StockBeanList.size() > 0 && Tools.IsObjectNull(Out_StockBeanList)) {
                    OutStockBeanList = Out_StockBeanList;
                    DirectAllotStockAdapter InputStockAdapter = new DirectAllotStockAdapter(Out_StockBeanList, MContect);
                    Sp_DirectAllotNotification_FOutStockCell.setAdapter(InputStockAdapter);
                    Sp_DirectAllotNotification_FOutStockCell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (Sp_DirectAllotNotification_FOutStockCellIndex != i) {
                                Sp_DirectAllotNotification_FOutStockCellIndex = i;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    ViseLog.i("OnOutRes = NUll");
                }
            }

            @Override
            public void OnInRes(List<StockBean> In_StockBeanList) {
                if (In_StockBeanList.size() > 0 && Tools.IsObjectNull(In_StockBeanList)) {
                    InStockBeanList = In_StockBeanList;
                    DirectAllotStockAdapter InputStockAdapter = new DirectAllotStockAdapter(In_StockBeanList, MContect);
                    Sp_DirectAllotNotification_FInStockCell.setAdapter(InputStockAdapter);
                    Sp_DirectAllotNotification_FInStockCell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (Sp_DirectAllotNotification_FInStockCellIndex != i) {
                                Sp_DirectAllotNotification_FInStockCellIndex = i;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    ViseLog.i("OnInRes = NUll");
                }
            }
        };
        for (int i = 1; i < 3; i++) {
            if (i == 1) {
                StockCellTask stockCellTask = new StockCellTask(directStockCellPort, webService, DirectAllotHeadBeanList.get(0).getFOutStock(), i);
                stockCellTask.execute();
            } else {
                StockCellTask stockCellTask = new StockCellTask(directStockCellPort, webService, DirectAllotHeadBeanList.get(0).getFInStock(), i);
                stockCellTask.execute();
            }
        }
    }

    private MyHandler AllotHandle = new MyHandler(AllotMainActiivty.this);

    class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<AllotMainActiivty> weakReference;

        public MyHandler(AllotMainActiivty AllotMainActiivty) {
            weakReference = new WeakReference<>(AllotMainActiivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AllotMainActiivty handlerMemoryActivity = weakReference.get();
            if (handlerMemoryActivity != null) {
                switch (msg.what) {
                    case 1: {
                        break;
                    }
                }
            } else {
                ViseLog.i("没有得到Activity实例不进行操作");
            }
        }
    }

    private List<DirectAllotBodyBean> DisposeTaskTableList(List<DirectAllotBodyBean> directAllotBodyBeanList) {
        List<DirectAllotBodyBean> removeList = new ArrayList<DirectAllotBodyBean>();
        for (int index = 0; index < directAllotBodyBeanList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(directAllotBodyBeanList.get(index).getFAuxQty()) && !TextUtils.isEmpty(directAllotBodyBeanList.get(index).getFExecutedAuxQty())) {
                    float AuxQty = Tools.StringOfFloat(directAllotBodyBeanList.get(index).getFAuxQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(directAllotBodyBeanList.get(index).getFExecutedAuxQty());
                    NoSendQty = String.valueOf(AuxQty - ExecutedAuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(directAllotBodyBeanList.get(index));
                    continue;
                }
                if (!TextUtils.isEmpty(directAllotBodyBeanList.get(index).getFIsClosed())) {
                    if (directAllotBodyBeanList.get(index).getFIsClosed().equals("1")) {
                        removeList.add(directAllotBodyBeanList.get(index));
                        continue;
                    }
                }
            }
            if (!TextUtils.isEmpty(directAllotBodyBeanList.get(index).getFAuxQty()) &&
                    !TextUtils.isEmpty(directAllotBodyBeanList.get(index).getFExecutedAuxQty())) {
                AddAuxQty = Tools.StringOfFloat(directAllotBodyBeanList.get(index).getFAuxQty());
                AddExecutedAuxQty = Tools.StringOfFloat(directAllotBodyBeanList.get(index).getFExecutedAuxQty());
                AddNoSendQty = String.valueOf(AddAuxQty - AddExecutedAuxQty);
            }
            directAllotBodyBeanList.get(index).setFNotAllotQty(AddNoSendQty);
            directAllotBodyBeanList.get(index).setFAuxQty(String.valueOf(AddAuxQty));
            directAllotBodyBeanList.get(index).setFExecutedAuxQty(String.valueOf(AddExecutedAuxQty));
        }
        directAllotBodyBeanList.removeAll(removeList);
        return directAllotBodyBeanList;
    }

    private void InitTagTemplateSp(int pos) {
        TagModePort tagModePort = new TagModePort() {
            @Override
            public void OnTagRes(List<MaterialModeBean> materialModeBeanList) {
                MaterialModeBeanList = materialModeBeanList;
                MaterialModeAdapter InputStockAdapter = new MaterialModeAdapter(materialModeBeanList, MContect);
                Sp_DirectAllotLabel.setAdapter(InputStockAdapter);
                Sp_DirectAllotLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (Sp_DirectAllotLabelIndex != i) {
                            Sp_DirectAllotLabelIndex = i;
                        }
                        GetLabelTempletBaecodes(MaterialModeBeanList.get(i).getFGuid());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        };
        GetTagModeTask getTagModeTask = new GetTagModeTask(tagModePort, webService, DirectAllotBodyBeanList.get(pos).getFMaterial());
        getTagModeTask.execute();
    }

    private void GetLabelTempletBaecodes(String LabelTempletBaecodeID) {
        GetLabelTempletBaecodesPort getLabelTempletBaecodesPort = new GetLabelTempletBaecodesPort() {
            @Override
            public void OnResult(List<ChildTag> childTagList) {
                if (childTagList.size() > 0) {
                    ChildTagList = childTagList;
                    if (RefreshStatu == 1) {
                        RefreshStatu = 2;
                        InitRecycler();
                    } else {
                        scanResult_Input_rvAdapter = new ScanResult_InputRvAdapter(MContect, childTagList);
                        RV_ResInfoTable.setAdapter(scanResult_Input_rvAdapter);
                    }
                }
            }

            @Override
            public void OnError(String ErrotInfo) {
                if (!TextUtils.isEmpty(ErrotInfo)) {
                    scanTask_directAllotRvAdapter.setSelection(-1);
                    scanTask_directAllotRvAdapter.notifyDataSetChanged();//取消选中
                    tools.ShowDialog(MContect, ErrotInfo);
                }
            }
        };
        GetLabelTempletBaecodesTask getLabelTempletBaecodesTask = new GetLabelTempletBaecodesTask(getLabelTempletBaecodesPort, webService, LabelTempletBaecodeID);
        getLabelTempletBaecodesTask.execute();
    }

    private void InitRecycler() {
        if (Tools.IsObjectNull(ChildTagList)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_ResInfoTable.setLayoutManager(layoutManager);
            scanResult_Input_rvAdapter = new ScanResult_InputRvAdapter(this, ChildTagList);
            RV_ResInfoTable.setAdapter(scanResult_Input_rvAdapter);
        }
    }
}
