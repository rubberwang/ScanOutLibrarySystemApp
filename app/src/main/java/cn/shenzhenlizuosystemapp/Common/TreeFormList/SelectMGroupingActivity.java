package cn.shenzhenlizuosystemapp.Common.TreeFormList;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectM_ItemAdapter;
import cn.shenzhenlizuosystemapp.Common.AsyncGetData.GetMaterialTreeTask.GetM;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TreeMBean;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.CheckLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.TreeFromData.MAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class SelectMGroupingActivity extends BaseActivity {

    private WebService webService;
    private Tools tools;
    private SelectMGroupingActivity MContext;
    private SelectM_ItemAdapter selectM_itemAdapter;

    private String MID = "";
    private String FID = "";
    private List<TreeMBean> treeMBeanList;

    private TextView Back;
    private TextView TV_Close;
    private TextView TV_CastAbout;
    private RecyclerView RV_Seleect_M_Grouping;
    private MyProgressDialog myProgressDialog;

    @Override
    protected int inflateLayout() {
        return R.layout.select_m_grouping_layout;
    }

    @Override
    public void initData() {
        MID = getIntent().getStringExtra("MID");
        FID = getIntent().getStringExtra("ID");
        tools = Tools.getTools();
        MContext = new WeakReference<>(SelectMGroupingActivity.this).get();
        myProgressDialog = new MyProgressDialog(MContext, R.style.CustomDialog);
        webService = WebService.getSingleton(SelectMGroupingActivity.this);
        myProgressDialog.ShowPD("加载中...");
        GetM();
        InitClick();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        TV_Close = $(R.id.TV_Close);
        TV_CastAbout = $(R.id.TV_CastAbout);
        RV_Seleect_M_Grouping = $(R.id.RV_Seleect_M_Grouping);
    }

    private void InitClick() {
        TV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
            }
        });
        
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
            }
        });

        TV_CastAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( Tools.IsObjectNull(treeMBeanList.get(selectM_itemAdapter.getselection()))&&selectM_itemAdapter.getselection() >= 0){
                    ViewManager.getInstance().finishActivity(CheckLibraryActivity.class);
                    Intent intent = new Intent(SelectMGroupingActivity.this, CheckLibraryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("M", treeMBeanList.get(selectM_itemAdapter.getselection()));
                    intent.putExtra("FGUID",FID);
                    ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
                    ViewManager.getInstance().finishActivity(TreeListActivity.class);
                    startActivity(intent);

                }else {
                    tools.ShowDialog(MContext,"必须选择物料在点下一步");
                }
            }
        });
    }

    private void GetM() {
        if (!TextUtils.isEmpty(MID)) {
            M_OnRes m = new M_OnRes() {
                @Override
                public void OnRes(String Result) {
                    if (Result.substring(0, 2).equals("EX")) {
                        myProgressDialog.dismiss();
                        tools.ShowOnClickDialog(MContext, "加载数据出错了" + Result.substring(2, Result.length()), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }, true);
                    } else {
                        try {
                            InputStream In_M = new ByteArrayInputStream(Result.getBytes("UTF-8"));
                            treeMBeanList = MAnalysis.getSingleton().GetMXml(In_M);
                            InitRV();
                            myProgressDialog.dismiss();
                        } catch (Exception e) {
                            ViseLog.i("GetM XmlAnalysis Exception= ");
                        }
                    }
                }
            };
            GetM getM = new GetM(m, webService, MID);
            getM.execute();
        } else {
            myProgressDialog.dismiss();
            tools.ShowOnClickDialog(MContext, "加载数据出错了", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }, true);
        }
    }

    private void InitRV() {
        LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_Seleect_M_Grouping.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_Seleect_M_Grouping.setLayoutManager(ScanTaskL);
        selectM_itemAdapter = new SelectM_ItemAdapter(this, treeMBeanList);
        RV_Seleect_M_Grouping.setAdapter(selectM_itemAdapter);
        selectM_itemAdapter.setOnItemClickLitener(new SelectM_ItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectM_itemAdapter.getselection() != position) {
                    selectM_itemAdapter.setSelection(position);
                    selectM_itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ViewManager.getInstance().finishActivity(SelectMGroupingActivity.this);
    }
}
