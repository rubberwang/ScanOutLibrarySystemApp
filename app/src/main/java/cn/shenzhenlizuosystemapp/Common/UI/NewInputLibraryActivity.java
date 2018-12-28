package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.BarCodeCheckTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.InputBillCreateTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.InputBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.UnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Myapplication;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Stock_Return;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.InputBillCreate;
import cn.shenzhenlizuosystemapp.Common.Port.InputTagModePort;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.MaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.View.EditSumDialog;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.WebBean.InputLibraryAllInfo;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.InputStockXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.InputTagModeAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class NewInputLibraryActivity extends BaseActivity {

    //标示符
    private String FGUID = "";
    private int RV_ScanInfoTableIndex = 0;
    private int SpHouseIndex = 0;
    private int SpInputHouseSpaceIndex = 0;
    private int Sp_LabelModeIndex = 0;
    private boolean Is_InputNumber_Mode = false;
    private String FBarcodeLib = "";
    private String ILSum = "";
    private int RefreshStatu = 1;
    private boolean Is_Single = false;
    private String HeadID = "";
    private boolean IsSerialNumber = true;
    private boolean IsAddSerialNumber = false;
    private boolean IsSave = false;

    //数组
    private List<ChildTag> childTagList = null;
    private List<InputTaskRvData> inputTaskRvDataList = null;
    private List<StockBean> stockBeans = null;
    private List<StockBean> stockBeanList = null;
    private List<MaterialModeBean> materialModeBeanList = new ArrayList<MaterialModeBean>();
    private List<InputSubBodyBean> InputSubmitList = new ArrayList<InputSubBodyBean>();
    private List<String> CheckFGuid = new ArrayList<String>();

    //类
    private Context MContect;
    private Tools tools = null;
    private InputLibraryObServer inputLibraryObServer;
    private WebService webService;
    private ScanResult_InputRvAdapter scanResult_Input_rvAdapter;
    private ScanTask_InputRvAdapter scanTask_Input_rvAdapter;
    private EditSumPort editSumPort;

    //控件
    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private Spinner Sp_house;
    private Spinner spinnerScannerDevices;
    private Spinner Sp_InputHouseSpace;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private TextView TV_Scaning;
    private TextView TV_Cancel;
    private TextView TV_Sumbit;
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;
    private Spinner Sp_Label;
    private MyProgressDialog myProgressDialog;

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
        return R.layout.scaning_input_layout;
    }

    @Override
    public void initData() {
        MContect = new WeakReference<>(NewInputLibraryActivity.this).get();
        EventBus.getDefault().register(MContect);
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        tools = Tools.getTools();
        tools.PutStringData("NewInputLibraryActivityFGUID", FGUID, tools.InitSharedPreferences(MContect));
        inputLibraryObServer = new InputLibraryObServer();
        getLifecycle().addObserver(inputLibraryObServer);
        webService = WebService.getSingleton(MContect);
        InitClick();
        GetInputLibraryBillsAsyncTask getInputLibraryBillsAsyncTask = new GetInputLibraryBillsAsyncTask();
        getInputLibraryBillsAsyncTask.execute();
        Drawable drawable = getResources().getDrawable(R.drawable.circularbead_gray);
        TV_Sumbit.setBackground(drawable);
        TV_Sumbit.setTextColor(getResources().getColor(R.color.Black));
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
        Back = $(R.id.Back);
        TV_DeliverGoodsNumber = $(R.id.TV_DeliverGoodsNumber);
        Sp_house = $(R.id.Sp_house);
        TV_BusType = $(R.id.TV_BusType);
        TV_Unit = $(R.id.TV_Unit);
        RV_GetInfoTable = $(R.id.RV_GetInfoTable);
        RV_ScanInfoTable = $(R.id.RV_ScanInfoTable);
        spinnerScannerDevices = $(R.id.spinnerScannerDevices);
        Sp_InputHouseSpace = $(R.id.Sp_InputHouseSpace);
        TV_Cancel = $(R.id.TV_Cancel);
        TV_Sumbit = $(R.id.TV_Sumbit);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
        Sp_Label = $(R.id.Sp_Label);
    }

    private void InitClick() {
//        Et_ScanNumber.setFocusable(false);
//        Et_ScanNumber.setFocusableInTouchMode(false);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(NewInputLibraryActivity.this);
            }
        });

        TV_Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                        ViewManager.getInstance().finishActivity(NewInputLibraryActivity.this);
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                    }
                }, false);
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
    }

    /***
     * 标签模版列表适配
     * ***/
    private void InitRecycler() {
        if (Tools.IsObjectNull(childTagList)) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            RV_GetInfoTable.setLayoutManager(layoutManager);
            scanResult_Input_rvAdapter = new ScanResult_InputRvAdapter(this, childTagList);
            RV_GetInfoTable.setAdapter(scanResult_Input_rvAdapter);
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
        scanTask_Input_rvAdapter = new ScanTask_InputRvAdapter(this, inputTaskRvDataList);
        RV_ScanInfoTable.setAdapter(scanTask_Input_rvAdapter);
        scanTask_Input_rvAdapter.setOnItemClickLitener(new ScanTask_InputRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (Integer.parseInt(inputTaskRvDataList.get(position).getFAuxQty().split("\\.")[0]) <= Integer.parseInt(inputTaskRvDataList.get(position).getFThisAuxQty().split("\\.")[0]) +
                        Integer.parseInt(inputTaskRvDataList.get(position).getFExecutedAuxQty().split("\\.")[0])) {
                    tools.ShowDialog(MContect, "这张单已扫描完成");
                } else {
                    if (!Is_InputNumber_Mode) {
                        if (scanTask_Input_rvAdapter.getselection() != position) {
                            LockResultPort lockResultPort = new LockResultPort() {
                                @Override
                                public void onStatusResult(String res) {
                                    myProgressDialog.dismiss();
                                    if (res.equals("Success")) {
                                        if (RV_ScanInfoTableIndex != position) {
                                            RV_ScanInfoTableIndex = position;
                                        }
                                        GetMaterialMode getMaterialMode = new GetMaterialMode();
                                        getMaterialMode.execute(inputTaskRvDataList.get(position).getFMaterial());
                                        scanTask_Input_rvAdapter.setSelection(position);
                                        scanTask_Input_rvAdapter.notifyDataSetChanged();//选中
                                    } else {
                                        tools.ShowDialog(MContect, res);
                                    }
                                }
                            };
                            InputBodyLockTask inputBodyLockTask = new InputBodyLockTask(lockResultPort, webService, inputTaskRvDataList.get(position).getFGuid(), myProgressDialog);
                            inputBodyLockTask.execute();
                        } else {
                            childTagList.clear();
                            scanResult_Input_rvAdapter.notifyDataSetChanged();
                            scanTask_Input_rvAdapter.setSelection(-1);
                            scanTask_Input_rvAdapter.notifyDataSetChanged();//取消选中
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

    private void InitSp(List<StockBean> stockBeans, String StockName) {
        if (stockBeans.size() >= 0) {
            InputStockAdapter InputStockAdapter = new InputStockAdapter(stockBeans, NewInputLibraryActivity.this);
            Sp_house.setAdapter(InputStockAdapter);
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


    class InputLibraryObServer implements LifecycleObserver {
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

    private class AsyncGetStocksCell extends AsyncTask<String, Void, List<StockBean>> {

        private int pos = 0;

        AsyncGetStocksCell(int pos) {
            this.pos = pos;
        }

        @Override
        protected List<StockBean> doInBackground(String... params) {
            stockBeanList = new ArrayList<>();
            try {
                String StocksCell = webService.GetStocksCell(ConnectStr.ConnectionToString, stockBeans.get(pos).getFGuid());
                InputStream inStockCell = new ByteArrayInputStream(StocksCell.getBytes("UTF-8"));
                List<AdapterReturn> stock_returns = AnalysisReturnsXml.getSingleton().GetReturn(inStockCell);
                if (stock_returns.get(0).getFStatus().equals("1")) {
                    InputStream In_Info = new ByteArrayInputStream(stock_returns.get(0).getFInfo().getBytes("UTF-8"));
                    stockBeanList = InputStockXmlAnalysis.getSingleton().GetXmlStockInfo(In_Info);
                } else {
                    stockBeanList.clear();
                }
                inStockCell.close();
            } catch (Exception e) {
                ViseLog.i("AsyncGetStocksCellException = " + e.getMessage());
            }
            return stockBeanList;
        }

        protected void onPostExecute(List<StockBean> result) {
            if (result.size() >= 0) {
                InputStockAdapter InputStockAdapter = new InputStockAdapter(result, NewInputLibraryActivity.this);
                Sp_InputHouseSpace.setAdapter(InputStockAdapter);
                Sp_InputHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (SpInputHouseSpaceIndex != i) {
                            SpInputHouseSpaceIndex = i;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<InputLibraryDetail>> {

        @Override
        protected List<InputLibraryDetail> doInBackground(Integer... params) {
            List<InputLibraryDetail> inputLibraryBills = new ArrayList<>();
            stockBeans = new ArrayList<>();
            inputTaskRvDataList = new ArrayList<>();
            String InputBills = "";
            String Stocks = "";
            InputStream in_Stocks = null;
            try {
                InputBills = webService.GetWareHouseData(ConnectStr.ConnectionToString, FGUID);
                InputStream InputAllInfoStream = new ByteArrayInputStream(InputBills.getBytes("UTF-8"));
                List<InputLibraryAllInfo> inputLibraryAllInfoList = InputLibraryXmlAnalysis.getSingleton().GetInputAllInfoList(InputAllInfoStream);
                if (inputLibraryAllInfoList.get(0).getFStatus().equals("1")) {
                    InputStream HeadinfoStr = new ByteArrayInputStream(inputLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    InputStream BodyinfoStr = new ByteArrayInputStream(inputLibraryAllInfoList.get(0).getFInfo().getBytes("UTF-8"));
                    ViseLog.i("inputLibraryAllInfoList.get(0).getFInfo() = " + inputLibraryAllInfoList.get(0).getFInfo());
                    inputLibraryBills = InputLibraryXmlAnalysis.getSingleton().GetInputDetailXml(HeadinfoStr);
                    inputTaskRvDataList = InputLibraryXmlAnalysis.getSingleton().GetBodyInfo(BodyinfoStr);
                    inputTaskRvDataList = DisposeTaskRvDataList(inputTaskRvDataList);
                    HeadinfoStr.close();
                    BodyinfoStr.close();
                    Stocks = webService.GetStocks(ConnectStr.ConnectionToString);
                    in_Stocks = new ByteArrayInputStream(Stocks.getBytes("UTF-8"));
                    List<Stock_Return> stock_returnList = InputStockXmlAnalysis.getSingleton().GetXmlStockReturn(in_Stocks);
                    if (stock_returnList.get(0).getFStatus().equals("1")) {
                        InputStream In_StockInfo = new ByteArrayInputStream(stock_returnList.get(0).getFInfo().getBytes("UTF-8"));
                        stockBeans = InputStockXmlAnalysis.getSingleton().GetXmlStockInfo(In_StockInfo);
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
        protected void onPostExecute(final List<InputLibraryDetail> result) {
            try {
                if (result.size() >= 0) {
                    if (inputTaskRvDataList.size() >= 0) {
                        InitScanRecycler();
                    }
                    if (stockBeans.size() >= 0) {
                        InitSp(stockBeans, result.get(0).getFStock_Name());
                    }
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    TV_BusType.setText(result.get(0).getFTransactionType_Name());
                    TV_Unit.setText(result.get(0).getFPartner_Name());
                    HeadID = result.get(0).getFGuid();
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

    private List DisposeTaskRvDataList(List<InputTaskRvData> disposeInputTaskRvDataList) {
        List<InputTaskRvData> removeList = new ArrayList<InputTaskRvData>();
        for (int index = 0; index < disposeInputTaskRvDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAuxQty()) && !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFExecutedAuxQty())) {
                    float AuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAuxQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFExecutedAuxQty());
                    NoSendQty = String.valueOf(AuxQty - ExecutedAuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(disposeInputTaskRvDataList.get(index));
                    continue;
                }
                if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFIsClosed())) {
                    if (disposeInputTaskRvDataList.get(index).getFIsClosed().equals("1")) {
                        removeList.add(disposeInputTaskRvDataList.get(index));
                        continue;
                    }
                }
            }
            if (!TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFAuxQty()) &&
                    !TextUtils.isEmpty(disposeInputTaskRvDataList.get(index).getFExecutedAuxQty())) {
                AddAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFAuxQty());
                AddExecutedAuxQty = Tools.StringOfFloat(disposeInputTaskRvDataList.get(index).getFExecutedAuxQty());
                AddNoSendQty = String.valueOf(AddAuxQty - AddExecutedAuxQty);
            }
            disposeInputTaskRvDataList.get(index).setNoInput(AddNoSendQty);
            disposeInputTaskRvDataList.get(index).setFAuxQty(String.valueOf(AddAuxQty));
            disposeInputTaskRvDataList.get(index).setFExecutedAuxQty(String.valueOf(AddExecutedAuxQty));
        }
        disposeInputTaskRvDataList.removeAll(removeList);
        return disposeInputTaskRvDataList;
    }

    @Override
    public void onBackPressed() {
        tools.ShowOnClickDialog(MContect, "是否退出入库界面，退出数据将清空", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.DisappearDialog();
                ViewManager.getInstance().finishActivity(NewInputLibraryActivity.this);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.DisappearDialog();
            }
        }, false);
    }

    private class GetMaterialMode extends AsyncTask<String, Void, List<MaterialModeBean>> {

        @Override
        protected List<MaterialModeBean> doInBackground(String... params) {
            String ModeXml = "";
            List<MaterialModeBean> materialModeBeanList = new ArrayList<>();
            List<AdapterReturn> adapterReturnList;
            try {
                ModeXml = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, params[0]);
                InputStream IS_ModeXml = new ByteArrayInputStream(ModeXml.getBytes("UTF-8"));
                adapterReturnList = AnalysisReturnsXml.getSingleton().GetReturn(IS_ModeXml);
                IS_ModeXml.close();
                if (adapterReturnList.get(0).getFStatus().equals("1")) {
                    InputStream IS_ModeInfoXml = new ByteArrayInputStream(adapterReturnList.get(0).getFInfo().getBytes("UTF-8"));
                    materialModeBeanList = AnalysisMaterialModeXml.getSingleton().GetMaterialModeInfo(IS_ModeInfoXml);
                    ViseLog.i("materialModeBeanList info = " + adapterReturnList.get(0).getFStatus());
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

        protected void onPostExecute(List<MaterialModeBean> result) {
            if (result.size() >= 0) {
                materialModeBeanList = result;
                MaterialModeAdapter InputStockAdapter = new MaterialModeAdapter(materialModeBeanList, NewInputLibraryActivity.this);
                Sp_Label.setAdapter(InputStockAdapter);
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

                        InputTagMode inputTagMode = new InputTagMode(materialModeBeanList.get(i).getFGuid(), webService);
                        inputTagMode.execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }

    public class InputTagMode extends AsyncTask<String, Void, List<ChildTag>> {

        private WebService webService;
        private String LabelTempletID;

        public InputTagMode(String LabelTempletID, WebService webService) {
            this.webService = webService;
            this.LabelTempletID = LabelTempletID;
        }

        @Override
        protected List<ChildTag> doInBackground(String[] par) {
            List<ChildTag> childTagList = new ArrayList<ChildTag>();
            try {
                String ResStatus = webService.GetLabelTempletBarcodes(ConnectStr.ConnectionToString, LabelTempletID);
                InputStream is_res = new ByteArrayInputStream(ResStatus.getBytes("UTF-8"));
                List<AdapterReturn> list_return = AnalysisReturnsXml.getSingleton().GetReturn(is_res);
                is_res.close();
                if (list_return.get(0).getFStatus().equals("1")) {
                    ViseLog.i("InputTagMode Info = " + list_return.get(0).getFInfo());
                    InputStream is_info = new ByteArrayInputStream(list_return.get(0).getFInfo().getBytes("UTF-8"));
                    childTagList = InputTagModeAnalysis.getSingleton().GetTagMode(is_info);
                    is_info.close();
                    return childTagList;
                } else {
                    return childTagList;
                }
            } catch (Exception e) {
                ViseLog.i("InputTagMode Exception =  " + e);
            }
            return null;
        }

        protected void onPostExecute(List<ChildTag> result) {
            if (Tools.IsObjectNull(childTagList)) {
                childTagList.clear();
            }
            if (result.size() >= 0) {
                childTagList = result;
                if (RefreshStatu == 1) {
                    RefreshStatu = 2;
                    InitRecycler();
                } else {
                    scanResult_Input_rvAdapter = new ScanResult_InputRvAdapter(MContect, childTagList);
                    RV_GetInfoTable.setAdapter(scanResult_Input_rvAdapter);
                }
            }
        }
    }

    /*
     * EventBus触发回调
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(BarCodeMessage msg) {
        if (!Is_InputNumber_Mode) {
            if (scanTask_Input_rvAdapter.getselection() >= 0) {
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
                                    Is_InputNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsSerialNumber = false;
                                    IsAddSerialNumber = true;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");
                                } else if (!TextUtils.isEmpty(BarCodeInfoHeadList.get(3).getFQty())) {
                                    //算法  fqty * FUnitRate / baseqty
                                    float ThisPutSum = Tools.StringOfFloat(BarCodeInfoHeadList.get(3).getFQty()) * Tools.StringOfFloat(BarCodeInfoHeadList.get(0).getFUnitRate())
                                            / Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFUnitRate());
                                    Is_InputNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(String.valueOf(ThisPutSum));
                                    ViseLog.i("有数量 = " + FBarcodeLib);
                                } else if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||
                                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                                    Is_InputNumber_Mode = true;
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
                BarCodeCheckTask barCodeCheckTask = new BarCodeCheckTask(barCodeCheckPort, webService, inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFMaterial(),
                        materialModeBeanList.get(Sp_LabelModeIndex).getFGuid(), data);
                barCodeCheckTask.execute();
            } else {
//            tools.ShowDialog(MContect, "请选择单据分路");
                if (!TextUtils.isEmpty(msg.data)) {
                    int pos = GetSpinnerPos(stockBeanList, msg.data);
                    if (pos != -1) {
                        Sp_InputHouseSpace.setSelection(pos);
                    }
                }
            }
        }
    }

    public void SubmitData(String InputSum) {
        try {
            if (Is_InputNumber_Mode) {
                if (!TextUtils.isEmpty(InputSum) && Tools.StringOfFloat(InputSum) > 0) {
                    if (CheckGuid(CheckFGuid, FBarcodeLib)) {
                        IsSerialNumber = true;
                        if (Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getNoInput()) >= Tools.StringOfFloat(InputSum)) {
                            if (IsAddSerialNumber) {
                                CheckFGuid.add(FBarcodeLib);
                                IsAddSerialNumber = false;
                            }
                            Sp_house.setEnabled(false);
                            Sp_InputHouseSpace.setEnabled(false);
                            Drawable drawable_border = getResources().getDrawable(R.drawable.border);
                            Sp_house.setBackground(drawable_border);
                            Sp_InputHouseSpace.setBackground(drawable_border);

                            IsSave = true;
                            Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                            TV_Sumbit.setBackground(drawable_purple);
                            TV_Sumbit.setTextColor(getResources().getColor(R.color.White));

                            InputSubBodyBean subBodyBean = new InputSubBodyBean();
                            subBodyBean.setFBillBodyID(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFGuid());
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
                            childTagList.clear();
                            scanResult_Input_rvAdapter.notifyDataSetChanged();
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
        if (!TextUtils.isEmpty(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFAuxQty()) && !TextUtils.isEmpty(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFExecutedAuxQty())) {
            float noSend = Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFAuxQty()) - (Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFExecutedAuxQty()) +
                    Tools.StringOfFloat(inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFThisAuxQty()));
            String NoSendSum = String.valueOf(noSend);
            ViseLog.i("NoInputLibrary = " + NoSendSum);
            return NoSendSum;
        }
        return "";
    }

    public void PutResultArray(List<BarcodeXmlBean> barcodeXmlBeans) {
        if (barcodeXmlBeans.size() > 0) {
            childTagList.clear();
            for (BarcodeXmlBean barcodeXmlBean : barcodeXmlBeans) {
                ChildTag childTag = new ChildTag();
                childTag.setName(barcodeXmlBean.getFBarcodeName());
                childTag.setValue(barcodeXmlBean.getFBarcodeContent());
                childTagList.add(childTag);
            }
            scanResult_Input_rvAdapter.notifyDataSetChanged();
        }
    }

    private void CreateBillData() {

        InputBillCreate inputBillCreate = new InputBillCreate() {
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
                            Intent intent = new Intent(NewInputLibraryActivity.this, NewInputLibraryActivity.class);
                            intent.putExtra("FGUID", tools.GetStringData(tools.InitSharedPreferences(NewInputLibraryActivity.this), "NewInputLibraryActivityFGUID"));
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
        InputBillCreateTask inputBillCreateTask = new InputBillCreateTask(HeadID, stockBeans.get(SpHouseIndex).getFGuid(), stockBeanList.get(SpInputHouseSpaceIndex).getFGuid()
                , InputSubmitList, webService, myProgressDialog, inputBillCreate);
        inputBillCreateTask.execute();

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
        EditSumDialog.getSingleton().Show(MContect, inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSumDialog.getSingleton().Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, inputTaskRvDataList.get(RV_ScanInfoTableIndex).getFUnit_Name());
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
