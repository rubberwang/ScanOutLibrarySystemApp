package cn.shenzhenlizuosystemapp.Common.UI.CheckActivity;

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
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckAdapterReturn;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckBodyMaterial;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckMaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree.TreeMBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckBillCreate;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckMaterialAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckMaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckSubBodyStocksAdapter;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeListActivity;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.View.ShowResultListDialog;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckBean.CheckLibraryAllInfo;
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
    private int SpCheckHouseSpaceIndex = 0;//仓位下标
    private int Sp_LabelModeIndex = 0;//标签模板列表下标
    private boolean Is_CheckNumber_Mode = false;
    private String FBarcodeLib = "";
    private int RefreshStatu = 1;
    private boolean Is_Single = false;
    private String IsAllow = "";//是否允许选择物料
    private boolean IsSerialNumber = true;//是否连续扫描
    private boolean IsSave = false;
    private boolean isInitial = true;
    private float subChek = 0;

    //数组
    private List<ChildCheckTag> checkBarCodeAnalyzeList = null;//条码解析列表
    private List<CheckLibraryDetail> checkHeadDataList = null;//单头列表
    private List<CheckTaskRvData> checkBodyDataList = null;//分录列表
    private List<CheckSubBody> checkSubBodyDataList = null;//子分录列表
    private List<CheckSubBody> checkSubBodyList = null;
    private List<CheckSubBody> checkSubBodyList1 = new ArrayList<CheckSubBody>();
    private List<BarCodeHeadBean> BarCodeInfoHeadList = null;
    private List<CheckStockBean> stockBeanList = null;//仓位列表
    private List<CheckStockBean> SubBodyStocksList = null;//子分录物料仓位列表
    private List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<CheckMaterialModeBean>();//标签模板
    private List<CheckBodyMaterial> SubBodyMaterialList = null;//盘点物料列表
    private List<CheckBodyMaterial> SubBodyMaterial = null;

    //类
    private Context MContect;
    private Tools tools = null;
    private WebService webService;
    private ScanResult_CheckRvAdapter scanResult_CheckRvAdapter;//扫描解析结果适配
    private ScanTask_CheckRvAdapter scanTask_check_rvAdapter;//分录适配
    private Subbody_CheckRvAdapter subbody_CheckRvAdapter;//子分录适配
    private EditSumPort editSumPort;
    private LinearLayoutManager ScanTaskL;

    //控件
    private TextView Back;
    private TextView TV_DeliverGoodsNumber;//通知单号
    private TextView TV_house;
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
        CheckLibraryObServer checkLibraryObServer;
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
        int m = 0;
        int n = 0;
        TreeMBean treeMBean;
        treeMBean = (TreeMBean) getIntent().getSerializableExtra("M");
        ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_BodyInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_BodyInfoTable.setLayoutManager(ScanTaskL);
        scanTask_check_rvAdapter = new ScanTask_CheckRvAdapter(this, checkBodyDataList);
        RV_BodyInfoTable.setAdapter(scanTask_check_rvAdapter);

        if (Tools.IsObjectNull(treeMBean)) {
            ViseLog.i("treeMBean.getFName() = " + treeMBean.getFName());
            CheckTaskRvData checkTaskRvData = new CheckTaskRvData();
            CheckBodyMaterial checkBodyMaterial = new CheckBodyMaterial();
            CheckMaterialModeBean checkMaterialModeBean = new CheckMaterialModeBean();
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
            checkTaskRvData.setFAccountQty("0.0");
            checkTaskRvData.setFCheckQty("0.0");
            checkTaskRvData.setFDiffQty("0.0");
            checkTaskRvDataArrayList.add(checkTaskRvData);
            if (IsAllow.equals("0")) {//如果不允许添加物料
                for (int j = 0; j < SubBodyMaterialList.size(); j++) {
                    if (SubBodyMaterialList.get(j).getFGuid().equals(treeMBean.getFCode())) {//若新增物料存在物料集合中，跳转位置，并选中
                        MoveToPosition(ScanTaskL, RV_BodyInfoTable, checkBodyDataList.size());
                        RvBodyItemClick(checkBodyDataList.size());
                        scanTask_check_rvAdapter.notifyDataSetChanged();
                        ConnectStr.ISMaterialExist = true;
                    } else {//若不存在，提示
                        ConnectStr.ISMaterialExist = false;
                    }
                }
            } else {//若允许，添加进去
                for (int pos = 0; pos < checkBodyDataList.size(); pos++) {
                    if (checkBodyDataList.get(pos).getFMaterial().equals(treeMBean.getFGuid())) {//若已存在添加的物料
                        m = pos;
                        MoveToPosition(ScanTaskL, RV_BodyInfoTable, pos);//移动到该物料
                        RvBodyItemClick(pos);//点击事件
                        checkBodyMaterial.setFName(treeMBean.getFName());
                        MaterialList.add(checkBodyMaterial);
                        SubBodyMaterialList.add(MaterialList.get(0));//在盘点物料里面添加显示选择的物料名
                        ConnectStr.ISMaterialExist = true;
                    }
                }
                if (!checkBodyDataList.get(m).getFMaterial().equals(treeMBean.getFGuid())) {
                    n = checkBodyDataList.size();
                    checkBodyDataList.add(checkTaskRvDataArrayList.get(0));//添加该物料
                    checkBodyMaterial.setFName(treeMBean.getFName());

                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, n);//移动到该物料
                    RvBodyItemClick(n);//点击事件
                    //MaterialList.add(checkBodyMaterial);
                    //SubBodyMaterialList.add(MaterialList.get(0));//在盘点物料里面添加显示选择的物料名
                    scanTask_check_rvAdapter.notifyDataSetChanged();
                    //subbody_CheckRvAdapter.notifyDataSetChanged();
                    ConnectStr.ISMaterialExist = true;

                }
            }
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
//                        if (checkSubBodyDataList.size()>0) {
//                            stockBeanList = StockList(stockBeanList, checkSubBodyDataList, SpCheckHouseSpaceIndex);//调用SumList方法，筛选子分录数据
//                            checkSubBodyList1 = stockBeanList.get(SpCheckHouseSpaceIndex).getCheckSubBody();//筛选的子分录数据
//                            if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
//                                if (scanTask_check_rvAdapter.getselection() >= 0){
//                                    subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList1);
//                                    RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
//                                }
//                            }
//                        }
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


        @Override
        protected List<CheckBodyMaterial> doInBackground(String... params) {
            List<CheckBodyMaterial> SubBodyMaterial = null;
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
            CheckMaterialAdapter QuitStockAdapter;
            if (result.size() >= 0) {
                QuitStockAdapter = new CheckMaterialAdapter(result, CheckLibraryActivity.this);
                Sp_Material.setAdapter(QuitStockAdapter);
                Sp_Material.setSelection(pos);
                Sp_Material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (SpCheckHouseSpaceIndex != i) {
                                SpCheckHouseSpaceIndex = i;
                            }
                            for (int pos = 0; pos < checkBodyDataList.size(); pos++) {
                                ViseLog.i("result GUid = " + result.get(i).getFGuid());
                                if (checkBodyDataList.get(pos).getFMaterial().equals(result.get(i).getFGuid())) {
                                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, pos);
                                    RvBodyItemClick(pos);
                                    scanTask_check_rvAdapter.notifyDataSetChanged();
                                }else if (!SubBodyMaterialList.isEmpty()&&!checkBodyDataList.get(pos).getFMaterial().equals(result.get(i).getFGuid())){
                                    CheckTaskRvData checkTaskRvData = new CheckTaskRvData();
                                    String BodyGuid = new CreateGuid().toString();//随机生成分录Guid；
                                    checkTaskRvData.setFGuid(BodyGuid);
                                    checkTaskRvData.setFMaterial(SubBodyMaterialList.get(i).getFGuid());
                                    checkTaskRvData.setFMaterial_Code(SubBodyMaterialList.get(i).getFCode());
                                    checkTaskRvData.setFMaterial_Name(SubBodyMaterialList.get(i).getFName());
                                    checkTaskRvData.setFModel(SubBodyMaterialList.get(i).getFModel());
                                    checkTaskRvData.setFBaseUnit(SubBodyMaterialList.get(i).getFBaseUnit());
                                    checkTaskRvData.setFBaseUnit_Name(SubBodyMaterialList.get(i).getFBaseUnit_Name());
                                    checkTaskRvData.setFRowIndex(String.valueOf(checkBodyDataList.size()));
                                    checkTaskRvData.setFAccountQty("0.0");
                                    checkTaskRvData.setFCheckQty("0.0");
                                    checkTaskRvData.setFDiffQty("0.0");
                                    checkBodyDataList.add(checkTaskRvData);
//                                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, checkBodyDataList.size());//移动到该物料
//                                    RvBodyItemClick(checkBodyDataList.size());//点击事件
                                    scanTask_check_rvAdapter.notifyDataSetChanged();
                                }else {

                                }
                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }else {

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
                    AsyncGetStocksCell asyncGetStocksCell = new AsyncGetStocksCell();
                    asyncGetStocksCell.execute();
                    AsyncGetMaterialCell asyncGetMaterialCell = new AsyncGetMaterialCell();
                    asyncGetMaterialCell.execute();
                    TV_house.setText(result.get(0).getFStock_Name());
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    IsAllow = result.get(0).getFAllowOtherMaterial();
                    if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
                        subbody_CheckRvAdapter.notifyDataSetChanged();
                    }
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

    /**
     * 获取标签模板
     */
    private class GetMaterialMode extends AsyncTask<String, Void, List<CheckMaterialModeBean>> {

        @Override
        protected List<CheckMaterialModeBean> doInBackground(String... params) {
            String ModeXml = "";
            List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<>();
            List<CheckAdapterReturn> adapterReturnList;
            try {
                ModeXml = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, checkBodyDataList.get(RV_ScanInfoTableIndex).getFMaterial());
                //ModeXml = webService.GetMaterialLabelTemplet(ConnectStr.ConnectionToString, treeMBean.getFGuid());
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
                    ViseLog.i("获取物料参数  = " + list_return.get(0).getFInfo());
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
                    InitSubBodyRecycler();
                } else {
                    scanResult_CheckRvAdapter = new ScanResult_CheckRvAdapter(MContect, checkBarCodeAnalyzeList);
                    subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
                    RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                }
            }
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


    /*
     * EventBus触发回调
     * 接收到pda扫描返回的数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(BarCodeMessage msg) {
        if (!Is_CheckNumber_Mode) {//判断是否选择物料
            if (scanTask_check_rvAdapter.getselection() >= 0) {
                String data = msg.data;
                ViseLog.i("messageEventBus msg = " + data);//条码解析数据，每一列换行。
                if (!Is_Single) {
                    data = data.replace("\n", "；");
                    data = data.substring(0, data.length() - 1);
                }
                ViseLog.i("messageEventBus msg = " + data);//条码解析数据，不换行，以“；”符号分割
                CheckBarCodeCheckPort quitbarCodeCheckPort = new CheckBarCodeCheckPort() {
                    @Override
                    public void onData(String Info) {
                        boolean ISExist = false;
                        try {
                            //判断物料条码管控类型
                            if (!Info.substring(0, 2).equals("EX")) {
                                ViseLog.i("IFnfo = " + Info);//条码解析成功后返回Info数据
                                InputStream IsBarCodeInfoHead = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                InputStream IsBarCodeInfoBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                BarCodeInfoHeadList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                List<BarcodeXmlBean> barcodeXmlBeanList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_SEQUENCE.toLowerCase())) {//如果产品是序列号
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ViseLog.i("FBarcodeLib = " + FBarcodeLib);
                                    IsSerialNumber = false;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");

                                    //定位当前分录的子分录

                                    for (int i = 0; i < checkSubBodyDataList.size(); i++) {
                                        //循环分录
                                        if (checkSubBodyDataList.get(i).getFBarcodeLib().equals(FBarcodeLib)) {//匹配出子分录的物料
                                            ISExist = true;
                                            float subCheck = Tools.StringOfFloat("1.0");
                                            checkSubBodyDataList.get(i).setFCheckQty(String.valueOf(subCheck));//把当前子分录盘点数量为1.0；
                                            float SubBodyCheck = Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty());
                                            float subBodyDiff = Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty()) - (Tools.StringOfFloat(checkSubBodyDataList.get(i).getFAccountQty()));
                                            String subDiff = String.valueOf(subBodyDiff);
                                            checkSubBodyDataList.get(i).setFDiffQty(subDiff);//计算当前子分录差异数量
                                            if (subBodyDiff > 0) {
                                                checkSubBodyDataList.get(i).setFCheckStockStatus("49E79140-94CA-4B43-988A-D3E2FE5BDEC6");//盘盈
                                            } else if (subBodyDiff < 0) {
                                                checkSubBodyDataList.get(i).setFCheckStockStatus("124164D2-6B47-4614-8B0D-9212A459D1E2");//盘亏
                                            } else {
                                                checkSubBodyDataList.get(i).setFCheckStockStatus("108A8304-083C-4370-AE5C-D2E43C91CE21");//匹配
                                            }
                                            subbody_CheckRvAdapter.notifyDataSetChanged();

                                            float subBodyCheck = Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFCheckQty());
                                            subBodyCheck += SubBodyCheck;
                                            checkBodyDataList.get(RV_ScanInfoTableIndex).setFCheckQty(String.valueOf(subBodyCheck));//计算分录盘点数量
                                            float BodyDiff = Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                                            String Diff = String.valueOf(BodyDiff);
                                            checkBodyDataList.get(RV_ScanInfoTableIndex).setFDiffQty(Diff);//计算当前分录差异数量
                                            scanTask_check_rvAdapter.notifyDataSetChanged();

                                        }

                                    }

                                    if (!ISExist) {
                                        int h = checkSubBodyDataList.size() + 1;
                                        CheckSubBody checkSubBodyData = new CheckSubBody();
                                        List<CheckSubBody> checkSubBodyDataArrayList = new ArrayList<CheckSubBody>();
                                        String guid = new CreateGuid().toString();
                                        checkSubBodyData.setFGuid(guid);
                                        checkSubBodyData.setFBillBodyID(checkBodyDataList.get(RV_ScanInfoTableIndex).getFGuid());
                                        checkSubBodyData.setFRowIndex(String.valueOf(h));
                                        checkSubBodyData.setFStockCell(stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
                                        checkSubBodyData.setFStockCell_Name(stockBeanList.get(SpCheckHouseSpaceIndex).getFName());
                                        checkSubBodyData.setFBarcodeLib(FBarcodeLib);
                                        checkSubBodyData.setFBarcodeType("33A50386-F167-4913-95C8-B7AE69B8CB55");
                                        checkSubBodyData.setFBarcodeLib_Name(barcodeXmlBeanList.get(3).getFBarcodeContent() + "|" + barcodeXmlBeanList.get(0).getFBarcodeContent());
                                        checkSubBodyData.setFRowIndex(String.valueOf(checkSubBodyDataList.size()));
                                        checkSubBodyData.setFCheckQty("1.0");
                                        checkSubBodyData.setFAccountQty("0.0");
                                        checkSubBodyData.setFDiffQty("1.0");
                                        checkSubBodyData.setFCheckStockStatus("49E79140-94CA-4B43-988A-D3E2FE5BDEC6");
                                        checkSubBodyData.setFBarcodeType_Name("序列号");
                                        checkSubBodyDataArrayList.add(checkSubBodyData);
                                        checkSubBodyList = new ArrayList<CheckSubBody>();
                                        if (!checkBodyDataList.get(RV_ScanInfoTableIndex).getFAccountQty().equals("0.0")) {
                                            checkSubBodyList.add(checkSubBodyData);
                                            checkSubBodyDataList.add(checkSubBodyDataArrayList.get(0));
                                            subbody_CheckRvAdapter.notifyDataSetChanged();
                                        } else {
                                            checkSubBodyList.add(checkSubBodyData);
                                            checkSubBodyDataList.add(checkSubBodyData);
                                            if (Tools.IsObjectNull(checkSubBodyList)) {
                                                LinearLayoutManager ScanTaskL = new LinearLayoutManager(MContect);
                                                ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
                                                RV_SubBodyInfoTable.addItemDecoration(new RvLinearManageDivider(MContect, LinearLayoutManager.VERTICAL));
                                                RV_SubBodyInfoTable.setLayoutManager(ScanTaskL);
                                                subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
                                                RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                                            }
                                        }
                                        float subBodyCheck = 0;
                                        for (int i = 0; i < checkSubBodyList.size(); i++) {
                                            float SubBodyCheck = Tools.StringOfFloat(checkSubBodyList.get(i).getFCheckQty());
                                            subBodyCheck += SubBodyCheck;
                                        }
                                        checkBodyDataList.get(RV_ScanInfoTableIndex).setFCheckQty(String.valueOf(subBodyCheck));//计算分录盘点数量
                                        float BodyDiff = Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                                        String Diff = String.valueOf(BodyDiff);
                                        checkBodyDataList.get(RV_ScanInfoTableIndex).setFDiffQty(Diff);//计算当前分录差异数量
                                        scanTask_check_rvAdapter.notifyDataSetChanged();
                                    }


                                } else if (!TextUtils.isEmpty(BarCodeInfoHeadList.get(3).getFQty())) {
                                    //算法  fqty * FUnitRate / baseqty
                                    float ThisPutSum = Tools.StringOfFloat(BarCodeInfoHeadList.get(3).getFQty());
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(String.valueOf(ThisPutSum));
                                    ViseLog.i("有数量 = " + FBarcodeLib);
                                    //循环子分录
                                } else if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||//批次码或者通用码
                                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ShowEditSumDialog(NoQuitLibrary());
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
                //调用条码解析
                CheckBarCodeCheckTask barCodeCheckTask = new CheckBarCodeCheckTask(quitbarCodeCheckPort, webService, checkBodyDataList.get(RV_ScanInfoTableIndex).getFMaterial(),
                        materialModeBeanList.get(Sp_LabelModeIndex).getFGuid(), data, "0");
                barCodeCheckTask.execute();
            }
        } else {
            tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或保存");
        }
    }

    public void SubmitData(String QuitSum) {
        try {
            if (Is_CheckNumber_Mode) {
                Sp_CheckHouseSpace.setEnabled(false);
                Sp_Material.setEnabled(false);
                Drawable BorderInputHouseSpace = getResources().getDrawable(R.drawable.border);
                Sp_CheckHouseSpace.setBackground(BorderInputHouseSpace);
                Sp_Material.setBackground(BorderInputHouseSpace);
                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_COMMON) ||//批次码或者通用码
                        BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_BATCH.toLowerCase())) {
                    //累加子分录的盘点数量CheckQty
                    for (int i = 0; i < checkSubBodyDataList.size(); i++) {
                        if (checkSubBodyDataList.get(i).getFBarcodeLib().equals(FBarcodeLib)) {
                            float Check = Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty());
                            float subChek = Tools.StringOfFloat(String.valueOf((QuitSum)));
                            checkSubBodyDataList.get(i).setFCheckQty(String.valueOf(subChek + Check));//判断条码解析出来的条码库ID是否存在在子分录集合中是否存在，没有的话添加

                            float NewNoPut = Tools.StringOfFloat(checkSubBodyDataList.get(i).getFCheckQty()) - (Tools.StringOfFloat(checkSubBodyDataList.get(i).getFAccountQty()));
                            String SetNoInput = String.valueOf(NewNoPut);
                            checkSubBodyDataList.get(i).setFDiffQty(SetNoInput);
                            if (NewNoPut > 0) {
                                checkSubBodyDataList.get(i).setFCheckStockStatus("49E79140-94CA-4B43-988A-D3E2FE5BDEC6");//盘盈
                            } else if (NewNoPut < 0) {
                                checkSubBodyDataList.get(i).setFCheckStockStatus("124164D2-6B47-4614-8B0D-9212A459D1E2");//盘亏
                            } else {
                                checkSubBodyDataList.get(i).setFCheckStockStatus("108A8304-083C-4370-AE5C-D2E43C91CE21");//匹配
                            }

                            //更新分录盘点数量
                            checkBodyDataList.get(RV_ScanInfoTableIndex).setFCheckQty(checkSubBodyDataList.get(i).getFCheckQty());
                            float BodyNoPut = Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                            String BodyNoInput = String.valueOf(BodyNoPut);
                            checkBodyDataList.get(RV_ScanInfoTableIndex).setFDiffQty(BodyNoInput);
                            subbody_CheckRvAdapter.notifyDataSetChanged();
                            scanTask_check_rvAdapter.notifyDataSetChanged();
                        }
                    }
                }

                IsSave = true;
                Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                TV_Sumbit.setBackground(drawable_purple);
                TV_Sumbit.setTextColor(getResources().getColor(R.color.White));
                TV_Save.setBackground(drawable_purple);
                TV_Save.setTextColor(getResources().getColor(R.color.White));
                Is_CheckNumber_Mode = false;
            } else {
                tools.ShowDialog(MContect, "盘点数量为空或小于0");
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
                Check = checkSubBodyDataList.get(i).getFAccountQty();
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
        scanTask_check_rvAdapter.setSelection(-1);
        scanTask_check_rvAdapter.notifyDataSetChanged();//分录数据刷新
        ViseLog.i("Click CleanData");
    }

    /**
     * 筛选分录行下面的子分录
     */
    private List<CheckTaskRvData> SumList(List<CheckTaskRvData> taskRvData, List<CheckSubBody> checkSubBodies, int pos) {
        List<CheckSubBody> subBodies = new ArrayList<>();

        for (int i = 0; i < checkSubBodies.size(); i++) {
            if (taskRvData.get(pos).getFGuid().equals(checkSubBodies.get(i).getFBillBodyID())) {//找出相匹配的分录和子分录
                subBodies.add(checkSubBodies.get(i));//点击下标为pos的分录获得下标为i的子分录list
            }
        }
        if (subBodies.size() > 0) {
            taskRvData.get(pos).setCheckSubBody(subBodies);//把下标为i的子分录数据传入CheckTakData的setCheckSubBody方法中
        }
        return taskRvData;//返回taskRvData
    }

    private List<CheckStockBean> StockList(List<CheckStockBean> stockBeans, List<CheckSubBody> checkSubBodies, int pos) {
        List<CheckSubBody> subBodies = new ArrayList<>();

        for (int i = 0; i < checkSubBodies.size(); i++) {
            if (stockBeans.get(pos).getFGuid().equals(checkSubBodies.get(i).getFStockCell())) {//找出相匹配的分录和子分录
                subBodies.add(checkSubBodies.get(i));//点击下标为pos的分录获得下标为i的子分录list
            }
        }
        if (subBodies.size() > 0) {
            stockBeans.get(pos).setCheckSubBody(subBodies);//把下标为i的子分录数据传入CheckTakData的setCheckSubBody方法中
        }
        return stockBeans;//返回taskRvData
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
//                            if (checkSubBodyList1.size()>0) {
//                                checkBodyDataList = SumList(checkBodyDataList, checkSubBodyList1, position);//调用SumList方法，筛选子分录数据
//                                checkSubBodyList = checkBodyDataList.get(position).getCheckSubBody();//添加筛选的子分录数据
//                            }
                            if (checkSubBodyDataList.size() > 0) {
                                checkBodyDataList = SumList(checkBodyDataList, checkSubBodyDataList, position);//调用SumList方法，筛选子分录数据
                                checkSubBodyList = checkBodyDataList.get(position).getCheckSubBody();//添加筛选的子分录数据
                            }
                            GetMaterialMode getMaterialMode = new GetMaterialMode();//获取标签模板
                            getMaterialMode.execute(checkBodyDataList.get(position).getFMaterial());
                            scanTask_check_rvAdapter.setSelection(position);
                            scanTask_check_rvAdapter.notifyDataSetChanged();//刷新选中
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
