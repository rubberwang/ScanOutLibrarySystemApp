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

import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.ScanTask_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter.Subbody_CheckRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBarCodeCheckTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBillCreateTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckGetLabelTempletTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckGetTagModeTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.CheckTask.CheckStockCellTask;
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
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree.TreeMBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckBarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckGetLabelTempletPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckTagModePort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.CheckPort.CheckBillCreate;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckMaterialAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckStockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter.CheckMaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeListActivity;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.View.ShowResultListDialog;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckBean.CheckLibraryAllInfo;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXml.CheckLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class CheckLibraryActivity extends BaseActivity {

    //标识符
    private String FGUID = "";//通知单Guid
    private int RV_ScanInfoTableIndex = 0;//分录列表下标
    private int SpCheckHouseSpaceIndex = 0;//仓位下标
    private int Sp_LabelModeIndex = 0;//标签模板列表下标
    private int Sp_MaterialIndex = 0;
    private boolean Is_CheckNumber_Mode = false;
    private boolean Is_Single = false;//是否单条码模式
    private String IsAllow = "";//是否允许选择物料
    private boolean IsSerialNumber = true;//是否连续扫描
    private boolean IsSave = false;//是否保存
    private String FBarcodeLib = "";//条码解析的条码库
    private int RefreshStatu = 1;
    private boolean Shield_Sp_Default_Event = false;

    //数组
    private List<ChildCheckTag> checkBarCodeAnalyzeList = null;//条码解析列表
    private List<CheckLibraryDetail> checkHeadDataList = null;//单头列表
    private List<CheckTaskRvData> checkBodyAllDataList = null;//分录全集
    private List<CheckTaskRvData> checkBodyPartDataList = new ArrayList<>();//分录子集
    private List<CheckSubBody> checkSubBodyDataList = null;//子分录全集
    private List<CheckSubBody> checkSubBodyList = null;//子分录子集
    private List<BarCodeHeadBean> BarCodeInfoHeadList = null;//条码解析单头
    private List<BarcodeXmlBean> barcodeXmlBeanList = null;//条码解析单体
    private List<CheckStockBean> stockBeanList = null;//显示所有仓位列表
    private List<CheckStockBean> SubBodyStocksList = null;//接口返回仓位列表
    private List<CheckMaterialModeBean> materialModeBeanList = new ArrayList<CheckMaterialModeBean>();//标签模板集合
    private List<CheckBodyMaterial> SubBodyMaterialList = null;//物料集合
    private List<CheckStockBean> checkStockBeanList = new ArrayList<>();
    //类
    private Context MContect;
    private Tools tools = null;
    private WebService webService;
    private ScanTask_CheckRvAdapter scanTask_check_rvAdapter;//分录适配
    private Subbody_CheckRvAdapter subbody_CheckRvAdapter;//子分录适配
    private EditSumPort editSumPort;//输入数量弹窗
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
    private RecyclerView RV_BodyInfoTable;//存放分录list view
    private RecyclerView RV_SubBodyInfoTable;//存放子分录list view
    private Spinner Sp_Label;
    private MyProgressDialog myProgressDialog;
    private EditText ET_SuckUp;
    private ImageButton BT_CheckMaterial;

    private int GetSpinnerPos(List<CheckBodyMaterial> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFGuid().equals(value)) {
                return i;
            }
        }
        return -1;
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
        CheckLibraryObServer checkLibraryObServer;
        checkLibraryObServer = new CheckLibraryObServer();
        getLifecycle().addObserver(checkLibraryObServer);
        webService = WebService.getSingleton(MContect);
        InitClick();//按钮点击事件
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
     * 取消按钮点击事件
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
/**
 * 取消按钮点击事件
 */
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
/**
 * 保存按钮点击事件
 */
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
                ViewManager.getInstance().finishActivity(CheckLibraryActivity.this);
                Intent intent = new Intent(MContect, TreeListActivity.class);
                intent.putExtra("GUID", FGUID);
                startActivity(intent);
            }
        });
