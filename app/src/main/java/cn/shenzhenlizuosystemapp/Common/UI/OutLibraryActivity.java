package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.symbol.emdk.EMDKManager;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_RvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_RvAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TaskRvData;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.ItemData;
//import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.LoginAdapter;
import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.InputAdapter;
import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.LoginAdapter;
import cn.shenzhenlizuosystemapp.Common.Port.ZebarScanResult;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.InputTaskXml;
import cn.shenzhenlizuosystemapp.Common.ZebarScan.ContinuousScan;
import cn.shenzhenlizuosystemapp.R;

public class OutLibraryActivity extends BaseActivity {

    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private TextView TV_Time;
    private Spinner Sp_house;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private TextView TV_Scaning;
    private String FGUID = "";
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;

    private OutLibraryObServer outLibraryObServer;
    private List<ScanResultData> scanResultData;
    private WebService webService;
    private ScanResult_RvAdapter scanResultRvAdapter;
    private ScanTask_RvAdapter scanTask_rvAdapter;
    private List<ItemData> SpStrList;
    private List<QuitLibraryDetail> quitLibraryDetails;
    private List<TaskRvData> taskRvDataList;
    private Tools tools;

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_quit_layout;
    }

    @Override
    public void initData() {
        tools = Tools.getTools();
        scanResultData = new ArrayList<>();
        taskRvDataList = new ArrayList<>();
        outLibraryObServer = new OutLibraryObServer();
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        getLifecycle().addObserver(outLibraryObServer);
        SpStrList = new ArrayList<>();
        webService = WebService.getSingleton();
        EventBus.getDefault().register(this);
        InitClick();
        GetOutLibraryBills();
        InitRecycler();
        InitScanRecycler();
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
        TV_Scaning = $(R.id.TV_Scaning);
    }

    public void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(OutLibraryActivity.this);
            }

        });
        //扫描
//        TV_Scaning.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContinuousScan.getCancelRead(OutLibraryActivity.this).HowState()) {
//                    ContinuousScan.getCancelRead(OutLibraryActivity.this).StartScan().SetResultPort(new ZebarScanResult() {
//                        @Override
//                        public void OnBad(String e) {
//                            ViseLog.i("扫描头错误" + e);
//                        }
//
//                        @Override
//                        public void OnSuccess(String Data) {
//                            ScanResultData scanResult = new ScanResultData();
//                            scanResult.setScanData(Data);
//                            scanResultData.add(scanResult);
//                            scanResultRvAdapter.notifyDataSetChanged();
//                        }
//                    });
//                } else {
//                    tools.ShowDialog(OutLibraryActivity.this, "扫描头适配失败");
//                }
//            }
//        });
    }
    private void InitRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_GetInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_GetInfoTable.setLayoutManager(layoutManager);
        scanResultRvAdapter = new ScanResult_RvAdapter(this, scanResultData);
        RV_GetInfoTable.setAdapter(scanResultRvAdapter);
        scanResultRvAdapter.setOnItemClickLitener(new ScanResult_RvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitScanRecycler() {
        //扫描任务列表适配
        LinearLayoutManager ScanTaskL = new LinearLayoutManager(this);
        ScanTaskL.setOrientation(ScanTaskL.VERTICAL);
        RV_ScanInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_ScanInfoTable.setLayoutManager(ScanTaskL);
        scanTask_rvAdapter = new ScanTask_RvAdapter(this, taskRvDataList);
        RV_ScanInfoTable.setAdapter(scanTask_rvAdapter);
        scanTask_rvAdapter.setOnItemClickLitener(new ScanTask_RvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitSp(List<QuitLibraryDetail> quitLibraryDetails) {
        if (quitLibraryDetails.size() >= 0) {
            for (QuitLibraryDetail quitLibraryDetail : quitLibraryDetails) {
                ItemData itemData = new ItemData();
                itemData.setData(quitLibraryDetail.getFStock_Name());
                SpStrList.add(itemData);
            }
        }
        InputAdapter InputAdapter = new InputAdapter(SpStrList, OutLibraryActivity.this);
        Sp_house.setAdapter(InputAdapter);
    }

    class OutLibraryObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {
            EventBus.getDefault().unregister(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
        }
    }

    private void GetOutLibraryBills() {
        OutLibraryActivity.GetInputLibraryBillsAsyncTask getOutLibraryBillsAsyncTask = new OutLibraryActivity.GetInputLibraryBillsAsyncTask();
        getOutLibraryBillsAsyncTask.execute();
    }

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<QuitLibraryDetail>> {

        private RecyclerView recyclerView;

        @Override
        protected List<QuitLibraryDetail> doInBackground(Integer... params) {
            String OutBills = "";
            List<QuitLibraryDetail> outLibraryBills = new ArrayList<>();
            InputStream in_Heard = null;
            InputStream in_Body = null;
            try {
                OutBills = webService.GetWareHouseData(ConnectStr.ConnectionToString, FGUID);
                ViseLog.i("OutBills = " + OutBills);
                in_Heard = new ByteArrayInputStream(OutBills.getBytes("UTF-8"));
                outLibraryBills = GetInputArray(in_Heard);
                in_Body = new ByteArrayInputStream(OutBills.getBytes("UTF-8"));
                taskRvDataList = InputTaskXml.getSingleton().GetInputBodyXml(in_Body);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return outLibraryBills;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(final List<QuitLibraryDetail> result) {
            try {
                if (result.size() >= 0) {
                    if (taskRvDataList.size() >= 0) {
                        taskRvDataList.remove(0);
                        InitScanRecycler();
                    }
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    //TV_Time.setText(result.get(0).getFDate());
                    //TV_house.setDropDownHorizontalOffset(result.get(0).getFStock_Name());
                    TV_BusType.setText(result.get(0).getFTransactionType_Name());
                    TV_Unit.setText(result.get(0).getFPartner_Name());
                    InitSp(result);
                    ViseLog.i("quitLibraryDetails 赋值");

                }
            } catch (Exception e) {
                ViseLog.d("Select适配RV数据错误" + e);
            }
            ViseLog.i("出库单返回数据" + result);
        }

        @Override
        protected void onPreExecute() {
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(EventBusScanDataMsg event) {
        ViseLog.i("EventBus = " + event.ScanDataMsg);

    }

    public List GetInputArray(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        OutLibraryActivity.BodySAXHandler handler = new OutLibraryActivity.BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<QuitLibraryDetail> outbodys = handler.getBody();
        return outbodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<QuitLibraryDetail> OutBodys;
        private QuitLibraryDetail outbody;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<QuitLibraryDetail> getBody() {
            if (OutBodys != null) {
                return OutBodys;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            OutBodys = new ArrayList<QuitLibraryDetail>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table")) {
                outbody = new QuitLibraryDetail();
                ViseLog.i("创建outbody");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table")) {
                OutBodys.add(outbody);
                outbody = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("HeadGuid")) {
                    outbody.setFGuid(data);
                    ViseLog.i(data);
                } else if (tag.equals("FCode")) {
                    outbody.setFCode(data);
                } else if (tag.equals("FStock")) {
                    outbody.setFStock(data);
                } else if (tag.equals("FStock_Name")) {
                    outbody.setFStock_Name(data);
                } else if (tag.equals("FTransactionType")) {
                    outbody.setFTransactionType(data);
                } else if (tag.equals("FTransactionType_Name")) {
                    outbody.setFTransactionType_Name(data);
                } else if (tag.equals("FPartner")) {
                    outbody.setFPartner(data);
                } else if (tag.equals("FPartner_Name")) {
                    outbody.setFPartner_Name(data);
                }
            }

        }
    }
}