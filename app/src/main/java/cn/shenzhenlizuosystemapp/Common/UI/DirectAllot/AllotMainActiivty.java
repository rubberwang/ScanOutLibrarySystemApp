package cn.shenzhenlizuosystemapp.Common.UI.DirectAllot;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_InputRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_DirectAllotRvAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.BarCodeCheckTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.AllotDetailTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.CreatAdjustStockBill;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.InputBodyLockTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.UnlockTask;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.Base.ZebarTools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeMessage;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CreateAdjustStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.Port.BarCodeCheckPort;
import cn.shenzhenlizuosystemapp.Common.Port.CreatAdjustStockBillPort;
import cn.shenzhenlizuosystemapp.Common.Port.EditSumPort;
import cn.shenzhenlizuosystemapp.Common.Port.GetLabelTempletBaecodesPort;
import cn.shenzhenlizuosystemapp.Common.Port.LockResultPort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.MaterialModeAdapter;
import cn.shenzhenlizuosystemapp.Common.View.EditSumDialog;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.BatchCodeXml;
import cn.shenzhenlizuosystemapp.Common.Xml.InputLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetLabelTempletBaecodesTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.GetTagModeTask;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.DirectAllotTask.StockCellTask;
import cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor.DirectAllotLibraryXmlAnalysis;
import cn.shenzhenlizuosystemapp.Common.Port.TagModePort;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.DirectAllotStockAdapter;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectStockCellPort;
import cn.shenzhenlizuosystemapp.Common.Port.AllotDetailPort;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotTaskRvData;
import cn.shenzhenlizuosystemapp.R;

public class AllotMainActiivty extends BaseActivity {

    private TextView TV_DeliverGoodsNumber;
    private TextView TV_BusType;
    private TextView TV_DirectAllotSumbit;
    private TextView TV_DirectAllotCancel;
    private TextView Tv_DirectAllotNotification_FOutStock;
    private TextView BarText;
    private Spinner Sp_DirectAllotNotification_FOutStockCell;
    private TextView Tv_DirectAllotNotification_FInStock;
    private Spinner Sp_DirectAllotNotification_FInStockCell;
    private Spinner Sp_DirectAllotLabel;
    private RecyclerView RV_ScanInfoTable;
    private RecyclerView RV_ResInfoTable;
    private MyProgressDialog myProgressDialog;
    private EditText ET_SuckUp;
    private TextView Back;

    private String BillID;
    private int scanTask_directAllotRvIndex = -1;
    private int Sp_DirectAllotLabelIndex = -1;
    private int Sp_DirectAllotNotification_FOutStockCellIndex = -1;
    private int Sp_DirectAllotNotification_FInStockCellIndex = -1;
    private String FBarcodeLib = "";
    private String ILSum = "";
    private int RefreshStatu = 1;
    private boolean Is_ExistScanValue = false;
    private boolean Is_Single = false;
    private String HeadID = "";
    private boolean IsSerialNumber = true;
    private boolean IsAddSerialNumber = false;
    private boolean IsSave = false;
    private String Type;
    private String Batch = "";

    private List<CreateAdjustStockBean> InputSubmitList = new ArrayList<CreateAdjustStockBean>();
    private List<String> CheckFGuid = new ArrayList<String>();
    private List<DirectAllotTaskRvData> directAllotTaskRvDataList;
    private List<MaterialModeBean> MaterialModeBeanList;
    private List<DirectAllotDetail> directAllotDetailList;
    private List<StockBean> OutStockBeanList;
    private List<StockBean> InStockBeanList;
    private List<ChildTag> ChildTagList;

