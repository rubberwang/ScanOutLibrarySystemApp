package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.ScanResult_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.ScanTask_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.Subbody_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBarCodeCheckTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBillCreateTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.UnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckBodyMaterial;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckBodyStocks;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckMaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStock_Return;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.CheckBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckBillCreate;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckMaterialAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckMaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSubBodyStocksAdapter;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeListActivity;
import cn.shenzhenlizuosystemapp.Common.View.EditSumDialog;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckLibraryAllInfo;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckStockXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckTagModeAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class CheckLibraryActivity extends BaseActivity {

    //标示符
    private String FGUID = "";
    private int RV_ScanInfoTableIndex = 0;
    private int SpHouseIndex = 0;
    private int SpQuitHouseSpaceIndex = 0;
    private int Sp_LabelModeIndex = 0;
    private boolean Is_QuitNumber_Mode = false;
    private String FBarcodeLib = "";
    private String ILSum = "";
    private int RefreshStatu = 1;
    private boolean Is_Single = false;
    private String HeadID = "";
    private String FbillbodyID = "";
    private String IsAllow = "";
    private boolean IsSerialNumber = true;
    private boolean IsAddSerialNumber = false;
    private boolean IsSave = false;

    //数组
    private List<ChildCheckTag> childQuitTagList = null;
    private List<CheckLibraryDetail> inputLibraryBills = null;
    private List<CheckTaskRvData> quitTaskRvDataList = null;
    private List<CheckSubBody> checkSubbodyRvDataList = null;
    private List<CheckStockBean> stockBeans = null;
    private List<CheckStockBean> stockBeanList = null;
    private List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<CheckMaterialModeBean>();
    private List<String> CheckFGuid = new ArrayList<String>();
    private List<CheckSubBody> checkSubbodyRvDataList2 = null;
    private List<CheckStockBean> SubBodyStocksList = null;
    private List<CheckBodyMaterial> SubBodyMaterialList = null;
    private List<CheckBodyMaterial> SubBodyMaterial = null;


    //类
    private Context MContect;
    private Tools tools = null;
    private QuitLibraryObServer quitLibraryObServer;
    private WebService webService;
    private ScanResult_CheckRvAdapter scanResult_Quit_rvAdapter;
    private ScanTask_CheckRvAdapter scanTask_quit_rvAdapter;
    private Subbody_CheckRvAdapter subbody_CheckRvAdapter;
    private EditSumPort editSumPort;
    private CheckSubBodyStocksAdapter checkSubBodyStocksAdapter;

    //控件
    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private Spinner Sp_house;
    private Spinner spinnerScannerDevices;
    private Spinner Sp_QuitHouseSpace;
    private Spinner Sp_Material;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private TextView TV_Scaning;
    private TextView TV_Cancel;
    private TextView TV_Sumbit;
    private TextView TV_Save;
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;
    private RecyclerView RV_SubBodyInfoTable;
    private Spinner Sp_Label;
    private MyProgressDialog myProgressDialog;
    private EditText ET_SuckUp;
    private ImageButton BT_CheckMaterial;

    private int GetSpinnerPos(List<CheckStockBean> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFName().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private int GetSpinnerPos1(List<CheckBodyStocks> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            return i;
        }
        return 0;
    }

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_check_layout;
    }

    @Override
    public void initData() {
        MContect = new WeakReference<>(CheckLibraryActivity.this).get();
        EventBus.getDefault().register(MContect);
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        tools = Tools.getTools();
        tools.PutStringData("CheckLibraryActivityFGUID", FGUID, tools.InitSharedPreferences(MContect));
        quitLibraryObServer = new QuitLibraryObServer();
        getLifecycle().addObserver(quitLibraryObServer);
        webService = WebService.getSingleton(MContect);
        InitClick();
        GetQuitLibraryBillsAsyncTask getQuitLibraryBillsAsyncTask = new GetQuitLibraryBillsAsyncTask();
        getQuitLibraryBillsAsyncTask.execute();
        Drawable drawable = getResources().getDrawable(R.drawable.circularbead_gray);
        TV_Sumbit.setBackground(drawable);
        TV_Sumbit.setTextColor(getResources().getColor(R.color.Black));
        TV_Save.setBackground(drawable);
        TV_Save.setTextColor(getResources().getColor(R.color.Black));
        editSumPort = new EditSumPort() {
            @Override
            public void OnEnSure(String Sum) {
                if (true) {
                    EditSumDialog.getSingleton().Dismiss();
                    SubmitData(Sum);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                } else {
                    EditSumDialog.getSingleton().ShowErrorInfo("输入数据有误");
                }
            }
        };
        ET_SuckUp.setFocusable(true);
        ET_SuckUp.setFocusableInTouchMode(true);
        ET_SuckUp.requestFocus();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        TV_DeliverGoodsNumber = $(R.id.TV_DeliverGoodsNumber);
        Sp_house = $(R.id.Sp_house);
        //TV_BusType = $(R.id.TV_BusType);
        //TV_Unit = $(R.id.TV_Unit);
        RV_GetInfoTable = $(R.id.RV_GetInfoTable);
        RV_ScanInfoTable = $(R.id.RV_ScanInfoTable);
        RV_SubBodyInfoTable = $(R.id.RV_SubBodyInfoTable);
        spinnerScannerDevices = $(R.id.spinnerScannerDevices);
        Sp_QuitHouseSpace = $(R.id.Sp_QuitHouseSpace);
        Sp_Material = $(R.id.Sp_Material);
        TV_Cancel = $(R.id.TV_Cancel);
        TV_Sumbit = $(R.id.TV_Sumbit);
        TV_Save = $(R.id.TV_Save);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
        Sp_Label = $(R.id.Sp_Label);
        ET_SuckUp = $(R.id.ET_SuckUp);
        BT_CheckMaterial = $(R.id.BT_CheckMaterial);
    }

    private void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (Tools.IsObjectNull(childQuitTagList)) {
                    if (childQuitTagList.size() > 0) {
                        tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                                ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                            }
                        }, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                            }
                        }, false);
                    } else {
                        ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                    }
                } else {
                    ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                }
            }
        });

        TV_Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Tools.IsObjectNull(childQuitTagList)) {
                    if (childQuitTagList.size() > 0) {
                        tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                                ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                            }
                        }, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                            }
                        }, false);
                    } else {
                        ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                    }
                } else {
                    ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                }
            }
        });

        TV_Sumbit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (IsSave) {
                    tools.ShowOnClickDialog(MContect, "确认保存吗？", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            CreateBillData();
                            tools.DisappearDialog();
                        }
                    }, new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            tools.DisappearDialog();
                        }
                    }, false);
                }
            }
        });

        BT_CheckMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckLibraryActivity.this,TreeListActivity.class);
                startActivity(intent);
            }
        });

        TV_Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (IsSave) {
                    tools.ShowOnClickDialog(MContect, "确认结案吗？", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            CreateBillData();
                            tools.DisappearDialog();
                        }
                    }, new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            tools.DisappearDialog();
                        }
                    }, false);
                }
            }
        });
    }



    /***
     * 标签模版列表适配
     * ***/
    private void InitRecycler() {
        if (Tools.IsObjectNull(childQuitTagList)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_GetInfoTable.setLayoutManager(layoutManager);
            scanResult_Quit_rvAdapter = new ScanResult_CheckRvAdapter(this, childQuitTagList);
            RV_GetInfoTable.setAdapter(scanResult_Quit_rvAdapter);
        }
    }

    /***
     * 子分录列表适配
     * ***/
    private void InitSubbodyRecycler() {
        if (Tools.IsObjectNull(checkSubbodyRvDataList2)) {
            LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
            ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
            RV_SubBodyInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
            RV_SubBodyInfoTable.setLayoutManager(ScanTaskL);
            subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(this, checkSubbodyRvDataList2);
            RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
        }
    }

    /***
     * 扫描任务列表适配
     * ***/
    private void InitScanRecycler() {
        LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_ScanInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_ScanInfoTable.setLayoutManager(ScanTaskL);
        scanTask_quit_rvAdapter = new ScanTask_CheckRvAdapter(this, quitTaskRvDataList);
        RV_ScanInfoTable.setAdapter(scanTask_quit_rvAdapter);
        scanTask_quit_rvAdapter.setOnItemClickLitener(new ScanTask_CheckRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
//                if (Integer.parseInt(quitTaskRvDataList.get(position).getFAccountQty().split("\\.")[0]) <= Integer.parseInt(quitTaskRvDataList.get(position).getFCheckQty().split("\\.")[0])) {
//                    tools.ShowDialog(MContect, "这张单已扫描完成");
//                } else {
                    if (!Is_QuitNumber_Mode) {
                        if (scanTask_quit_rvAdapter.getselection() != position) {
                            LockResultPort lockResultPort = new LockResultPort() {
                                @Override
                                public void onStatusResult(String res) {
                                    myProgressDialog.dismiss();
                                    if (res.equals("Success")) {
                                        if (RV_ScanInfoTableIndex != position) {
                                            RV_ScanInfoTableIndex = position;
                                        }
                                        quitTaskRvDataList = SumList(quitTaskRvDataList, checkSubbodyRvDataList, position);
                                        checkSubbodyRvDataList2 = quitTaskRvDataList.get(position).getCheckSubBody();
                                        GetMaterialMode getMaterialMode = new GetMaterialMode();
                                        getMaterialMode.execute(quitTaskRvDataList.get(position).getFMaterial());
                                        scanTask_quit_rvAdapter.setSelection(position);
                                        scanTask_quit_rvAdapter.notifyDataSetChanged();//选中
                                    } else {
                                        tools.ShowDialog(MContect, res);
                                    }
                                }
                            };
                            CheckBodyLockTask quitBodyLockTask = new CheckBodyLockTask(lockResultPort, webService, quitTaskRvDataList.get(position).getFGuid(), myProgressDialog);
                            quitBodyLockTask.execute();
                        } else {
                            childQuitTagList.clear();
                            scanResult_Quit_rvAdapter.notifyDataSetChanged();
                            scanTask_quit_rvAdapter.setSelection(-1);
                            scanTask_quit_rvAdapter.notifyDataSetChanged();//取消选中
                            checkSubbodyRvDataList2.clear();
                            subbody_CheckRvAdapter.notifyDataSetChanged();
                        }
                    } else {
                        tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或提交");
                    }

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitSp(List<CheckStockBean> stockBeans, String StockName) {
        if (stockBeans.size() >= 0) {
            CheckStockAdapter QuitStockAdapter = new CheckStockAdapter(stockBeans, CheckLibraryActivity.this);
            Sp_house.setAdapter(QuitStockAdapter);
            int Pos = GetSpinnerPos(stockBeans, StockName);
            Sp_house.setSelection(Pos);
            Sp_house.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (SpHouseIndex != i) {
                        SpHouseIndex = i;
                    }
                    AsyncGetStocksCell asyncGetStocksCell = new AsyncGetStocksCell(i);
                    asyncGetStocksCell.execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }


    class QuitLibraryObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
            ZebarTools.getZebarTools().SetZebarDWConfig(MContect, "1", "1");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {
            UnlockTask unlockTask = new UnlockTask(webService);
            unlockTask.execute();
            EditSumDialog.getSingleton().Dismiss();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            EventBus.getDefault().unregister(MContect);
        }
    }

    private class AsyncGetStocksCell extends AsyncTask<String, Void, List<CheckStockBean>> {

        private int pos = 0;

        AsyncGetStocksCell(int pos) {
            this.pos = pos;
        }

        @Override
        protected List<CheckStockBean> doInBackground(String... params) {
            stockBeanList = new ArrayList<>();
            try {
                String StocksCell = webService.GetStocksCell(ConnectStr.ConnectionToString, stockBeans.get(pos).getFGuid());
                InputStream inStockCell = new ByteArrayInputStream(StocksCell.getBytes("UTF-8"));
                List<CheckAdapterReturn> stock_returns = CheckAnalysisReturnsXml.getSingleton().GetReturn(inStockCell);
                if (false) {
                    InputStream In_Info = new ByteArrayInputStream(stock_returns.get(0).getFInfo().getBytes("UTF-8"));
                    stockBeanList = CheckStockXmlAnalysis.getSingleton().GetXmlStockInfo(In_Info);
                } else {
                    stockBeanList = SubBodyStocksList;
                }
                inStockCell.close();
            } catch (Exception e) {
                ViseLog.i("AsyncGetStocksCellException = " + e.getMessage());
            }
            return stockBeanList;
        }

        protected void onPostExecute(List<CheckStockBean> result) {
            if (result.size() >= 0) {
                CheckStockAdapter QuitStockAdapter = new CheckStockAdapter(result, CheckLibraryActivity.this);
                Sp_QuitHouseSpace.setAdapter(QuitStockAdapter);
                Sp_QuitHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (SpQuitHouseSpaceIndex != i) {
                            SpQuitHouseSpaceIndex = i;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    private class AsyncGetMaterialCell extends AsyncTask<String, Void, List<CheckBodyMaterial>> {

        private int pos = 0;

//        AsyncGetMaterialCell(int pos) {
//            this.pos = pos;
//        }

        @Override
        protected List<CheckBodyMaterial> doInBackground(String... params) {
            SubBodyMaterial = new ArrayList<>();
            try {

                if (false) {
                    SubBodyMaterial = SubBodyMaterialList;
                } else {
                    SubBodyMaterial = SubBodyMaterialList;
                }
            } catch (Exception e) {
                ViseLog.i("AsyncGetStocksCellException = " + e.getMessage());
            }
            return SubBodyMaterial;
        }

        protected void onPostExecute(List<CheckBodyMaterial> result) {
            if (result.size() >= 0) {
                CheckMaterialAdapter QuitStockAdapter = new CheckMaterialAdapter(result, CheckLibraryActivity.this);
                Sp_Material.setAdapter(QuitStockAdapter);
                Sp_Material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (SpQuitHouseSpaceIndex != i) {
                            SpQuitHouseSpaceIndex = i;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    public class GetQuitLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<CheckLibraryDetail>> {

        @Override
        protected List<CheckLibraryDetail> doInBackground(Integer... params) {
            inputLibraryBills = new ArrayList<>();
            stockBeans = new ArrayList<>();
            quitTaskRvDataList = new ArrayList<>();
            checkSubbodyRvDataList = new ArrayList<>();
            SubBodyStocksList = new ArrayList<>();
            SubBodyMaterialList = new ArrayList<>();
            String InputBills = "";
            String Stocks = "";
            InputStream in_Stocks = null;
            try {
                InputBills = webService.GetCheckWareHouseData(ConnectStr.ConnectionToString, FGUID);
                InputStream InputAllInfoStream = new ByteArrayInputStream(InputBills.getBytes("UTF-8"));
                List<CheckLibraryAllInfo> quitLibraryAllInfoList = CheckLibraryXmlAnalysis.getSingleton().GetCheckAllInfoList(InputAllInfoStream);
                if (quitLibraryAllInfoList.get(0).getFStatus().equals("1")) {
                    InputStream HeadinfoStr = new ByteArrayInputStream(quitLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    InputStream BodyinfoStr = new ByteArrayInputStream(quitLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    InputStream SubBodyinfoStr = new ByteArrayInputStream(quitLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    InputStream SubBodyStocks = new ByteArrayInputStream(quitLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    InputStream SubBodyMaterial = new ByteArrayInputStream(quitLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    ViseLog.i("CheckLibraryAllInfoList.get(0).getFInfo() = " + quitLibraryAllInfoList.get(0).getFInfo());
                    inputLibraryBills = CheckLibraryXmlAnalysis.getSingleton().GetCheckDetailXml(HeadinfoStr);
                    quitTaskRvDataList = CheckLibraryXmlAnalysis.getSingleton().GetBodyInfo(BodyinfoStr);
                    checkSubbodyRvDataList = CheckLibraryXmlAnalysis.getSingleton().GetSubBodyInfo(SubBodyinfoStr);
                    SubBodyStocksList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyStocks(SubBodyStocks);
                    SubBodyMaterialList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyMaterial(SubBodyMaterial);
                    quitTaskRvDataList = DisposeTaskRvDataList(quitTaskRvDataList);
                    checkSubbodyRvDataList = DisposeSubbodyRvDataList(checkSubbodyRvDataList);
                    HeadinfoStr.close();
                    BodyinfoStr.close();
                    SubBodyinfoStr.close();
                    SubBodyStocks.close();
                    SubBodyMaterial.close();
                    Stocks = webService.GetStocks(ConnectStr.ConnectionToString);
                    in_Stocks = new ByteArrayInputStream(Stocks.getBytes("UTF-8"));
                    List<CheckStock_Return> stock_returnList = CheckStockXmlAnalysis.getSingleton().GetXmlStockReturn(in_Stocks);
                    if (stock_returnList.get(0).getFStatus().equals("1")) {
                        InputStream In_StockInfo = new ByteArrayInputStream(stock_returnList.get(0).getFInfo().getBytes("UTF-8"));
                        stockBeans = CheckStockXmlAnalysis.getSingleton().GetXmlStockInfo(In_StockInfo);
                    } else {
                        stockBeans.clear();
                    }
                    ViseLog.i("Stocks = " + Stocks);
                    return inputLibraryBills;
                } else {
                    inputLibraryBills.clear();
                    return inputLibraryBills;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputLibraryBills;
        }

        @Override
        protected void onPostExecute(final List<CheckLibraryDetail> result) {
            try {
                if (result.size() >= 0) {
                    if (quitTaskRvDataList.size() >= 0) {
                        InitScanRecycler();
                    }
                    if (stockBeans.size() >= 0){
                        InitSp(stockBeans, result.get(0).getFStock_Name());
                    }else {

                    }
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    HeadID = result.get(0).getFGuid();
                    IsAllow = result.get(0).getFAllowOtherMaterial();
                }
                myProgressDialog.dismiss();
            } catch (Exception e) {
                ViseLog.d("Input Bill Result Exception" + e.getMessage());
            }
            ViseLog.i("Input Bill Result = " + result);
        }

        @Override
        protected void onPreExecute() {
            myProgressDialog.ShowPD("加载数据中...");
        }
    }

    private List DisposeTaskRvDataList(List<CheckTaskRvData> disposeInputTaskRvDataList) {
        List<CheckTaskRvData> removeList = new ArrayList<CheckTaskRvData>();
        for (int index = 0; index < disposeInputTaskRvDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFCheckQty()) && !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAccountQty())) {
                    float AuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAccountQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFCheckQty());
                    NoSendQty = String.valueOf(ExecutedAuxQty - AuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(disposeInputTaskRvDataList.get(index));
                    continue;
                }
            }
            if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAccountQty()) &&
                    !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFCheckQty())) {
                AddAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAccountQty());
                AddExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFCheckQty());
                AddNoSendQty = String.valueOf(AddExecutedAuxQty - AddAuxQty);
            }
            disposeInputTaskRvDataList.get(index).setFDiffQty(AddNoSendQty);
            disposeInputTaskRvDataList.get(index).setFAccountQty(String.valueOf(AddAuxQty));
            disposeInputTaskRvDataList.get(index).setFCheckQty(String.valueOf(AddExecutedAuxQty));
        }
        disposeInputTaskRvDataList.removeAll(removeList);
        return disposeInputTaskRvDataList;
    }

    private List DisposeSubbodyRvDataList(List<CheckSubBody> disposeInputTaskRvDataList) {
        List<CheckSubBody> removeList = new ArrayList<CheckSubBody>();
        for (int index = 0; index < disposeInputTaskRvDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFCheckQty()) && !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAccountQty())) {
                    float AuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAccountQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFCheckQty());
                    NoSendQty = String.valueOf(ExecutedAuxQty - AuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(disposeInputTaskRvDataList.get(index));
                    continue;
                }
            }
            if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAccountQty()) &&
                    !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFCheckQty())) {
                AddAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAccountQty());
                AddExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFCheckQty());
                AddNoSendQty = String.valueOf(AddExecutedAuxQty - AddAuxQty);
            }
            disposeInputTaskRvDataList.get(index).setFDiffQty(AddNoSendQty);
            disposeInputTaskRvDataList.get(index).setFAccountQty(String.valueOf(AddAuxQty));
            disposeInputTaskRvDataList.get(index).setFCheckQty(String.valueOf(AddExecutedAuxQty));
        }
        disposeInputTaskRvDataList.removeAll(removeList);
        return disposeInputTaskRvDataList;
    }

    @Override
    public void onBackPressed() {
        if (Tools.IsObjectNull(childQuitTagList)) {
            if (childQuitTagList.size() > 0) {
                tools.ShowOnClickDialog(MContect, "是否退出盘点界面，退出数据将清空", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                        ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                    }
                }, false);
            } else {
                ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
            }
        } else {
            ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
        }
    }

    private class GetMaterialMode extends AsyncTask<String, Void, List<CheckMaterialModeBean>> {

        @Override
        protected List<CheckMaterialModeBean> doInBackground(String... params) {
            String ModeXml = "";
            List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<>();
            List<CheckAdapterReturn> adapterReturnList;
            try {
                ModeXml = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, params[0]);
                InputStream IS_ModeXml = new ByteArrayInputStream(ModeXml.getBytes("UTF-8"));
                adapterReturnList = CheckAnalysisReturnsXml.getSingleton().GetReturn(IS_ModeXml);
                IS_ModeXml.close();
                if (adapterReturnList.get(0).getFStatus().equals("1")) {
                    InputStream IS_ModeInfoXml = new ByteArrayInputStream(adapterReturnList.get(0).getFInfo().getBytes("UTF-8"));
                    materialModeBeanList = CheckAnalysisMaterialModeXml.getSingleton().GetMaterialModeInfo(IS_ModeInfoXml);
                    ViseLog.i("标签模板 = " + adapterReturnList.get(0).getFInfo());
                    IS_ModeInfoXml.close();
                } else {
                    materialModeBeanList.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ViseLog.e("GetMaterialMode 异常 = " + e);
            }
            return materialModeBeanList;
        }

        protected void onPostExecute(List<CheckMaterialModeBean> result) {
            if (result.size() >= 0) {
                materialModeBeanList = result;
                CheckMaterialModeAdapter QuitStockAdapter = new CheckMaterialModeAdapter(materialModeBeanList, CheckLibraryActivity.this);
                Sp_Label.setAdapter(QuitStockAdapter);
                Sp_Label.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (Sp_LabelModeIndex != i) {
                            Sp_LabelModeIndex = i;
                        }

                        if (materialModeBeanList.get(i).getFBarCoeeCount().equals("1") || materialModeBeanList.get(i).getFBarCoeeCount().equals("0")) {
                            Is_Single = true;
                            ZebarTools.getZebarTools().SetZebarDWConfig(MContect, "1", "1");
                            ViseLog.i("Zebar单条码格式");
                        } else {
                            Is_Single = false;
                            ZebarTools.getZebarTools().SetZebarDWConfig(MContect, materialModeBeanList.get(i).getFBarCoeeCount(), "3");
                            ViseLog.i("Zebar多条码格式");
                        }

                        QuitTagMode quitTagMode = new QuitTagMode(materialModeBeanList.get(i).getFGuid(), webService);
                        quitTagMode.execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    public class QuitTagMode extends AsyncTask<String, Void, List<ChildCheckTag>> {

        private WebService webService;
        private String LabelTempletID;

        public QuitTagMode(String LabelTempletID, WebService webService) {
            this.webService = webService;
            this.LabelTempletID = LabelTempletID;
        }

        @Override
        protected List<ChildCheckTag> doInBackground(String[] par) {
            List<ChildCheckTag> childQuitTagList = new ArrayList<ChildCheckTag>();
            try {
                String ResStatus = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
                InputStream is_res = new ByteArrayInputStream(ResStatus.getBytes("UTF-8"));
                List<CheckAdapterReturn> list_return = CheckAnalysisReturnsXml.getSingleton().GetReturn(is_res);
                is_res.close();
                if (list_return.get(0).getFStatus().equals("1")) {
                    ViseLog.i("CheckTagMode Info = " + list_return.get(0).getFInfo());
                    InputStream is_info = new ByteArrayInputStream(list_return.get(0).getFInfo().getBytes("UTF-8"));
                    childQuitTagList = CheckTagModeAnalysis.getSingleton().GetTagMode(is_info);
                    is_info.close();
                    childQuitTagList.get(0).getGuid();
                    return childQuitTagList;
                } else {
                    return childQuitTagList;
                }
            } catch (Exception e) {
                ViseLog.i("CheckTagMode Exception =  " + e);
            }
            return null;
        }

        protected void onPostExecute(List<ChildCheckTag> result) {
            if (Tools.IsObjectNull(childQuitTagList)) {
                childQuitTagList.clear();
            }
            if (result.size() >= 0) {
                childQuitTagList = result;
                if (RefreshStatu == 1) {
                    RefreshStatu = 2;
                    InitRecycler();
                    InitSubbodyRecycler();
                } else {
                    scanResult_Quit_rvAdapter = new ScanResult_CheckRvAdapter(MContect, childQuitTagList);
                    RV_GetInfoTable.setAdapter(scanResult_Quit_rvAdapter);
                    subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubbodyRvDataList2);
                    RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                }
            }
        }
    }

    /*
     * EventBus触发回调
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(BarCodeMessage msg) {
        if (!Is_QuitNumber_Mode) {
            if (scanTask_quit_rvAdapter.getselection() >= 0) {
                String data = msg.data;
                ViseLog.i("messageEventBus msg = " + data);
                if (!Is_Single) {
                    data = data.replace("\n", "；");
                    data = data.substring(0, data.length() - 1);
                }
                ViseLog.i("messageEventBus msg = " + data);
                CheckBarCodeCheckPort quitbarCodeCheckPort = new CheckBarCodeCheckPort() {
                    @Override
                    public void onData(String Info) {
                        try {
                            if (!Info.substring(0, 2).equals("EX")) {
                                ViseLog.i("Info = " + Info);
                                InputStream IsBarCodeInfoHead = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                InputStream IsBarCodeInfoBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                List<BarCodeHeadBean> BarCodeInfoHeadList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                List<BarcodeXmlBean> barcodeXmlBeanList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                //BarcodeLib

                                //判断条码解析出来的条码库ID是否存在在子分录集合中是否存在，没有的话添加

                                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_SEQUENCE.toLowerCase())) {
                                    Is_QuitNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsSerialNumber = false;
                                    IsAddSerialNumber = true;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");
                                } else if (!TextUtils.isEmpty(BarCodeInfoHeadList.get(3).getFQty())) {
                                    //算法  fqty * FUnitRate / baseqty
                                    float ThisPutSum = Tools.StringOfFloat(BarCodeInfoHeadList.get(3).getFQty());
                                    Is_QuitNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(String.valueOf(ThisPutSum));
                                    ViseLog.i("有数量 = " + FBarcodeLib);
                                } else if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||
                                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                                    Is_QuitNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsBarCodeInfoHead.close();
                                    IsBarCodeInfoBody.close();
                                    ShowEditSumDialog(NoQuitLibrary());
                                    ViseLog.i("通用号 FBarcodeLib =" + FBarcodeLib);
                                }
                                //更新子分录

                                //根据子分录数量汇总分录

                            } else {
                                tools.ShowDialog(MContect, Info.substring(2, Info.length()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ViseLog.i("BarCodeCheckPort Exception = " + e);
                        }
                    }
                };
                CheckBarCodeCheckTask barCodeCheckTask = new CheckBarCodeCheckTask(quitbarCodeCheckPort, webService, quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFMaterial(),
                        materialModeBeanList.get(Sp_LabelModeIndex).getFGuid(), data);
                barCodeCheckTask.execute();
            } else {
//            tools.ShowDialog(MContect, "请选择单据分路");
                if (!TextUtils.isEmpty(msg.data)) {
                    int pos = GetSpinnerPos(stockBeanList, msg.data);
                    if (pos != -1) {
                        Sp_QuitHouseSpace.setSelection(pos);
                    }
                }
            }
        }
    }

    public void SubmitData(String QuitSum) {
        try {
            if (Is_QuitNumber_Mode) {
                if (true) {
                    if (CheckGuid(CheckFGuid, FBarcodeLib)) {
                        IsSerialNumber = true;
                        if (true) {
                            if (IsAddSerialNumber) {
                                CheckFGuid.add(FBarcodeLib);
                                IsAddSerialNumber = false;
                            }
                            Sp_house.setEnabled(false);
                            Sp_QuitHouseSpace.setEnabled(false);
                            Sp_Material.setEnabled(false);
                            Drawable Borderhouse = getResources().getDrawable(R.drawable.border);
                            Sp_house.setBackground(Borderhouse);
                            Drawable BorderInputHouseSpace = getResources().getDrawable(R.drawable.border);
                            Sp_QuitHouseSpace.setBackground(BorderInputHouseSpace);
                            Sp_Material.setBackground(BorderInputHouseSpace);

                            IsSave = true;
                            Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                            TV_Sumbit.setBackground(drawable_purple);
                            TV_Sumbit.setTextColor(getResources().getColor(R.color.White));
                            TV_Save.setBackground(drawable_purple);
                            TV_Save.setTextColor(getResources().getColor(R.color.White));

//                            CheckSubBody checkSubBody = new CheckSubBody();
//                            checkSubBody.setFGuid(FGUID);
//                            checkSubBody.setFBillBodyID(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFGuid());
//                            subBodyBean.setInputLibrarySum(QuitSum);
//                            checkSubBodyBeanList.add(checkSubBody);

                            float Sum = Tools.StringOfFloat(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) +
                                    Tools.StringOfFloat(QuitSum);
                            String SetFThisAuxQty = String.valueOf(Sum);
                            quitTaskRvDataList.get(RV_ScanInfoTableIndex).setFCheckQty(SetFThisAuxQty);

                            //为最后生成入库单保存数据array
                            float NewNoPut = Tools.StringOfFloat(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                            String SetNoInput = String.valueOf(NewNoPut);
                            quitTaskRvDataList.get(RV_ScanInfoTableIndex).setFDiffQty(SetNoInput);

                            if (true) {
                                scanTask_quit_rvAdapter.notifyDataSetChanged();
                                Is_QuitNumber_Mode = false;
                            }
                        }
                    } else {
                        tools.ShowDialog(MContect, "此条码已经扫描过了");
                    }
                } else {
                    tools.ShowDialog(MContect, "盘点数量为空或小于0");
                }
            }
        } catch (Exception e) {
            ViseLog.i("SubmitData Exception = " + e);
            tools.ShowDialog(MContect, "提交错误：" + e.getMessage());
        }
    }

    public String NoQuitLibrary() {
        if (!TextUtils.isEmpty(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFAccountQty()) && !TextUtils.isEmpty(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFCheckQty())) {
            float DiffQty = Tools.StringOfFloat(quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFCheckQty());
            String DiffQtySum = String.valueOf(DiffQty);
            ViseLog.i("CheckSumLibrary = " + DiffQtySum);
            return DiffQtySum;
        }
        return "";
    }

    public void PutResultArray(List<BarcodeXmlBean> barcodeXmlBeans) {
        if (barcodeXmlBeans.size() > 0) {
            childQuitTagList.clear();
            for (BarcodeXmlBean barcodeXmlBean : barcodeXmlBeans) {
                ChildCheckTag childQuitTag = new ChildCheckTag();
                childQuitTag.setName(barcodeXmlBean.getFBarcodeName());
                childQuitTag.setValue(barcodeXmlBean.getFBarcodeContent());
                childQuitTagList.add(childQuitTag);
            }
            scanResult_Quit_rvAdapter.notifyDataSetChanged();
        }
    }

    private void CreateBillData() {

        CheckBillCreate checkBillCreate = new CheckBillCreate() {
            @Override
            public void onResult(String Info) {
                if (Info.substring(0, 2).equals("EX")) {
                    tools.ShowDialog(MContect, Info.substring(2, Info.length()));
                } else {
                    tools.ShowOnClickDialog(MContect, Info, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tools.DisappearDialog();
                            finish();
                            Intent intent = new Intent(CheckLibraryActivity.this, CheckLibraryActivity.class);
                            intent.putExtra("FGUID", tools.GetStringData(tools.InitSharedPreferences(CheckLibraryActivity.this), "CheckLibraryActivityFGUID"));
                            startActivity(intent);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }, true);
                }
            }
        };
        CheckBillCreateTask checkBillCreateTask = new CheckBillCreateTask(inputLibraryBills,quitTaskRvDataList,checkSubbodyRvDataList, webService, myProgressDialog, checkBillCreate);
        checkBillCreateTask.execute();

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

    private void ShowEditSumDialog(String Sum) {
        EditSumDialog.getSingleton().Show(MContect, quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSumDialog.getSingleton().Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, quitTaskRvDataList.get(RV_ScanInfoTableIndex).getFBaseUnit_Name());
    }

    private void CleanData() {
        Is_QuitNumber_Mode = false;
        childQuitTagList.clear();
        scanResult_Quit_rvAdapter.notifyDataSetChanged();
        scanTask_quit_rvAdapter.setSelection(-1);
        scanTask_quit_rvAdapter.notifyDataSetChanged();
        ViseLog.i("Click CleanData");
    }

    private List<CheckTaskRvData> SumList(List<CheckTaskRvData> taskRvData, List<CheckSubBody> checkSubBodies, int pos) {
        List<CheckSubBody> subBodies = new ArrayList<>();
        for (int i = 0; i < checkSubBodies.size(); i++) {
            if (taskRvData.get(pos).getFGuid().equals(checkSubBodies.get(i).getFBillBodyID())) {
                subBodies.add(checkSubBodies.get(i));
            }
        }
        if (subBodies.size() > 0) {
            taskRvData.get(pos).setCheckSubBody(subBodies);
        }
        return taskRvData;
    }
}
