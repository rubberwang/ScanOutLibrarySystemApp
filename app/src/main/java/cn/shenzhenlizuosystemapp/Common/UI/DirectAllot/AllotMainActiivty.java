package cn.shenzhenlizuosystemapp.Common.UI.DirectAllot;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_DirectAllotRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.BarCodeCheckTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.AllotDetailTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetLabelTempletBaecodesTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetTagModeTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.StockCellTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.InputBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.GetLabelTempletBaecodesPort;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.Port.TagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.DirectAllotStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.MaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.UI.LoginActivity;
import cn.shenzhenlizuosystemapp.Common.UI.MainTabActivity;
import cn.shenzhenlizuosystemapp.Common.UI.NewInputLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.EditSumDialog;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.AnalyAllXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputLibraryXmlAnalysis;
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
    private MyProgressDialog myProgressDialog;

    private String BillID;
    private int scanTask_directAllotRvIndex = -1;
    private int Sp_DirectAllotLabelIndex = -1;
    private int Sp_DirectAllotNotification_FOutStockCellIndex = -1;
    private int Sp_DirectAllotNotification_FInStockCellIndex = -1;
    private int RefreshStatu = 1;
    private boolean Is_ExistScanValue = false;
    private boolean Is_Single = false;
    private String FBarcodeLib = "";
    private boolean IsSerialNumber = true;
    private boolean IsAddSerialNumber = false;
    private boolean IsSave = false;

    private WebService webService;
    private AllotMainActiivty MContect;
    private Tools tools;
    private ScanTask_DirectAllotRvAdapter scanTask_directAllotRvAdapter;
    private ScanResult_InputRvAdapter scanResult_Input_rvAdapter;
    private EditSumPort editSumPort;

    private List<DirectAllotBodyBean> DirectAllotBodyBeanList;
    private List<MaterialModeBean> MaterialModeBeanList;
    private List<DirectAllotHeadBean> DirectAllotHeadBeanList;
    private List<StockBean> OutStockBeanList;
    private List<StockBean> InStockBeanList;
    private List<ChildTag> ChildTagList;
    private List<String> CheckFGuid = new ArrayList<String>();
    private List<InputSubBodyBean> InputSubmitList = new ArrayList<InputSubBodyBean>();

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
        editSumPort = new EditSumPort() {
            @Override
            public void OnEnSure(String Sum) {
                EditSumDialog.getSingleton().Dismiss();
                SubmitData(Sum);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        };
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
                    if (!Is_ExistScanValue) {
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
                            ChildTagList.clear();
                            scanResult_Input_rvAdapter.notifyDataSetChanged();
                            scanTask_directAllotRvAdapter.setSelection(-1);
                            scanTask_directAllotRvAdapter.notifyDataSetChanged();//取消选中
                        }
                    } else {
                        tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或提交");
                    }
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

                        if (MaterialModeBeanList.get(i).getFBarCoeeCount().equals("1") || MaterialModeBeanList.get(i).getFBarCoeeCount().equals("0")) {
                            Is_Single = true;
                            ZebarTools.getZebarTools().SetZebarDWConfig(MContect, "1", "1");
                            ViseLog.i("Zebar单条码格式");
                        } else {
                            Is_Single = false;
                            ZebarTools.getZebarTools().SetZebarDWConfig(MContect, MaterialModeBeanList.get(i).getFBarCoeeCount(), "3");
                            ViseLog.i("Zebar多条码格式");
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

    /*
     * EventBus触发回调
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(BarCodeMessage msg) {
        if (!Is_ExistScanValue) {
            if (scanTask_directAllotRvAdapter.getselection() >= 0) {
                String data = msg.data;
                ViseLog.i("messageEventBus msg = " + data);
                if (!Is_Single) {
                    data = data.replace("\n", "；");
                    data = data.substring(0, data.length() - 1);
                }
                ViseLog.i("messageEventBus msg = " + data);
                BarCodeCheckPort barCodeCheckPort = new BarCodeCheckPort() {
                    @Override
                    public void onData(String Info) {
                        try {
                            if (!Info.substring(0, 2).equals("EX")) {
                                ViseLog.i("Info = " + Info);
                                InputStream IsBarCodeInfoHead = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                InputStream IsBarCodeInfoBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                List<BarCodeHeadBean> BarCodeInfoHeadList = InputLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                List<BarcodeXmlBean> barcodeXmlBeanList = InputLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_SEQUENCE.toLowerCase())) {
                                    Is_ExistScanValue = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsSerialNumber = false;
                                    IsAddSerialNumber = true;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");
                                } else if (!TextUtils.isEmpty(BarCodeInfoHeadList.get(3).getFQty())) {
                                    //算法  fqty * FUnitRate / baseqty
                                    float ThisPutSum = Tools.StringOfFloat(BarCodeInfoHeadList.get(3).getFQty()) * Tools.StringOfFloat(BarCodeInfoHeadList.get(0).getFUnitRate())
                                            / Tools.StringOfFloat(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFUnitRate());
                                    Is_ExistScanValue = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(String.valueOf(ThisPutSum));
                                    ViseLog.i("有数量 = " + FBarcodeLib);
                                } else if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||
                                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                                    Is_ExistScanValue = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsBarCodeInfoHead.close();
                                    IsBarCodeInfoBody.close();
                                    ShowEditSumDialog(NoInputLibrary());
                                    ViseLog.i("通用号 FBarcodeLib =" + FBarcodeLib);
                                }
                            } else {
                                tools.ShowDialog(MContect, Info.substring(2, Info.length()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ViseLog.i("BarCodeCheckPort Exception = " + e);
                        }
                    }
                };
                BarCodeCheckTask barCodeCheckTask = new BarCodeCheckTask(barCodeCheckPort, webService, DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFMaterial(),
                        MaterialModeBeanList.get(Sp_DirectAllotLabelIndex).getFGuid(), data);
                barCodeCheckTask.execute();
            } else {
                if (!TextUtils.isEmpty(msg.data)) {
                    int InPos = GetSpinnerPos(InStockBeanList, msg.data);
                    if (InPos != -1) {
                        Sp_DirectAllotNotification_FInStockCell.setSelection(InPos);
                    } else if (GetSpinnerPos(OutStockBeanList, msg.data) != -1) {
                        Sp_DirectAllotNotification_FOutStockCell.setSelection(GetSpinnerPos(OutStockBeanList, msg.data));
                    } else {
                        tools.showshort(MContect, "没有找到对应的仓位");
                    }
                }
            }
        } else {
            tools.ShowDialog(MContect, "已经有扫描的数据了，先提交或取消");
        }
    }

    public void PutResultArray(List<BarcodeXmlBean> barcodeXmlBeans) {
        if (barcodeXmlBeans.size() > 0) {
            ChildTagList.clear();
            for (BarcodeXmlBean barcodeXmlBean : barcodeXmlBeans) {
                ChildTag childTag = new ChildTag();
                childTag.setName(barcodeXmlBean.getFBarcodeName());
                childTag.setValue(barcodeXmlBean.getFBarcodeContent());
                ChildTagList.add(childTag);
            }
            scanResult_Input_rvAdapter.notifyDataSetChanged();
        }
    }

    private void ShowEditSumDialog(String Sum) {
        EditSumDialog.getSingleton().Show(MContect, DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSumDialog.getSingleton().Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFUnit_Name());
    }

    public void SubmitData(String InputSum) {
        try {
            if (Is_ExistScanValue) {
                if (!TextUtils.isEmpty(InputSum) && Tools.StringOfFloat(InputSum) > 0) {
                    if (CheckGuid(CheckFGuid, FBarcodeLib)) {
                        IsSerialNumber = true;
                        if (Tools.StringOfFloat(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFNotAllotQty()) >= Tools.StringOfFloat(InputSum)) {
                            if (IsAddSerialNumber) {
                                CheckFGuid.add(FBarcodeLib);
                                IsAddSerialNumber = false;
                            }
                            Sp_DirectAllotNotification_FInStockCell.setEnabled(false);
                            Sp_DirectAllotNotification_FOutStockCell.setEnabled(false);
                            Drawable drawable_border = getResources().getDrawable(R.drawable.border);
                            Sp_DirectAllotNotification_FInStockCell.setBackground(drawable_border);
                            Sp_DirectAllotNotification_FOutStockCell.setBackground(drawable_border);

                            IsSave = true;
                            Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                            TV_DirectAllotSumbit.setBackground(drawable_purple);
                            TV_DirectAllotSumbit.setTextColor(getResources().getColor(R.color.White));

                            InputSubBodyBean subBodyBean = new InputSubBodyBean();
                            subBodyBean.setFBillBodyID(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFGuid());
                            subBodyBean.setFBarcodeLib(FBarcodeLib);
                            subBodyBean.setInputLibrarySum(InputSum);
                            InputSubmitList.add(subBodyBean);

                            float Sum = Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFThisAuxQty()) +
                                    Tools.StringOfFloat(InputSum);
                            String SetFThisAuxQty = String.valueOf(Sum);
                            inputTaskRvDataList.get(RV_ScanInfoTableIndex).setFThisAuxQty(SetFThisAuxQty);

                            //为最后生成入库单保存数据array
                            float NewNoPut = Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFAuxQty()) - (Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFExecutedAuxQty()) +
                                    Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFThisAuxQty()));
                            String SetNoInput = String.valueOf(NewNoPut);
                            inputTaskRvDataList.get(RV_ScanInfoTableIndex).setNoInput(SetNoInput);

                            if (Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getNoInput()) <= 0) {
                                scanTask_Input_rvAdapter.setSelection(-1);
                            }
                            scanTask_Input_rvAdapter.notifyDataSetChanged();
                            Is_InputNumber_Mode = false;
                        } else {
                            tools.ShowDialog(MContect, "提交数量不能大于未收数量");
                        }
                    } else {
                        tools.ShowDialog(MContect, "此条码已经扫描过了");
                    }
                } else {
                    tools.ShowDialog(MContect, "入库数量为空或小于0");
                }
            }
        } catch (Exception e) {
            ViseLog.i("SubmitData Exception = " + e);
            tools.ShowDialog(MContect, "提交错误：" + e.getMessage());
        }
    }

    public String NoInputLibrary() {
        if (!TextUtils.isEmpty(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFAuxQty()) && !TextUtils.isEmpty(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFExecutedAuxQty())) {
            float noSend = Tools.StringOfFloat(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFAuxQty()) - (Tools.StringOfFloat(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFExecutedAuxQty()) +
                    Tools.StringOfFloat(DirectAllotBodyBeanList.get(scanTask_directAllotRvIndex).getFThisAuxQty()));
            String NoSendSum = String.valueOf(noSend);
            ViseLog.i("NoInputLibrary = " + NoSendSum);
            return NoSendSum;
        }
        return "";
    }


    public boolean CheckGuid(List<String> Check, String Result) {
        if (IsSerialNumber) {
            return true;
        }
        if (Tools.IsObjectNull(Check) && !TextUtils.isEmpty(Result)) {
            if (!Check.contains(Result)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void CleanData() {
        Is_InputNumber_Mode = false;
        childTagList.clear();
        scanResult_Input_rvAdapter.notifyDataSetChanged();
        scanTask_Input_rvAdapter.setSelection(-1);
        scanTask_Input_rvAdapter.notifyDataSetChanged();
        ViseLog.i("Click CleanData");
    }
}