    private Context MContect;
    private Tools tools = null;
    private WebService webService;
    private EditSumPort editSumPort;
    private ScanTask_DirectAllotRvAdapter scanTask_directAllotRvAdapter;
    private ScanResult_InputRvAdapter scanResult_Input_rvAdapter;
    private DirectAllotObServer directAllotObServer;

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
        directAllotObServer = new DirectAllotObServer();
        getLifecycle().addObserver(directAllotObServer);
        MContect = new WeakReference<>(AllotMainActiivty.this).get();
        EventBus.getDefault().register(MContect);
        tools = Tools.getTools();
        BillID = getIntent().getStringExtra("FGUID");
        Type = getIntent().getStringExtra("Type");
        tools.PutStringData("AllotMainActiivtyFGUID", BillID, tools.InitSharedPreferences(MContect));
        tools.PutStringData("Type", Type, tools.InitSharedPreferences(MContect));
        webService = WebService.getSingleton(MContect);
        tools = Tools.getTools();
        GetAllotDetail();
        InitClick();
        editSumPort = new EditSumPort() {
            @Override
            public void OnEnSure(String Sum) {
                if (Tools.StringOfFloat(Sum) <= Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFNotAllotQty()) && Tools.StringOfFloat(Sum) > 0) {
                    EditSumDialog.getSingleton(AllotMainActiivty.this).Dismiss();
                    SubmitData(Sum);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                } else {
                    EditSumDialog.getSingleton(AllotMainActiivty.this).ShowErrorInfo("输入数据有误");
                }
            }
        };
        ET_SuckUp.setFocusable(true);
        ET_SuckUp.setFocusableInTouchMode(true);
        ET_SuckUp.requestFocus();
        switch (Type) {
            case "0": {
                BarText.setText(R.string.DircetAllotAction);
                break;
            }
            case "-1": {
                Drawable FInStockCell_border = getResources().getDrawable(R.drawable.border);
                Sp_DirectAllotNotification_FInStockCell.setBackground(FInStockCell_border);
                Sp_DirectAllotNotification_FInStockCell.setEnabled(false);
                BarText.setText(R.string.OutAllotScan);
                break;
            }
            case "1": {
                Drawable FInStockCell_border = getResources().getDrawable(R.drawable.border);
                Sp_DirectAllotNotification_FOutStockCell.setBackground(FInStockCell_border);
                Sp_DirectAllotNotification_FOutStockCell.setEnabled(false);
                BarText.setText(R.string.InAllotScan);
                break;
            }
        }
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
        ET_SuckUp = $(R.id.ET_SuckUp);
        BarText = $(R.id.BarText);
        Back = $(R.id.Back);
    }

    class DirectAllotObServer implements LifecycleObserver {
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
            EditSumDialog.getSingleton(AllotMainActiivty.this).Dismiss();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            EventBus.getDefault().unregister(MContect);
        }
    }

    private void InitClick() {
        TV_DirectAllotSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsSave) {
                    tools.ShowOnClickDialog(MContect, "确定保存吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myProgressDialog.ShowPD("保存中");
                            CreateBillData();
                            tools.DisappearDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tools.DisappearDialog();
                        }
                    }, false);
                } else {
                    tools.ShowDialog(MContect, "没有提交数据，无法保存");
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.IsObjectNull(ChildTagList)) {
                    if (ChildTagList.size() > 0) {
                        tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                                ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                            }
                        }, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                            }
                        }, false);
                    } else {
                        ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                    }
                } else {
                    ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                }
            }
        });

        TV_DirectAllotCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.IsObjectNull(ChildTagList)) {
                    if (ChildTagList.size() > 0) {
                        tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                                ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                            }
                        }, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                            }
                        }, false);
                    } else {
                        ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                    }
                } else {
                    ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                }
            }
        });
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
                            public void OnBody(List<DirectAllotTaskRvData> directAllotBodyBeanList) {
                                directAllotTaskRvDataList = directAllotBodyBeanList;
                                InitTaskRv();
                                myProgressDialog.dismiss();
                            }
                        };
                        InputStream IS_DirectAllotBody = new ByteArrayInputStream(res.getBytes("UTF-8"));
                        DirectAllotLibraryXmlAnalysis.getSingleton().GetDirectAllotDetailBodyInfo(IS_DirectAllotBody, directPortDetailBodyXmlPort);
                        DirectPortDetailHeadXmlPort directPortDetailHeadXmlPort = new DirectPortDetailHeadXmlPort() {
                            @Override
                            public void OnHead(List<DirectAllotDetail> directAllotHeadBeanList) {
                                directAllotDetailList = directAllotHeadBeanList;
                                TV_DeliverGoodsNumber.setText(directAllotHeadBeanList.get(0).getFCode());
                                TV_BusType.setText(directAllotHeadBeanList.get(0).getFTransactionType_Name());
                                Tv_DirectAllotNotification_FOutStock.setText(directAllotHeadBeanList.get(0).getFOutStock_Name());
                                Tv_DirectAllotNotification_FInStock.setText(directAllotHeadBeanList.get(0).getFInStock_Name());
                                HeadID = directAllotHeadBeanList.get(0).getFGuid();
                                InitSotckCellSp();
                            }
                        };
                        InputStream IS_DirectAllotHead = new ByteArrayInputStream(res.getBytes("UTF-8"));
                        DirectAllotLibraryXmlAnalysis.getSingleton().GetDirectAllotDetailHeadInfo(IS_DirectAllotHead, directPortDetailHeadXmlPort);
                        IS_DirectAllotHead.close();
                        IS_DirectAllotBody.close();
                    } catch (Exception e) {

                    }
                }
            }
        };
        AllotDetailTask allotDetailTask = new AllotDetailTask(allotDetailPort, webService, BillID, Type);
        allotDetailTask.execute();
    }

    private void InitTaskRv() {
        directAllotTaskRvDataList = DisposeTaskTableList(directAllotTaskRvDataList);
        LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_ScanInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_ScanInfoTable.setLayoutManager(ScanTaskL);
        scanTask_directAllotRvAdapter = new ScanTask_DirectAllotRvAdapter(this, directAllotTaskRvDataList);
        RV_ScanInfoTable.setAdapter(scanTask_directAllotRvAdapter);
        scanTask_directAllotRvAdapter.setOnItemClickLitener(new ScanTask_DirectAllotRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (Tools.StringOfFloat(directAllotTaskRvDataList.get(position).getFAuxQty()) <= Tools.StringOfFloat(directAllotTaskRvDataList.get(position).getFThisAuxQty()) +
                        Tools.StringOfFloat(directAllotTaskRvDataList.get(position).getFExecutedAuxQty())) {
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
                            InputBodyLockTask inputBodyLockTask = new InputBodyLockTask(lockResultPort, webService, directAllotTaskRvDataList.get(position).getFGuid(), myProgressDialog);
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
                StockCellTask stockCellTask = new StockCellTask(directStockCellPort, webService, directAllotDetailList.get(0).getFOutStock(), i);
                stockCellTask.execute();
            } else {
                StockCellTask stockCellTask = new StockCellTask(directStockCellPort, webService, directAllotDetailList.get(0).getFInStock(), i);
                stockCellTask.execute();
            }
        }
    }

    private List<DirectAllotTaskRvData> DisposeTaskTableList(List<DirectAllotTaskRvData> directAllotTaskRvDataList) {
        List<DirectAllotTaskRvData> removeList = new ArrayList<DirectAllotTaskRvData>();
        for (int index = 0; index < directAllotTaskRvDataList.size(); index++) {
            String AddNoSendQty = "0";
            float AddAuxQty = 0;
            float AddExecutedAuxQty = 0;
            if (ConnectStr.ISSHOWNONEXECUTION) {
                String NoSendQty = "0";
                if (!TextUtils.isEmpty(directAllotTaskRvDataList.get(index).getFAuxQty()) && !TextUtils.isEmpty(directAllotTaskRvDataList.get(index).getFExecutedAuxQty())) {
                    float AuxQty = Tools.StringOfFloat(directAllotTaskRvDataList.get(index).getFAuxQty());
                    float ExecutedAuxQty = Tools.StringOfFloat(directAllotTaskRvDataList.get(index).getFExecutedAuxQty());
                    NoSendQty = String.valueOf(AuxQty - ExecutedAuxQty);
                }
                if (Tools.StringOfFloat(NoSendQty) <= 0) {
                    removeList.add(directAllotTaskRvDataList.get(index));
                    continue;
                }
                if (!TextUtils.isEmpty(directAllotTaskRvDataList.get(index).getFIsClosed())) {
                    if (directAllotTaskRvDataList.get(index).getFIsClosed().equals("1")) {
                        removeList.add(directAllotTaskRvDataList.get(index));
                        continue;
                    }
                }
            }
            if (!TextUtils.isEmpty(directAllotTaskRvDataList.get(index).getFAuxQty()) &&
                    !TextUtils.isEmpty(directAllotTaskRvDataList.get(index).getFExecutedAuxQty())) {
                AddAuxQty = Tools.StringOfFloat(directAllotTaskRvDataList.get(index).getFAuxQty());
                AddExecutedAuxQty = Tools.StringOfFloat(directAllotTaskRvDataList.get(index).getFExecutedAuxQty());
                AddNoSendQty = String.valueOf(AddAuxQty - AddExecutedAuxQty);
            }
            directAllotTaskRvDataList.get(index).setFNotAllotQty(AddNoSendQty);
            directAllotTaskRvDataList.get(index).setFAuxQty(String.valueOf(AddAuxQty));
            directAllotTaskRvDataList.get(index).setFExecutedAuxQty(String.valueOf(AddExecutedAuxQty));
            directAllotTaskRvDataList.get(index).setFThisAuxQty(String.valueOf(Tools.StringOfFloat(directAllotTaskRvDataList.get(index).getFThisAuxQty())));
        }
        if (removeList.size() > 0) {
            directAllotTaskRvDataList.removeAll(removeList);
        }
        return directAllotTaskRvDataList;
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
        GetTagModeTask getTagModeTask = new GetTagModeTask(tagModePort, webService, directAllotTaskRvDataList.get(pos).getFMaterial());
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
                                InputStream BatchBody = new ByteArrayInputStream(Info.getBytes("UTF-8"));
                                List<BarCodeHeadBean> BarCodeInfoHeadList = InputLibraryXmlAnalysis.getSingleton().GetBarCodeHead(IsBarCodeInfoHead);
                                List<BarcodeXmlBean> barcodeXmlBeanList = InputLibraryXmlAnalysis.getSingleton().GetBarCodeBody(IsBarCodeInfoBody);
                                Batch = BatchCodeXml.getSingleton().GETBatchCode(BatchBody);
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
                                            / Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFUnitRate());
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
                                    ShowEditSumDialog(NoInputLibrary());
                                    ViseLog.i("通用号 FBarcodeLib =" + FBarcodeLib);
                                }
                                Sp_DirectAllotNotification_FInStockCell.setEnabled(false);
                                Sp_DirectAllotNotification_FOutStockCell.setEnabled(false);
                                IsBarCodeInfoHead.close();
                                IsBarCodeInfoBody.close();
                            } else {
                                tools.ShowDialog(MContect, Info.substring(2, Info.length()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ViseLog.i("BarCodeCheckPort Exception = " + e);
                        }
                    }
                };
                String IS = "";
                if (Type.equals("0")) {
                    IS = "-1";
                } else if (Type.equals("-1")) {
                    IS = "-1";
                } else if (Type.equals("1")) {
                    IS = "1";
                }
                BarCodeCheckTask barCodeCheckTask = new BarCodeCheckTask(barCodeCheckPort, webService, directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFMaterial(),
                        MaterialModeBeanList.get(Sp_DirectAllotLabelIndex).getFGuid(), data, IS);
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

    public void SubmitData(String InputSum) {
        try {
            if (Is_ExistScanValue) {
                if (!TextUtils.isEmpty(InputSum) && Tools.StringOfFloat(InputSum) > 0) {
                    if (CheckGuid(CheckFGuid, FBarcodeLib)) {
                        IsSerialNumber = true;
                        if (Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFNotAllotQty()) >= Tools.StringOfFloat(InputSum)) {
                            if (IsAddSerialNumber) {
                                CheckFGuid.add(FBarcodeLib);
                                IsAddSerialNumber = false;
                            }

                            Drawable FInStockCell_border = getResources().getDrawable(R.drawable.border);
                            Sp_DirectAllotNotification_FInStockCell.setBackground(FInStockCell_border);
                            Drawable FOutStockCell_border = getResources().getDrawable(R.drawable.border);
                            Sp_DirectAllotNotification_FOutStockCell.setBackground(FOutStockCell_border);

                            IsSave = true;
                            Drawable drawable_purple = getResources().getDrawable(R.drawable.circularbead_purple);
                            TV_DirectAllotSumbit.setBackground(drawable_purple);
                            TV_DirectAllotSumbit.setTextColor(getResources().getColor(R.color.White));

                            CreateAdjustStockBean createAdjustStockBean = new CreateAdjustStockBean();
                            createAdjustStockBean.setFBillBodyID(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFGuid());
                            createAdjustStockBean.setFBarcodeLib(FBarcodeLib);
                            createAdjustStockBean.setFAuxQty(InputSum);
                            InputSubmitList.add(createAdjustStockBean);

                            float Sum = Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFThisAuxQty()) +
                                    Tools.StringOfFloat(InputSum);
                            String SetFThisAuxQty = String.valueOf(Sum);
                            directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).setFThisAuxQty(SetFThisAuxQty);

                            //为最后生成入库单保存数据array
                            float NewNoPut = Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFAuxQty()) - (Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFExecutedAuxQty()) +
                                    Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFThisAuxQty()));
                            String SetNoInput = String.valueOf(NewNoPut);
                            directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).setFNotAllotQty(SetNoInput);

                            if (Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFNotAllotQty()) <= 0) {
                                scanTask_directAllotRvAdapter.setSelection(-1);
                            }
                            scanTask_directAllotRvAdapter.notifyDataSetChanged();
                            Is_ExistScanValue = false;
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
        if (!TextUtils.isEmpty(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFAuxQty()) && !TextUtils.isEmpty(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFExecutedAuxQty())) {
            float noSend = Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFAuxQty()) - (Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFExecutedAuxQty()) +
                    Tools.StringOfFloat(directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFThisAuxQty()));
            String NoSendSum = String.valueOf(noSend);
            ViseLog.i("NoInputLibrary = " + NoSendSum);
            return NoSendSum;
        }
        return "";
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

    private void CreateBillData() {
        CreatAdjustStockBillPort creatAdjustStockBillPort = new CreatAdjustStockBillPort() {
            @Override
            public void OnResult(String res) {
                String ResInfo = res.substring(0, 2);
                if (ResInfo.equals("EX")) {
                    tools.ShowDialog(MContect, res.substring(2, res.length()));
                } else {
                    tools.ShowOnClickDialog(MContect, res, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myProgressDialog.dismiss();
                            tools.DisappearDialog();
                            finish();
                            Intent intent = new Intent(AllotMainActiivty.this, AllotMainActiivty.class);
                            intent.putExtra("FGUID", tools.GetStringData(tools.InitSharedPreferences(AllotMainActiivty.this), "AllotMainActiivtyFGUID"));
                            intent.putExtra("Type", tools.GetStringData(tools.InitSharedPreferences(AllotMainActiivty.this), "Type"));
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
        CreatAdjustStockBill inputBillCreateTask = new CreatAdjustStockBill(creatAdjustStockBillPort, webService, HeadID, directAllotDetailList.get(0).getFTransactionType(),
                directAllotDetailList.get(0).getFInStock(), directAllotDetailList.get(0).getFOutStock(), InStockBeanList.get(Sp_DirectAllotNotification_FInStockCellIndex).getFGuid(),
                OutStockBeanList.get(Sp_DirectAllotNotification_FOutStockCellIndex).getFGuid(), InputSubmitList);
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
        EditSumDialog.getSingleton(AllotMainActiivty.this).Show(MContect, directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFMaterial_Code(), editSumPort, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSumDialog.getSingleton(AllotMainActiivty.this).Dismiss();
                CleanData();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
        }, Sum, directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFUnit_Name(), Batch, directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFMaterial_Name() +
                directAllotTaskRvDataList.get(scanTask_directAllotRvIndex).getFModel());
    }

    private void CleanData() {
        Is_ExistScanValue = false;
        ChildTagList.clear();
        scanResult_Input_rvAdapter.notifyDataSetChanged();
        scanTask_directAllotRvAdapter.setSelection(-1);
        scanTask_directAllotRvAdapter.notifyDataSetChanged();
        ViseLog.i("Click CleanData");
    }

    @Override
    public void onBackPressed() {
        if (Tools.IsObjectNull(ChildTagList)) {
            if (ChildTagList.size() > 0) {
                tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                        ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                    }
                }, false);
            } else {
                ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
            }
        } else {
            ViewManager.getInstance().finishActivity(AllotMainActiivty.this);
        }
    }
}