/**
 * 结案按钮点击事件
 */
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
            LinearLayoutManager ScanTaskL = new LinearLayoutManager(MContect);
            ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
            RV_SubBodyInfoTable.addItemDecoration(new RvLinearManageDivider(MContect, LinearLayoutManager.VERTICAL));
            RV_SubBodyInfoTable.setLayoutManager(ScanTaskL);
            subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
            RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
        }
    }

    /***
     * 分录列表适配
     * ***/
    private void InitScanRecycler() {
        ScanTaskL = new LinearLayoutManager(CheckLibraryActivity.this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_BodyInfoTable.addItemDecoration(new RvLinearManageDivider(CheckLibraryActivity.this, LinearLayoutManager.VERTICAL));
        RV_BodyInfoTable.setLayoutManager(ScanTaskL);
        scanTask_check_rvAdapter = new ScanTask_CheckRvAdapter(CheckLibraryActivity.this, checkBodyPartDataList);
        RV_BodyInfoTable.setAdapter(scanTask_check_rvAdapter);
        scanTask_check_rvAdapter.setOnItemClickLitener(new ScanTask_CheckRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                RvBodyItemClick(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        RvBodyItemClick(0);
        AddTreeList();
    }

    /*
    仓位初始化
     */
    private void InitCheckStockCell() {
        CheckStockCellPort checkStockCellPort = new CheckStockCellPort() {
            @Override
            public void OnRes(final List<CheckStockBean> checkStockBeanList) {
                if (SubBodyStocksList.size() > 0) {
                    stockBeanList = SubBodyStocksList;
                }
                if (checkStockBeanList.size() > 0) {
                    CheckStockAdapter QuitStockAdapter = new CheckStockAdapter(checkStockBeanList, MContect);
                    Sp_CheckHouseSpace.setAdapter(QuitStockAdapter);
                    Sp_CheckHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (Shield_Sp_Default_Event) {
                                if (Shield_Sp_Default_Event) {
                                    if (SpCheckHouseSpaceIndex != i) {
                                        SpCheckHouseSpaceIndex = i;
                                    }
                                    if (Tools.IsObjectNull(subbody_CheckRvAdapter) && Tools.IsObjectNull(checkSubBodyDataList)) {
                                        checkSubBodyList.clear();
                                        subbody_CheckRvAdapter.notifyDataSetChanged();
                                    }
                                    //筛选分录
                                    if (checkBodyAllDataList.size() > 0) {
                                        stockBeanList = checkStockBeanList;
                                        checkBodyPartDataList = GetBodyByStockCellList(checkBodyAllDataList, stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
                                        scanTask_check_rvAdapter = new ScanTask_CheckRvAdapter(MContect, checkBodyPartDataList);
                                        RV_BodyInfoTable.setAdapter(scanTask_check_rvAdapter);
                                        RvBodyItemClick(0);
                                        scanTask_check_rvAdapter.setOnItemClickLitener(new ScanTask_CheckRvAdapter.OnItemClickLitener() {
                                            @Override
                                            public void onItemClick(View view, final int position) {
                                                RvBodyItemClick(position);
                                                //筛选子分录
                                                if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
                                                    if (scanTask_check_rvAdapter.getselection() >= 0) {
                                                        subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
                                                        RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                                                        scanTask_check_rvAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onItemLongClick(View view, int position) {
                                            }
                                        });
                                    } else {
                                        tools.ShowDialog(MContect, "数据缺失加载异常");
                                    }
                                }
                            }
                            Shield_Sp_Default_Event = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
        };
        if (SubBodyStocksList.size() > 0) {
            CheckStockAdapter QuitStockAdapter = new CheckStockAdapter(SubBodyStocksList, MContect);
            Sp_CheckHouseSpace.setAdapter(QuitStockAdapter);
            Sp_CheckHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (Shield_Sp_Default_Event) {
                        if (SpCheckHouseSpaceIndex != i) {
                            SpCheckHouseSpaceIndex = i;
                        }
                        if (Tools.IsObjectNull(subbody_CheckRvAdapter) && Tools.IsObjectNull(checkSubBodyDataList)) {
                            checkSubBodyList.clear();
                            subbody_CheckRvAdapter.notifyDataSetChanged();
                        }
                        //筛选分录
                        if (checkBodyAllDataList.size() > 0) {
                            checkBodyPartDataList = GetBodyByStockCellList(checkBodyAllDataList, stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
                            scanTask_check_rvAdapter = new ScanTask_CheckRvAdapter(MContect, checkBodyPartDataList);
                            RV_BodyInfoTable.setAdapter(scanTask_check_rvAdapter);
                            RvBodyItemClick(0);
                            scanTask_check_rvAdapter.setOnItemClickLitener(new ScanTask_CheckRvAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, final int position) {
                                    RvBodyItemClick(position);
                                    Sp_MaterialIndex = position;
                                    //筛选子分录
                                    if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
                                        if (scanTask_check_rvAdapter.getselection() >= 0) {
                                            subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
                                            RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                                            scanTask_check_rvAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {
                                }
                            });
                        } else {
                            tools.ShowDialog(MContect, "数据缺失加载异常");
                        }
                    }
                    Shield_Sp_Default_Event = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else {
            CheckStockCellTask checkStockCellTask = new CheckStockCellTask(checkStockCellPort, webService, checkHeadDataList.get(0).getFStock());
            checkStockCellTask.execute();
        }
    }

    /**
     * 获取物料
     */
    private class AsyncGetMaterialCell extends AsyncTask<String, Void, List<CheckBodyMaterial>> {
        private int pos = 0;

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
            if (result.size() > 0) {
                QuitStockAdapter = new CheckMaterialAdapter(result, CheckLibraryActivity.this);
                Sp_Material.setAdapter(QuitStockAdapter);
                Sp_Material.setSelection(pos);
                Sp_Material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        if (!IsCLICK){
                        AddMaterialList(i);
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
     * 获取单头
     */
    public class GetCheckLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<CheckLibraryDetail>> {

        @Override
        protected List<CheckLibraryDetail> doInBackground(Integer... params) {
            checkHeadDataList = new ArrayList<>();
            checkBodyAllDataList = new ArrayList<>();
            checkSubBodyDataList = new ArrayList<>();
            SubBodyStocksList = new ArrayList<>();
            SubBodyMaterialList = new ArrayList<>();
            String InputBills = "";
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
                    checkBodyAllDataList = CheckLibraryXmlAnalysis.getSingleton().GetBodyInfo(BodyinfoStr);//分录列表
                    checkSubBodyDataList = CheckLibraryXmlAnalysis.getSingleton().GetSubBodyInfo(SubBodyinfoStr);//子分录列表
                    SubBodyStocksList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyStocks(SubBodyStocks);//仓位列表
                    SubBodyMaterialList = CheckLibraryXmlAnalysis.getSingleton().GetCheckBodyMaterial(SubBodyMaterial);//物料集合列表
                    if (SubBodyStocksList.size() > 0) {

                        stockBeanList = SubBodyStocksList;
                    } else {
                        CheckStockCellPort checkStockCellPort = new CheckStockCellPort() {
                            @Override
                            public void OnRes(List<CheckStockBean> checkStockBeanList) {
                                stockBeanList = checkStockBeanList;
                            }
                        };
                        CheckStockCellTask checkStockCellTask = new CheckStockCellTask(checkStockCellPort, webService, checkHeadDataList.get(0).getFStock());
                        checkStockCellTask.execute();
                    }
                    if (checkBodyAllDataList.size() > 0) {//筛选分录
                        checkBodyPartDataList = GetBodyByStockCellList(checkBodyAllDataList, stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
                    }
                    checkBodyAllDataList = DisposeTaskRvDataList(checkBodyAllDataList);
                    checkBodyPartDataList = DisposeTaskRvDataList(checkBodyPartDataList);
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
                myProgressDialog.dismiss();
                if (result.size() > 0) {
                    if (checkBodyPartDataList.size() > 0) {
                        InitScanRecycler();
                    }
                    InitCheckStockCell();
                    AsyncGetMaterialCell asyncGetMaterialCell = new AsyncGetMaterialCell();
                    asyncGetMaterialCell.execute();
                    TV_house.setText(result.get(0).getFStock_Name());
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    IsAllow = result.get(0).getFAllowOtherMaterial();

                }
            } catch (Exception e) {
                ViseLog.d("Check Bill Result Exception" + e.getMessage());
                tools.ShowDialog(MContect, "数据消失加载异常");
            }
            ViseLog.i("Check Bill Result = " + result);
        }

        @Override
        protected void onPreExecute() {
            myProgressDialog.ShowPD("加载数据中...");
        }
    }

    /*
    标签模板初始化
     */
    private void InitCheckTagMode(int pos) {
        CheckTagModePort checkTagModePort = new CheckTagModePort() {
            @Override
            public void OnTagRes(List<CheckMaterialModeBean> checkMaterialModeBeanList) {
                materialModeBeanList = checkMaterialModeBeanList;
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
                        CheckGetLabelTemplet(materialModeBeanList.get(i).getFGuid());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        };
        CheckGetTagModeTask checkGetTagModeTask = new CheckGetTagModeTask(checkTagModePort, webService, checkBodyPartDataList.get(pos).getFMaterial());
        checkGetTagModeTask.execute();
    }

    /*
    标签模板条码集合
     */
    private void CheckGetLabelTemplet(String LabelTempletBaecodeID) {
        CheckGetLabelTempletPort checkGetLabelTempletPort = new CheckGetLabelTempletPort() {
            @Override
            public void OnResult(List<ChildCheckTag> childTagList) {
                if (childTagList.size() > 0) {
                    checkBarCodeAnalyzeList = childTagList;
                    if (RefreshStatu == 1) {
                        RefreshStatu = 2;
                        InitSubBodyRecycler();
                    } else {
                        subbody_CheckRvAdapter = new Subbody_CheckRvAdapter(MContect, checkSubBodyList);
                        RV_SubBodyInfoTable.setAdapter(subbody_CheckRvAdapter);
                    }
                }
            }

            @Override
            public void OnError(String ErrotInfo) {
                if (!TextUtils.isEmpty(ErrotInfo)) {
                    scanTask_check_rvAdapter.setSelection(-1);
                    scanTask_check_rvAdapter.notifyDataSetChanged();//取消选中
                    tools.ShowDialog(MContect, ErrotInfo);
                    ViseLog.i("CheckGetLabelTemplet OnError");
                }
            }
        };
        CheckGetLabelTempletTask checkGetLabelTempletTask = new CheckGetLabelTempletTask(checkGetLabelTempletPort, webService, LabelTempletBaecodeID);
        checkGetLabelTempletTask.execute();
    }

    /*
    去除小数点
     */
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
                    NoSendQty = String.valueOf(ExecutedAuxQty - AuxQty);
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
     * 接收并处理pda扫描返回的数据
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
                        try {
                            //判断物料条码管控类型
                            if (!Info.substring(0, 2).equals("EX")) {
                                ViseLog.i("IFnfo = " + Info);//条码解析成功后返回Info数据
                                InputStream IsBarCodeInfoHead = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                InputStream IsBarCodeInfoBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                BarCodeInfoHeadList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                barcodeXmlBeanList = CheckLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                if (BarCodeInfoHeadList.get(1).getFBarcodeType().equals(ConnectStr.BARCODE_SEQUENCE.toLowerCase())) {//如果产品是序列号
                                    Is_CheckNumber_Mode = true;
                                    PutResultArray(barcodeXmlBeanList);
                                    FBarcodeLib = BarCodeInfoHeadList.get(2).getFGudi();
                                    ViseLog.i("FBarcodeLib = " + FBarcodeLib);
                                    IsSerialNumber = false;
                                    SubmitData("1.0");
                                    ViseLog.i("number = 1 序列号");
                                    AddSubBodyList();
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
                CheckBarCodeCheckTask barCodeCheckTask = new CheckBarCodeCheckTask(quitbarCodeCheckPort, webService, checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFMaterial(),
                        materialModeBeanList.get(Sp_LabelModeIndex).getFGuid(), data, "0");
                barCodeCheckTask.execute();
            }
        } else {
            tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或保存");
        }
    }

    /*
    批次号物料提交计算数量
     */
    public void SubmitData(String QuitSum) {
        try {
            if (Is_CheckNumber_Mode) {
                Sp_Material.setEnabled(false);
                Drawable BorderInputHouseSpace = getResources().getDrawable(R.drawable.border);
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
                            checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFCheckQty(checkSubBodyDataList.get(i).getFCheckQty());
                            float BodyNoPut = Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                            String BodyNoInput = String.valueOf(BodyNoPut);
                            checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFDiffQty(BodyNoInput);
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

    /*
    分录帐存数量为输入默认数量
     */
    public String NoQuitLibrary() {
        String Check = "";
        for (int i = 0; i < checkSubBodyDataList.size(); i++) {
            if (checkSubBodyDataList.get(i).getFBarcodeLib_Name().equals(checkBarCodeAnalyzeList.get(0).getValue())) {
                Check = checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFAccountQty();
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
     * 保存按钮事件，返回值传0
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
                            intent.putExtra("FGUID", tools.GetStringData(tools.InitSharedPreferences(MContect), "CheckLibraryActivityFGUID"));
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
        CheckBillCreateTask checkBillCreateTask = new CheckBillCreateTask(checkHeadDataList, checkBodyPartDataList, checkSubBodyList, webService, myProgressDialog, checkBillCreate, "0");
        checkBillCreateTask.execute();
    }

    /**
     * 结案按钮事件，返回值传1
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
                            Intent intent = new Intent(MContect, CheckLibraryActivity.class);
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
        CheckBillCreateTask checkBillCreateSaveTask = new CheckBillCreateTask(checkHeadDataList, checkBodyAllDataList, checkSubBodyDataList, webService, myProgressDialog, checkBillCreate, "1");
        checkBillCreateSaveTask.execute();
    }

    /*
    输入数量
     */
    private void ShowEditSumDialog(String Sum) {
        ShowResultListDialog.getSingleton().Show(MContect, checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowResultListDialog.getSingleton().Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFBaseUnit_Name(), checkBarCodeAnalyzeList);
    }

    /*
    清除数据
     */
    private void CleanData() {
        Is_CheckNumber_Mode = false;
        checkBarCodeAnalyzeList.clear();//清除条码解析列表
        scanTask_check_rvAdapter.setSelection(-1);
        scanTask_check_rvAdapter.notifyDataSetChanged();//分录数据刷新
        ViseLog.i("Click CleanData");
    }

    /**
     * 筛选分录行下面的子分录集合
     */
    private List<CheckSubBody> SumList(List<CheckSubBody> checkSubBodies, String StockCellID, String BodyDataID) {
        List<CheckSubBody> subBodies = new ArrayList<>();
        for (int i = 0; i < checkSubBodies.size(); i++) {
            if (BodyDataID.equals(checkSubBodies.get(i).getFBillBodyID()) && StockCellID.equals(checkSubBodies.get(i).getFStockCell())) {//找出相匹配的分录和子分录
                subBodies.add(checkSubBodies.get(i));//点击下标为pos的分录获得下标为i的子分录list
            }
        }
        return subBodies;//返回taskRvData
    }

    /**
     * 筛选仓位对应的分录集合
     */
    private List<CheckTaskRvData> GetBodyByStockCellList
    (List<CheckTaskRvData> taskRvData, String StockCellID) {
        List<CheckTaskRvData> checkTaskList = new ArrayList<>();
        for (int i = 0; i < taskRvData.size(); i++) {
            if (StockCellID.equals(taskRvData.get(i).getFStockCell())) {//找出相匹配的分录和子分录
                checkTaskList.add(taskRvData.get(i));//点击下标为pos的分录获得下标为i的子分录list
            }
        }
        return checkTaskList;//返回taskRvData
    }

    /*
    移动位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView,
                                      int n) {
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
            if (Tools.IsObjectNull(checkBodyPartDataList)) {
                if (checkBodyPartDataList.size() > 0) {
                    if (scanTask_check_rvAdapter.getselection() != position) {
                        LockResultPort lockResultPort = new LockResultPort() {
                            @Override
                            public void onStatusResult(String res) {
                                myProgressDialog.dismiss();
                                if (res.equals("Success")) {
                                    if (RV_ScanInfoTableIndex != position) {
                                        RV_ScanInfoTableIndex = position;
                                    }
                                    if (checkSubBodyDataList.size() > 0) {
                                        checkSubBodyList = SumList(checkSubBodyDataList, stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid(), checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFGuid());
                                        //添加筛选的子分录数据
                                    }
                                    InitCheckTagMode(position);//获取标签模板
//                                    if (GetSpinnerPos(SubBodyMaterialList, checkBodyPartDataList.get(position).getFMaterial()) >=0){
//                                        Sp_Material.setSelection(GetSpinnerPos(SubBodyMaterialList, checkBodyPartDataList.get(position).getFMaterial()));
//                                    }
                                    if (Tools.IsObjectNull(subbody_CheckRvAdapter)) {
                                        subbody_CheckRvAdapter.notifyDataSetChanged();
                                    }
                                    scanTask_check_rvAdapter.setSelection(position);
                                    scanTask_check_rvAdapter.notifyDataSetChanged();//刷新选中 
                                } else {
                                    tools.ShowDialog(MContect, res);
                                }
                            }
                        };
                        ViseLog.i("RV_ScanInfoTableIndex = " + RV_ScanInfoTableIndex);
                        CheckBodyLockTask quitBodyLockTask = new CheckBodyLockTask(lockResultPort, webService, checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFGuid(), myProgressDialog);
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
                }
            }
        } else {
            tools.ShowDialog(MContect, "检测到有扫描数据，请先清空或提交");
        }
    }

    private void AddTreeList() {
        int m = 0;
        TreeMBean treeMBean;
        treeMBean = (TreeMBean) getIntent().getSerializableExtra("M");
        if (Tools.IsObjectNull(treeMBean)) {
            ViseLog.i("treeMBean.getFName() = " + treeMBean.getFName());
            CheckTaskRvData checkTaskRvData = new CheckTaskRvData();
            CheckBodyMaterial checkBodyMaterial = new CheckBodyMaterial();
            List<CheckTaskRvData> checkTaskRvDataArrayList = new ArrayList<CheckTaskRvData>();
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
            checkTaskRvData.setFRowIndex(String.valueOf(checkBodyAllDataList.size() + 1.0));
            checkTaskRvData.setFStockCell(stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
            checkTaskRvData.setFStockCell_Name(stockBeanList.get(SpCheckHouseSpaceIndex).getFName());
            checkTaskRvDataArrayList.add(checkTaskRvData);
            if (IsAllow.equals("0")) {//如果不允许添加物料
                for (int j = 0; j < checkBodyPartDataList.size(); j++) {
                    if (checkBodyPartDataList.get(j).getFMaterial().equals(treeMBean.getFGuid())) {//若新增物料存在物料集合中，跳转位置，并选中
                        MoveToPosition(ScanTaskL, RV_BodyInfoTable, j);
                        RvBodyItemClick(j);
                        scanTask_check_rvAdapter.notifyDataSetChanged();
                        ConnectStr.ISMaterialExist = true;
                    } else {//若不存在，提示
                        ConnectStr.ISMaterialExist = false;
                    }
                }
            } else {//若允许，添加进去
                for (int pos = 0; pos < checkBodyPartDataList.size(); pos++) {
                    if (checkBodyPartDataList.get(pos).getFMaterial().equals(treeMBean.getFGuid())) {//若已存在添加的物料，跳转
                        m = pos;
                        MoveToPosition(ScanTaskL, RV_BodyInfoTable, pos);//移动到该物料
                        RvBodyItemClick(pos);//点击事件
                        checkBodyMaterial.setFName(treeMBean.getFName());
                        ConnectStr.ISMaterialExist = true;
                    }
                }
                if (!checkBodyPartDataList.get(m).getFMaterial().equals(treeMBean.getFGuid())) {//如果不存在此物料，添加
                    checkBodyPartDataList.add(checkTaskRvDataArrayList.get(0));//添加该物料
                    checkBodyMaterial.setFName(treeMBean.getFName());
                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, checkBodyPartDataList.size() - 1);//移动到该物料
                    RvBodyItemClick(checkBodyPartDataList.size() - 1);//点击事件
                    ConnectStr.ISMaterialExist = true;
                }
            }
        } else {
            ViseLog.i("TreeMBean == null");
        }
    }

    private void AddMaterialList(int i) {
        List<String> CheckList = new ArrayList<>();
        for (int pos = 0; pos < checkBodyPartDataList.size(); pos++) {
            CheckList.add(checkBodyPartDataList.get(pos).getFMaterial());
        }
        if (!CheckList.contains(SubBodyMaterialList.get(i).getFGuid())) {
            CheckTaskRvData checkTaskRvData = new CheckTaskRvData();
            String BodyGuid = new CreateGuid().toString();//随机生成分录Guid；
            checkTaskRvData.setFGuid(BodyGuid);
            checkTaskRvData.setFMaterial(SubBodyMaterialList.get(i).getFGuid());
            checkTaskRvData.setFMaterial_Code(SubBodyMaterialList.get(i).getFCode());
            checkTaskRvData.setFMaterial_Name(SubBodyMaterialList.get(i).getFName());
            checkTaskRvData.setFModel(SubBodyMaterialList.get(i).getFModel());
            checkTaskRvData.setFBaseUnit(SubBodyMaterialList.get(i).getFBaseUnit());
            checkTaskRvData.setFBaseUnit_Name(SubBodyMaterialList.get(i).getFBaseUnit_Name());
            checkTaskRvData.setFRowIndex(String.valueOf(checkBodyAllDataList.size() + 1.0));
            checkTaskRvData.setFStockCell(stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
            checkTaskRvData.setFStockCell_Name(stockBeanList.get(SpCheckHouseSpaceIndex).getFName());
            checkTaskRvData.setFAccountQty("0.0");
            checkTaskRvData.setFCheckQty("0.0");
            checkTaskRvData.setFDiffQty("0.0");
            checkBodyPartDataList.add(checkTaskRvData);
            if (Tools.IsObjectNull(scanTask_check_rvAdapter)){
                scanTask_check_rvAdapter.notifyDataSetChanged();
            }
        } else {
            for (int a = 0; a < checkBodyPartDataList.size(); a++) {
                if (checkBodyPartDataList.get(a).getFMaterial().equals(SubBodyMaterialList.get(i).getFGuid())) {
//                    if (!checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFMaterial().equals(SubBodyMaterialList.get(i).getFGuid())){
                    MoveToPosition(ScanTaskL, RV_BodyInfoTable, a);
                    RvBodyItemClick(a);
//                    }
                }
            }
        }
    }

    /*
    序列号扫描，新增子分录序列号
     */
    private void AddSubBodyList() {
        boolean ISExist = false;
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

                float subBodyCheck = Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFCheckQty());
                subBodyCheck += SubBodyCheck;
                checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFCheckQty(String.valueOf(subBodyCheck));//计算分录盘点数量
                float BodyDiff = Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
                String Diff = String.valueOf(BodyDiff);
                checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFDiffQty(Diff);//计算当前分录差异数量
                scanTask_check_rvAdapter.notifyDataSetChanged();
            }
        }
        if (!ISExist) {
            CheckSubBody checkSubBodyData = new CheckSubBody();
            String guid = new CreateGuid().toString();
            checkSubBodyData.setFGuid(guid);
            checkSubBodyData.setFBillBodyID(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFGuid());
            checkSubBodyData.setFStockCell(stockBeanList.get(SpCheckHouseSpaceIndex).getFGuid());
            checkSubBodyData.setFStockCell_Name(stockBeanList.get(SpCheckHouseSpaceIndex).getFName());
            checkSubBodyData.setFBarcodeLib(FBarcodeLib);
            checkSubBodyData.setFBarcodeType("33A50386-F167-4913-95C8-B7AE69B8CB55");
            checkSubBodyData.setFBarcodeLib_Name(barcodeXmlBeanList.get(3).getFBarcodeContent() + "|" + barcodeXmlBeanList.get(0).getFBarcodeContent());
            checkSubBodyData.setFRowIndex(String.valueOf(checkSubBodyDataList.size() + 1.0));
            checkSubBodyData.setFCheckQty("1.0");
            checkSubBodyData.setFAccountQty("0.0");
            checkSubBodyData.setFDiffQty("1.0");
            checkSubBodyData.setFCheckStockStatus("49E79140-94CA-4B43-988A-D3E2FE5BDEC6");
            checkSubBodyData.setFBarcodeType_Name("序列号");
            if (!checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFAccountQty().equals("0.0")) {
                checkSubBodyList.add(checkSubBodyData);
                checkSubBodyDataList.add(checkSubBodyData);
                subbody_CheckRvAdapter.notifyDataSetChanged();
            } else {
                checkSubBodyList = new ArrayList<CheckSubBody>();
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
            checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFCheckQty(String.valueOf(subBodyCheck));//计算分录盘点数量
            float BodyDiff = Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFCheckQty()) - (Tools.StringOfFloat(checkBodyPartDataList.get(RV_ScanInfoTableIndex).getFAccountQty()));
            String Diff = String.valueOf(BodyDiff);
            checkBodyPartDataList.get(RV_ScanInfoTableIndex).setFDiffQty(Diff);//计算当前分录差异数量
            scanTask_check_rvAdapter.notifyDataSetChanged();
        }
    }
}
