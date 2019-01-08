package cn.shenzhenlizuosystemapp.Common.TreeFormList;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vise.log.ViseLog;
import com.vise.log.inner.Tree;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.AsyncGetData.GetMaterialTreeTask.GetTree;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TreeParent;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TreeSon;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.adapter.ListViewAdapter;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.treelist.Node;
import cn.shenzhenlizuosystemapp.Common.Xml.TreeFromData.TreeAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class TreeListActivity extends BaseActivity {

    private ListView LV_TreeFrom;

    private List<TreeParent> TreeList;
    private List<Node> dataList = new ArrayList<>();
    private ListViewAdapter mAdapter;

    private WebService webService;

    @Override
    protected int inflateLayout() {
        return R.layout.tree_from_layout;
    }

    @Override
    public void initData() {
        webService = WebService.getSingleton(TreeListActivity.this);
        TreeOnRes treeOnRes = new TreeOnRes() {
            @Override
            public void OnRes(String Res) {
                try {
                    ViseLog.i("Tree" + Res);
                    InputStream Is_Res = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                    TreeList = TreeAnalysis.getSingleton().GetTreeFrom(Is_Res);
                    InitListData();
                    InitList();
                } catch (UnsupportedEncodingException e) {
                    ViseLog.i("treeOnRes Exception = " + e);
                }
            }
        };
        GetTree getTree = new GetTree(treeOnRes, webService);
        getTree.execute();
    }

    @Override
    public void initView() {
        LV_TreeFrom = $(R.id.LV_TreeFrom);
    }
    
    private void InitList(){
        mAdapter = new ListViewAdapter(LV_TreeFrom, this, dataList,
                0, R.drawable.zoomout_yzs, R.drawable.zoomin_yzs);
        LV_TreeFrom.setAdapter(mAdapter);
    }

    private void InitListData() {
        for (TreeParent t : TreeList) {
            dataList.add(new Node<>(t.getFGuid(), t.getFParent(), t.getFName()));
        }
    }
}
