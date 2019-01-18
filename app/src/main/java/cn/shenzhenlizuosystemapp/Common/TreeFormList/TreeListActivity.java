package cn.shenzhenlizuosystemapp.Common.TreeFormList;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.GetMaterialTreeTask.GetTree;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree.TreeParent;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.adapter.ListViewAdapter;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.treelist.Node;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.treelist.OnTreeNodeClickListener;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.Xml.TreeFromData.TreeAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class TreeListActivity extends BaseActivity {

    private ListView LV_TreeFrom;
    private TextView TV_CastAbout;
    private TextView TV_Close;
    private TextView Back;
    private MyProgressDialog myProgressDialog;

    private List<TreeParent> TreeList = new ArrayList<>();
    private List<Node> dataList = new ArrayList<>();
    private ListViewAdapter mAdapter;
    private Node AtPresentNode;

    private WebService webService;
    private TreeListActivity MContect = null;
    private Tools tools = null;

    private String ID;

    @Override
    protected int inflateLayout() {
        return R.layout.tree_from_layout;
    }

    @Override
    public void initData() {
        tools = Tools.getTools();
        ID = getIntent().getStringExtra("GUID");
        MContect = new WeakReference<>(TreeListActivity.this).get();
        webService = WebService.getSingleton(TreeListActivity.this);
        myProgressDialog.ShowPD("加载中...");
        TreeOnRes treeOnRes = new TreeOnRes() {
            @Override
            public void OnRes(String Res) {
                try {
                    ViseLog.i("Tree" + Res);
                    if (!Res.substring(0, 2).equals("EX")) {
                        InputStream Is_Res = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                        TreeList = TreeAnalysis.getSingleton().GetTreeFrom(Is_Res);
                        if (TreeList.size() > 0) {
                            InitListData();
                            InitList();
                        } else {
                            tools.ShowOnClickDialog(MContect, "加载数据出错了", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ViewManager.getInstance().finishActivity(TreeListActivity.this);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }, true);
                        }
                    } else {
                        tools.ShowOnClickDialog(MContect, "加载数据出错了" + Res.substring(2, Res.length()), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewManager.getInstance().finishActivity(TreeListActivity.this);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }, true);
                    }
                    myProgressDialog.dismiss();

                } catch (UnsupportedEncodingException e) {
                    ViseLog.i("treeOnRes Exception = " + e);
                }
            }
        };
        GetTree getTree = new GetTree(treeOnRes, webService);
        getTree.execute();
        InitClick();
    }

    @Override
    public void initView() {
        LV_TreeFrom = $(R.id.LV_TreeFrom);
        TV_CastAbout = $(R.id.TV_CastAbout);
        TV_Close = $(R.id.TV_Close);
        Back = $(R.id.Back);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
    }

    private void InitClick() {
        TV_CastAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.IsObjectNull(AtPresentNode)) {
                    Intent intent = new Intent(TreeListActivity.this, SelectMGroupingActivity.class);
                    intent.putExtra("MID", AtPresentNode.getId());
                    intent.putExtra("ID",ID);
                    startActivity(intent);
                } else {
                    tools.ShowDialog(MContect, "至少选择一个物料分组在点下一步");
                }
            }
        });

        TV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(TreeListActivity.this);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.getInstance().finishActivity(TreeListActivity.this);
            }
        });
    }

    private void InitList() {
        mAdapter = new ListViewAdapter(LV_TreeFrom, this, dataList,
                0, R.drawable.zoomout_yzs, R.drawable.zoomin_yzs);
        LV_TreeFrom.setAdapter(mAdapter);
        mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                ViseLog.i("nodeName = " + node.getName() + "position = " + position);
                if (node.getselection() != position) {
                    AtPresentNode = node;
                    node.setSelection(position);
                    mAdapter.SetName(node.getName());
                    mAdapter.notifyDataSetChanged();
                }
                if (!node.isLeaf()) {
                    node.setChildren(SetChild(node.getChildren()));
                }
            }
        });
    }

    private void InitListData() {
        for (TreeParent t : TreeList) {
            dataList.add(new Node<>(t.getFGuid(), t.getFParent(), t.getFName()));
        }
    }

    private static List<Node> SetChild(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        setAllChild(result, nodes);
        return result;
    }

    private static void setAllChild(List<Node> res, List<Node> Nodes) {
        for (Node node : Nodes) {
            res.add(node);
            node.setSelection(-1);
            if (node.isLeaf()) {
                return;
            } else {
                setAllChild(res, node.getChildren());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewManager.getInstance().finishActivity(TreeListActivity.this);
    }
}
