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
import cn.shenzhenlizuosystemapp.Common.Base.CreateGuid;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckBodyMaterial;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckMaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStock_Return;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TreeMBean;
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
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.View.ShowResultListDialog;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckLibraryAllInfo;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisMaterialModeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckAnalysisReturnsXml;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckStockXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckTagModeAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class CheckLibraryActivity extends BaseActivity {

    //标识符
    private String FGUID = "";
    private int RV_ScanInfoTableIndex = 0;//分录列表下标
    private int SpHouseIndex = 0;//仓库列表下标
    private int SpCheckHouseSpaceIndex = 0;//仓位下标
    private int Sp_LabelModeIndex = 0;//标签模板列表下标
    private boolean Is_CheckNumber_Mode = false;
    private String FBarcodeLib = "";
    private int RefreshStatu = 1;
    private boolean Is_Single = false;
    private String HeadID = "";
    private String FbillbodyID = "";
    private String IsAllow = "";
    private boolean IsSerialNumber = true;//是否连续扫描
    private boolean IsAddSerialNumber = false;
    private boolean IsSave = false;
    private boolean isInitial = true;

    //数组
    private List<ChildCheckTag> checkBarCodeAnalyzeList = null;//条码解析列表
    private List<CheckLibraryDetail> checkHeadDataList = null;//单头列表
    private List<CheckTaskRvData> checkBodyDataList = null;//分录列表
    private List<CheckSubBody> checkSubBodyDataList = null;//子分录列表
    private List<CheckStockBean> stockBeans = null;
    private List<CheckStockBean> stockBeanList = null;//仓位列表
    private List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<CheckMaterialModeBean>();//标签模板
    private List<String> CheckFGuid = new ArrayList<String>();
    private List<CheckSubBody> checkSubBodyList = null;
    private List<CheckStockBean> SubBodyStocksList = null;
    private List<CheckBodyMaterial> SubBodyMaterialList = null;//盘点物料列表
    private List<CheckBodyMaterial> SubBodyMaterial = null;
    private List<String> CheckM = new ArrayList<>();
    private List<String> SubBodyMaterialCheckList = new ArrayList<>();

    //类
    private Context MContect;
    private Tools tools = null;
    private CheckLibraryObServer checkLibraryObServer;
    private WebService webService;
    private ScanResult_CheckRvAdapter scanResult_CheckRvAdapter;//扫描解析结果适配
    private ScanTask_CheckRvAdapter scanTask_check_rvAdapter;//分录适配
    private Subbody_CheckRvAdapter subbody_CheckRvAdapter;//子分录适配
    private EditSumPort editSumPort;
    private CheckSubBodyStocksAdapter checkSubBodyStocksAdapter;
    private TreeMBean treeMBean;
    private CheckMaterialAdapter QuitStockAdapter;
    private LinearLayoutManager ScanTaskL;

    //控件
    private TextView Back;
    private TextView TV_DeliverGoodsNumber;//通知单号
    private TextView TV_house;
    private Spinner spinnerScannerDevices;
    private Spinner Sp_CheckHouseSpace;
    private Spinner Sp_Material;
    private TextView TV_Cancel;
    private TextView TV_Sumbit;
    private TextView TV_Save;
    private RecyclerView RV_ResultInfoTable;//存放条码解析list view
    private RecyclerView RV_BodyInfoTable;//存放分录list view
    private RecyclerView RV_SubBodyInfoTable;//存放子分录list view
    private Spinner Sp_Label;
    private MyProgressDialog myProgressDialog;
    private EditText ET_SuckUp;
    private ImageButton BT_CheckMaterial;

    /**
     * 获得spinner的下标
     *
     * @param Datas
     * @param value
     * @return
     */
    private int GetSpinnerPos(List<CheckStockBean> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFName().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private int GetMSpinnerPos(List<CheckBodyMaterial> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFName().equals(value)) {
                return i;
            }
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
        tools.PutStringData("CheckLibraryActivityFGUID", FGUID, tools.InitSharedPreferences(MContect));//获取通知单Guid
        checkLibraryObServer = new CheckLibraryObServer();
        getLifecycle().addObserver(checkLibraryObServer);
        webService = WebService.getSingleton(MContect);

        InitClick();//所有按钮点击事件
        GetCheckLibraryBillsAsyncTask getCheckLibraryBillsAsyncTask = new GetCheckLibraryBillsAsyncTask();
        getCheckLibraryBillsAsyncTask.execute();
        Drawable drawable = getResources().getDrawable(R.drawable.circularbead_gray);//设置背景颜色--灰色
        TV_Sumbit.setBackground(drawable);
        TV_Sumbit.setTextColor(getResources().getColor(R.color.Black));
        TV_Save.setBackground(drawable);
        TV_Save.setTextColor(getResources().getColor(R.color.Black));
        editSumPort = new EditSumPort() {
            @Override
            public void OnEnSure(String Sum) {
                if (true) {
                    SubmitData(Sum);
                    ShowResultListDialog.getSingleton().Dismiss();
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
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
        TV_house = $(R.id.TV_house);
        RV_ResultInfoTable = $(R.id.RV_GetInfoTable1);
        RV_BodyInfoTable = $(R.id.RV_ScanInfoTable);
        RV_SubBodyInfoTable = $(R.id.RV_SubBodyInfoTable);
        spinnerScannerDevices = $(R.id.spinnerScannerDevices);
        Sp_CheckHouseSpace = $(R.id.Sp_CheckHouseSpace);
        Sp_Material = $(R.id.Sp_Material);
        TV_Cancel = $(R.id.TV_Cancel);
        TV_Sumbit = $(R.id.TV_Sumbit);
        TV_Save = $(R.id.TV_Save);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
        Sp_Label = $(R.id.Sp_Label);
        ET_SuckUp = $(R.id.ET_SuckUp);
        BT_CheckMaterial = $(R.id.BT_CheckMaterial);
    }

    /**
     * 按钮点击事件
     */
    private void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
                    if (checkBarCodeAnalyzeList.size() > 0) {
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
                if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
                    if (checkBarCodeAnalyzeList.size() > 0) {
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
                            SubmitBillData();
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
                Intent intent = new Intent(CheckLibraryActivity.this, TreeListActivity.class);
                intent.putExtra("GUID", FGUID);
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
                            SaveBillData();
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
     * 标签模版列表初始化适配
     * ***/
//    private void InitRecycler() {
//        if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//            layoutManager.setOrientation(layoutManager.VERTICAL);
//            RV_ResultInfoTable.setLayoutManager(layoutManager);
//            scanResult_CheckRvAdapter = new ScanResult_CheckRvAdapter(this, checkBarCodeAnalyzeList);
//            RV_ResultInfoTable.setAdapter(scanResult_CheckRvAdapter);
//        }
//    }

    /***
     * 子分录列表适配
     * ***/
    private void InitSubBodyRecycler() {
        if (Tools.IsObjectNull(checkSubBodyList)) {
            LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
            ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
            RV_SubBodyInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
            RV_SubBodyInfoTable.setLayoutManager(ScanTaskL);
            subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(this, checkSubBodyList);
            RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
        }
    }

    /***
     * 扫描任务列表适配
     * ***/
    private void InitScanRecycler() {
        treeMBean = (TreeMBean) getIntent().getSerializableExtra("M");
        ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_BodyInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_BodyInfoTable.setLayoutManager(ScanTaskL);
        scanTask_check_rvAdapter = new ScanTask_CheckRvAdapter(this, checkBodyDataList);
        RV_BodyInfoTable.setAdapter(scanTask_check_rvAdapter);

        if (Tools.IsObjectNull(treeMBean)) {
            int i = 0;
            ViseLog.i("没有找到物料");
            ViseLog.i("treeMBean.getFName() = " + treeMBean.getFName());
            CheckTaskRvData checkTaskRvData = new CheckTaskRvData();
            CheckBodyMaterial checkBodyMaterial = new CheckBodyMaterial();
            List<CheckTaskRvData> checkTaskRvDataArrayList = new ArrayList<CheckTaskRvData>();
            List<CheckBodyMaterial> MaterialList = new ArrayList<CheckBodyMaterial>();
            String BodyGuid = new CreateGuid().toString();//随机生成分录Guid；
            checkTaskRvData.setFGuid(BodyGuid);
            checkTaskRvData.setFMaterial(treeMBean.getFGuid());
            checkTaskRvData.setFMaterial_Code(treeMBean.getFCode());
            checkTaskRvData.setFMaterial_Name(treeMBean.getFName());
            checkTaskRvData.setFModel(treeMBean.getFModel());
            checkTaskRvData.setFBaseUnit(treeMBean.getFBaseUnit());
            checkTaskRvData.setFBaseUnit_Name(treeMBean.getFBaseUnit_Name());
            checkTaskRvData.setFAccountQty("0");
            checkTaskRvData.setFCheckQty("0");
            checkTaskRvData.setFDiffQty("0");
            checkTaskRvDataArrayList.add(checkTaskRvData);
            checkBodyDataList.add(checkTaskRvDataArrayList.get(0));
            scanTask_check_rvAdapter.notifyDataSetChanged();

            checkBodyMaterial.setFName(treeMBean.getFName());
            MaterialList.add(checkBodyMaterial);
            SubBodyMaterialList.add(MaterialList.get(0));//在盘点物料里面添加显示选择的物料名
        } else {
            ViseLog.i("TreeMBean == null");
        }
        scanTask_check_rvAdapter.setOnItemClickLitener(new ScanTask_CheckRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                RvBodyItemClick(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    /**
     * 生命周期
     */
    class CheckLibraryObServer implements LifecycleObserver {
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
            ShowResultListDialog.getSingleton().Dismiss();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            EventBus.getDefault().unregister(MContect);
        }
    }

    /**
     * 仓位异步任务
     */
    private class AsyncGetStocksCell extends AsyncTask<String, Void, List<CheckStockBean>> {


        @Override
        protected List<CheckStockBean> doInBackground(String... params) {
            stockBeanList = new ArrayList<>();
            try {
                String StocksCell = webService.GetStocksCell(ConnectStr.ConnectionToString, checkHeadDataList.get(0).getFStock());
                InputStream inStockCell = new ByteArrayInputStream(StocksCell.getBytes("UTF-8"));
                List<CheckAdapterReturn> stock_returns = CheckAnalysisReturnsXml.getSingleton().GetReturn(inStockCell);
                if (SubBodyStocksList.size() > 0) {
                    stockBeanList = SubBodyStocksList;
                } else {
                    InputStream In_Info = new ByteArrayInputStream(stock_returns.get(0).getFInfo().getBytes("UTF-8"));
                    stockBeanList = CheckStockXmlAnalysis.getSingleton().GetXmlStockInfo(In_Info);
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
                Sp_CheckHouseSpace.setAdapter(QuitStockAdapter);
                Sp_CheckHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (SpCheckHouseSpaceIndex != i) {
                            SpCheckHouseSpaceIndex = i;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    /**
     * 物料异步任务
     */
    private class AsyncGetMaterialCell extends AsyncTask<String, Void, List<CheckBodyMaterial>> {

        private int pos = 0;
        private String MName;

        AsyncGetMaterialCell(int pos, String MName) {
            this.pos = pos;
            this.MName = MName;
        }

        @Override
        protected List<CheckBodyMaterial> doInBackground(String... params) {
            SubBodyMaterial = new ArrayList<>();
            try {
                if (checkHeadDataList.get(0).getFAllowOtherMaterial().equalsIgnoreCase("0")) {
                    SubBodyMaterial = SubBodyMaterialList;
                } else {
                    SubBodyMaterial = SubBodyMaterialList;
                }
            } catch (Exception e) {
                ViseLog.i("AsyncGetMaterialCellException = " + e.getMessage());
            }
            return SubBodyMaterial;
        }

        protected void onPostExecute(final List<CheckBodyMaterial> result) {
            if (result.size() >= 0) {
                QuitStockAdapter = new CheckMaterialAdapter(result, CheckLibraryActivity.this);
                Sp_Material.setAdapter(QuitStockAdapter);
                int pos = GetMSpinnerPos(SubBodyMaterial, MName);
                Sp_Material.setSelection(pos);
                Sp_Material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (isInitial) {
                            isInitial = false;
                            return;
                        } else {
                            if (SpCheckHouseSpaceIndex != i) {
                                SpCheckHouseSpaceIndex = i;
                            }
                            for (int pos = 0; pos < checkBodyDataList.size(); pos++) {
                                ViseLog.i("result GUid = " + result.get(i).getFGuid());
                                if (checkBodyDataList.get(pos).getFMaterial().equals(result.get(i).getFGuid())) {
                                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, pos);
                                    RvBodyItemClick(pos);
                                    scanTask_check_rvAdapter.notifyDataSetChanged();
                                    ViseLog.i("checkBodyDataList checkBodyDataList checkBodyDataList checkBodyDataList");
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }
    }

    /**
     * 获取单头异步任务
     */
    public class GetCheckLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<CheckLibraryDetail>> {

        @Override
        protected List<CheckLibraryDetail> doInBackground(Integer... params) {
            checkHeadDataList = new ArrayList<>();
            stockBeans = new ArrayList<>();
            checkBodyDataList = new ArrayList<>();
            checkSubBodyDataList = new ArrayList<>();
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
                    checkHeadDataList = CheckLibraryXmlAnalysis.getSingleton().GetCheckDetailXml(HeadinfoStr);//单头列表
                    checkBodyDataList = CheckLibraryXmlAnalysis.getSingleton().GetBodyInfo(BodyinfoStr);//分录列表
                    checkSubBodyDataList = CheckLibraryXmlAnalysis.getSingleton().GetSubBodyInfo(SubBodyinfoStr);//子分录列表
                    SubBodyStocksList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyStocks(SubBodyStocks);//仓位列表
                    SubBodyMaterialList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyMaterial(SubBodyMaterial);//物料集合列表
                    checkBodyDataList = DisposeTaskRvDataList(checkBodyDataList);
                    checkSubBodyDataList = DisposeSubbodyRvDataList(checkSubBodyDataList);
                    HeadinfoStr.close();
                    BodyinfoStr.close();
                    SubBodyinfoStr.close();
                    SubBodyStocks.close();
                    SubBodyMaterial.close();
                    return checkHeadDataList;
                } else {
                    checkHeadDataList.clear();
                    return checkHeadDataList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return checkHeadDataList;
        }

        @Override
        protected void onPostExecute(final List<CheckLibraryDetail> result) {
            try {
                if (result.size() >= 0) {
                    if (checkBodyDataList.size() >= 0) {
                        InitScanRecycler();
                    }
                    if (stockBeans.size() >= 0) {
                        AsyncGetStocksCell asyncGetStocksCell = new AsyncGetStocksCell();
                        asyncGetStocksCell.execute();
                    }
                    AsyncGetMaterialCell asyncGetMaterialCell = new AsyncGetMaterialCell(0, "");
                    asyncGetMaterialCell.execute();
                    TV_house.setText(result.get(0).getFStock_Name());
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    HeadID = result.get(0).getFGuid();
                    IsAllow = result.get(0).getFAllowOtherMaterial();
                }
                myProgressDialog.dismiss();
            } catch (Exception e) {
                ViseLog.d("Check Bill Result Exception" + e.getMessage());
            }
            ViseLog.i("Check Bill Result = " + result);
        }

        @Override
        protected void onPreExecute() {
            myProgressDialog.ShowPD("加载数据中...");
        }
    }

    private List DisposeTaskRvDataList(List<CheckTaskRvData> disposeCheckTaskRvDataList) {
        List<CheckTaskRvData> removeList = new ArrayList<CheckTaskRvData>();
        for (int index = 0; index < disposeCheckTaskRvDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(disposeCheckTaskRvDataList.get(index).getFCheckQty()) && !TextUtils.isEmpty(disposeCheckTaskRvDataList.get(index).getFAccountQty())) {
                    float AuxQty = Tools.StringOfFloat(disposeCheckTaskRvDataList.get(index).getFAccountQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(disposeCheckTaskRvDataList.get(index).getFCheckQty());
                    //NoSendQty = String.valueOf(ExecutedAuxQty - AuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(disposeCheckTaskRvDataList.get(index));
                    continue;
                }
            }
            if (!TextUtils.isEmpty(disposeCheckTaskRvDataList.get(index).getFAccountQty()) &&
                    !TextUtils.isEmpty(disposeCheckTaskRvDataList.get(index).getFCheckQty())) {
                AddAuxQty = Tools.StringOfFloat(disposeCheckTaskRvDataList.get(index).getFAccountQty());
                AddExecutedAuxQty = Tools.StringOfFloat(disposeCheckTaskRvDataList.get(index).getFCheckQty());
                AddNoSendQty = String.valueOf(AddExecutedAuxQty - AddAuxQty);
            }
            disposeCheckTaskRvDataList.get(index).setFDiffQty(AddNoSendQty);
            disposeCheckTaskRvDataList.get(index).setFAccountQty(String.valueOf(AddAuxQty));
            disposeCheckTaskRvDataList.get(index).setFCheckQty(String.valueOf(AddExecutedAuxQty));
        }
        disposeCheckTaskRvDataList.removeAll(removeList);
        return disposeCheckTaskRvDataList;
    }

    private List DisposeSubbodyRvDataList(List<CheckSubBody> disposeSubBodyDataList) {
        List<CheckSubBody> removeList = new ArrayList<CheckSubBody>();
        for (int index = 0; index < disposeSubBodyDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;

            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(disposeSubBodyDataList.get(index).getFCheckQty()) && !TextUtils.isEmpty(disposeSubBodyDataList.get(index).getFAccountQty())) {
                    float AuxQty = Tools.StringOfFloat(disposeSubBodyDataList.get(index).getFAccountQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(disposeSubBodyDataList.get(index).getFCheckQty());
                    NoSendQty = String.valueOf(ExecutedAuxQty - AuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(disposeSubBodyDataList.get(index));
                    continue;
                }
            }
            if (!TextUtils.isEmpty(disposeSubBodyDataList.get(index).getFAccountQty()) &&
                    !TextUtils.isEmpty(disposeSubBodyDataList.get(index).getFCheckQty())) {
                AddAuxQty = Tools.StringOfFloat(disposeSubBodyDataList.get(index).getFAccountQty());
                AddExecutedAuxQty = Tools.StringOfFloat(disposeSubBodyDataList.get(index).getFCheckQty());
                AddNoSendQty = String.valueOf(AddExecutedAuxQty - AddAuxQty);
            }
            disposeSubBodyDataList.get(index).setFDiffQty(AddNoSendQty);
            disposeSubBodyDataList.get(index).setFAccountQty(String.valueOf(AddAuxQty));
            disposeSubBodyDataList.get(index).setFCheckQty(String.valueOf(AddExecutedAuxQty));
        }
        disposeSubBodyDataList.removeAll(removeList);
        return disposeSubBodyDataList;
    }

    /**
     * 物理返回按钮点击事件
     */
    @Override
    public void onBackPressed() {
        if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
            if (checkBarCodeAnalyzeList.size() > 0) {
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

    /**
     * 获取标签模板异步任务
     */
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
            if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
                checkBarCodeAnalyzeList.clear();
            }
            if (result.size() >= 0) {
                checkBarCodeAnalyzeList = result;
                if (RefreshStatu == 1) {
                    RefreshStatu = 2;
                    //InitRecycler();
                    InitSubBodyRecycler();
                } else {
                    scanResult_CheckRvAdapter = new ScanResult_CheckRvAdapter(MContect, checkBarCodeAnalyzeList);
                    //RV_ResultInfoTable.setAdapter(scanResult_CheckRvAdapter);
                    subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
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
        if (!Is_CheckNumber_Mode) {
            if (scanTask_check_rvAdapter.getselection() >= 0) {
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
                                ViseLog.i("IFnfo = " + Info);
                                InputStream IsBarCodeInfoHead = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                InputStream IsBarCodeInfoBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                List<BarCodeHeadBean> BarCodeInfoHeadList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                List<BarcodeXmlBean> barcodeXmlBeanList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_SEQUENCE.toLowerCase())) {//如果产品是序列号
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    IsSerialNumber = false;
                                    IsAddSerialNumber = true;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");
                                } else if (!TextUtils.isEmpty(BarCodeInfoHeadList.get(3).getFQty())) {
                                    //算法  fqty * FUnitRate / baseqty
                                    float ThisPutSum = Tools.StringOfFloat(BarCodeInfoHeadList.get(3).getFQty());
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(String.valueOf(ThisPutSum));
                                    ViseLog.i("有数量 = " + FBarcodeLib);
                                } else if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||//批次码或者通用码
                                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                                    Is_CheckNumber_Mode = true;
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
                CheckBarCodeCheckTask barCodeCheckTask = new CheckBarCodeCheckTask(quitbarCodeCheckPort, webService, checkBodyDataList.get(RV_ScanInfoTableIndex).getFMaterial(),
                        materialModeBeanList.get(Sp_LabelModeIndex).getFGuid(), data);
                barCodeCheckTask.execute();
            } else {
                if (!TextUtils.isEmpty(msg.data)) {
                    int pos = GetSpinnerPos(stockBeanList, msg.data);
                    if (pos != -1) {
                        Sp_CheckHouseSpace.setSelection(pos);
                    }
                }
            }
        }
    }

    public void SubmitData(String QuitSum) {
        float SubBodySum;
        SubBodySum = 0;
        try {
            if (Is_CheckNumber_Mode) {
                if (true) {
                    if (CheckGuid(CheckFGuid, FBarcodeLib)) {
                        IsSerialNumber = true;
                        if (true) {
                            if (IsAddSerialNumber) {
                                CheckFGuid.add(FBarcodeLib);
                                IsAddSerialNumber = false;
                            }
                            Sp_CheckHouseSpace.setEnabled(false);
                            Sp_Material.setEnabled(false);
                            Drawable BorderInputHouseSpace = getResources().getDrawable(R.drawable.border);
                            Sp_CheckHouseSpace.setBackground(BorderInputHouseSpace);
                            Sp_Material.setBackground(BorderInputHouseSpace);

                            IsSave = true;
                            Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                            TV_Sumbit.setBackground(drawable_purple);
                            TV_Sumbit.setTextColor(getResources().getColor(R.color.White));
                            TV_Save.setBackground(drawable_purple);
                            TV_Save.setTextColor(getResources().getColor(R.color.White));
                            //累加子分录的盘点数量CheckQty
                            for (int i = 0; i < checkSubBodyDataList.size(); i++) {
                                if (checkSubBodyDataList.get(i).getFBarcodeLib_Name().equals(checkBarCodeAnalyzeList.get(0).getValue())) {
                                    float Check = Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty());
                                    float subChek = Tools.StringOfFloat(QuitSum);
                                    checkSubBodyDataList.get(i).setFCheckQty(String.valueOf(subChek+Check));//判断条码解析出来的条码库ID是否存在在子分录集合中是否存在，没有的话添加
                                    SubBodySum += Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty());
                                }
                                for (int j = 0;j<checkBodyDataList.size();j++){
                                    if (checkBodyDataList.get(j).getFGuid().equals(checkSubBodyDataList.get(i).getFBillBodyID())){
                                        //更新分录和子分录的盘点数量
                                        String SetFThisAux = String.valueOf(SubBodySum);
                                        checkBodyDataList.get(j).setFCheckQty(checkSubBodyDataList.get(i).getFCheckQty());
                                    }
                                }
                            }
                            ViseLog.i("SubBodySum = " + String.valueOf(SubBodySum));
                            subbody_CheckRvAdapter.notifyDataSetChanged();
                            scanTask_check_rvAdapter.notifyDataSetChanged();
//                            //为最后生成入库单保存数据array
//                            float NewNoPut = Tools.StringOfFloat(checkSubBodyDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkSubBodyDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
//                            String SetNoInput = String.valueOf(NewNoPut);
//                            checkSubBodyDataList.get(RV_ScanInfoTableIndex).setFDiffQty(SetNoInput);

                            if (true) {
                                //scanTask_check_rvAdapter.notifyDataSetChanged();
                                Is_CheckNumber_Mode = false;
                            }
                        }
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
        String Check = "";
        for (int i = 0; i < checkSubBodyDataList.size(); i++) {
            if (checkSubBodyDataList.get(i).getFBarcodeLib_Name().equals(checkBarCodeAnalyzeList.get(0).getValue())) {
                 Check = checkSubBodyDataList.get(i).getFCheckQty();
            }
        }
        ViseLog.i("CheckSumLibrary = " + Check);
        return Check;
    }

    public void PutResultArray(List<BarcodeXmlBean> barcodeXmlBeans) {
        if (barcodeXmlBeans.size() > 0) {
            checkBarCodeAnalyzeList.clear();
            for (BarcodeXmlBean barcodeXmlBean : barcodeXmlBeans) {
                ChildCheckTag childQuitTag = new ChildCheckTag();
                childQuitTag.setName(barcodeXmlBean.getFBarcodeName());
                childQuitTag.setValue(barcodeXmlBean.getFBarcodeContent());
                checkBarCodeAnalyzeList.add(childQuitTag);
            }
            scanResult_CheckRvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 保存按钮事件，传0
     */
    private void SubmitBillData() {

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
        CheckBillCreateTask checkBillCreateTask = new CheckBillCreateTask(checkHeadDataList, checkBodyDataList, checkSubBodyDataList, webService, myProgressDialog, checkBillCreate, "0");
        checkBillCreateTask.execute();
    }

    /**
     * 结案按钮事件，传1
     */
    private void SaveBillData() {
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
        CheckBillCreateTask checkBillCreateSaveTask = new CheckBillCreateTask(checkHeadDataList, checkBodyDataList, checkSubBodyDataList, webService, myProgressDialog, checkBillCreate, "1");
        checkBillCreateSaveTask.execute();
    }

    public boolean CheckGuid(List<String> Check, String Result) {
        if (IsSerialNumber) {
            return true;
        }
        if (Tools.IsObjectNull(Check) && !TextUtils.isEmpty(Result)) {
            if (!Check.contains(Result)) { //checkGuid不包含result，即扫描的Guid不重复
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void ShowEditSumDialog(String Sum) {
        ShowResultListDialog.getSingleton().Show(MContect, checkBodyDataList.get(RV_ScanInfoTableIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowResultListDialog.getSingleton().Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, checkBodyDataList.get(RV_ScanInfoTableIndex).getFBaseUnit_Name(), checkBarCodeAnalyzeList);
    }

    private void CleanData() {
        Is_CheckNumber_Mode = false;
        checkBarCodeAnalyzeList.clear();//清除条码解析列表
        scanResult_CheckRvAdapter.notifyDataSetChanged();//扫描结果刷新
        scanTask_check_rvAdapter.setSelection(-1);
        scanTask_check_rvAdapter.notifyDataSetChanged();//分录数据刷新
        ViseLog.i("Click CleanData");
    }

    /**
     * 筛选分录行下面的子分录
     *
     * @param taskRvData
     * @param checkSubBodies
     * @param pos
     * @return
     */
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

    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    private void RvBodyItemClick(final int position) {
        if (!Is_CheckNumber_Mode) {
            if (scanTask_check_rvAdapter.getselection() != position) {
                LockResultPort lockResultPort = new LockResultPort() {
                    @Override
                    public void onStatusResult(String res) {
                        myProgressDialog.dismiss();
                        if (res.equals("Success")) {
                            if (RV_ScanInfoTableIndex != position) {
                                RV_ScanInfoTableIndex = position;
                            }
                            if (!checkSubBodyDataList.get(position).getFGuid().isEmpty()) {
                                checkBodyDataList = SumList(checkBodyDataList, checkSubBodyDataList, position);//调用SumList方法，筛选子分录数据
                                checkSubBodyList = checkBodyDataList.get(position).getCheckSubBody();//添加筛选的子分录数据
                            } else {
                                    CheckSubBody checkSubBodyData = new CheckSubBody();
                                    List<CheckSubBody> checkSubBodyDataArrayList = new ArrayList<CheckSubBody>();
                                    String guid = new CreateGuid().toString();
                                    checkSubBodyData.setFGuid(guid);
                                    checkSubBodyData.setFBillBodyID(checkBodyDataList.get(position).getFGuid());
                                    checkSubBodyData.setFBarcodeLib_Name(checkBodyDataList.get(position).getFMaterial_Code());
                                    checkSubBodyData.setFAccountQty("0");
                                    checkSubBodyData.setFCheckQty("0");
                                    checkSubBodyData.setFDiffQty("0");
                                    checkSubBodyDataArrayList.add(checkSubBodyData);
                                    checkSubBodyDataList.add(checkSubBodyDataArrayList.get(0));
                                ViseLog.i("checkSubBodyDataArrayList.get(0) = " + checkSubBodyDataArrayList.get(0));
                            }
                            if(Tools.IsObjectNull(checkSubBodyStocksAdapter)){
                                checkSubBodyStocksAdapter.notifyDataSetChanged();
                            }
                            GetMaterialMode getMaterialMode = new GetMaterialMode();
                            getMaterialMode.execute(checkBodyDataList.get(position).getFMaterial());
                            scanTask_check_rvAdapter.setSelection(position);
                            scanTask_check_rvAdapter.notifyDataSetChanged();//刷新选中
                            if (SubBodyMaterialList.size() > 0) {
                                for (int i = 0; i < SubBodyMaterialList.size(); i++) {
                                    CheckM.add(SubBodyMaterialList.get(i).getFName());
                                }
                            }
                            if(Tools.IsObjectNull(subbody_CheckRvAdapter)){
                                subbody_CheckRvAdapter.notifyDataSetChanged();
                            }
                            if (!CheckM.contains(checkBodyDataList.get(position).getFMaterial_Name()) && checkHeadDataList.get(0).getFAllowOtherMaterial().equals("1")) {
                                CheckM.add(checkBodyDataList.get(position).getFMaterial_Name());
                                CheckBodyMaterial checkBodyMaterial = new CheckBodyMaterial();
                                checkBodyMaterial.setFName(checkBodyDataList.get(position).getFMaterial_Name() + " " + checkBodyDataList.get(position).getFModel());//取出分录选中的物料名
                                SubBodyMaterialList.add(checkBodyMaterial);//把分录物料list加入单头物料list
//                                AsyncGetMaterialCell asyncGetMaterialCell = new AsyncGetMaterialCell(position, checkBodyDataList.get(position).getFMaterial_Name() + " " + checkBodyDataList.get(position).getFModel());
//                                asyncGetMaterialCell.execute();
                            } else {
                                for (int i = 0; i < SubBodyMaterialList.size(); i++) {
                                    if (SubBodyMaterialList.get(i).getFName().equals(checkBodyDataList.get(position).getFMaterial_Name())) {
//                                        AsyncGetMaterialCell asyncGetMaterialCell = new AsyncGetMaterialCell(position, SubBodyMaterialList.get(i).getFName());
//                                        asyncGetMaterialCell.execute();
                                    }
                                }
                            }

                        } else {
                            tools.ShowDialog(MContect, res);
                        }
                    }
                };
                CheckBodyLockTask quitBodyLockTask = new CheckBodyLockTask(lockResultPort, webService, checkBodyDataList.get(position).getFGuid(), myProgressDialog);
                quitBodyLockTask.execute();
            } else {
                if (Tools.IsObjectNull(checkBarCodeAnalyzeList)) {
                    checkBarCodeAnalyzeList.clear();
                }
                if (Tools.IsObjectNull(scanResult_CheckRvAdapter)) {
                    scanResult_CheckRvAdapter.notifyDataSetChanged();
                }
                if (Tools.IsObjectNull(scanTask_check_rvAdapter)) {
                    scanTask_check_rvAdapter.setSelection(-1);
                }
                if (Tools.IsObjectNull(scanTask_check_rvAdapter)) {
                    scanTask_check_rvAdapter.notifyDataSetChanged();
                }
                if (Tools.IsObjectNull(checkSubBodyList)) {
                    checkSubBodyList.clear();
                }
                if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
                    subbody_CheckRvAdapter.notifyDataSetChanged();
                }
            }
        } else {
            tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或提交");
        }
    }
}
