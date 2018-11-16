package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
import com.symbol.emdk.barcode.StatusData.ScannerStates;
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
import java.util.Iterator;
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

public class QuitLibraryActivity extends BaseActivity implements EMDKListener, DataListener, StatusListener, ScannerConnectionListener{

    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private TextView TV_Time;
    private Spinner Sp_house;
    private Spinner spinnerScannerDevices;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private TextView TV_Scaning;
    private String FGUID = "";
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;

    private WebService webService;
    private OutLibraryObServer outLibraryObServer;
    private ScanResult_RvAdapter scanResult_rvAdapter;
    private ScanTask_RvAdapter scanTask_rvAdapter;
    private List<ScanResultData> scanResultData;
    private List<ItemData> SpStrList;
    private List<QuitLibraryDetail> quitLibraryDetails;
    private List<TaskRvData> taskRvDataList;
    private List<ScannerInfo> deviceList = null;
    private List<String> ScanResStrList = null;
    private Tools tools;
    /**
     * 扫描
     */
    private int scannerIndex = 0;
    private int defaultIndex = 0;
    private Scanner scanner = null;
    private BarcodeManager barcodeManager = null;
    private EMDKManager emdkManager = null;
    private ProfileManager profileManager = null;
    private EMDKManager emdkManager2 = null;
    private boolean IsStartRead = false;

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_quit_layout;
    }
    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        ScanResStrList = new ArrayList<String>();
        deviceList = new ArrayList<ScannerInfo>();
        SpStrList = new ArrayList<>();
        scanResultData = new ArrayList<>();
        taskRvDataList = new ArrayList<>();
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            ViseLog.i("Scan: " + "调用失败!");//调用失败
            return;
        }
        tools = Tools.getTools();
        outLibraryObServer = new OutLibraryObServer();
        getLifecycle().addObserver(outLibraryObServer);
        webService = WebService.getSingleton();
        InitClick();
        GetOutLibraryBills();
        InitRecycler();
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
        spinnerScannerDevices = $(R.id.spinnerScannerDevices);
    }

    public void InitClick() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(QuitLibraryActivity.this);
            }

        });
        //扫描
        TV_Scaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsStartRead = true;
                startScan();
            }
        });

        spinnerScannerDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {

                if ((scannerIndex != position) || (scanner == null)) {
                    scannerIndex = position;
                    deInitScanner();
                    initScanner();
                    setTrigger();
                    setDecoders();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();
            } catch (Exception e) {
                ViseLog.i("Status:错误 " + e.getMessage());
            }
            try {
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
            } catch (Exception e) {
                ViseLog.i("Status:错误 " + e.getMessage());
            }
            try {
                scanner.release();
            } catch (Exception e) {
                ViseLog.i("Status:错误 " + e.getMessage());
            }
            scanner = null;
        }
    }

    private void initScanner() {
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
                Log.i("huangmin", "deviceListdata" + deviceList.get(scannerIndex).toString());
            } else {
                ViseLog.i("Status: " + "未能获得指定的扫描仪设备!请关闭并重新启动应用程序。");
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    ViseLog.i("Status: " + e.getMessage());
                }
            } else {
                ViseLog.i("Status: " + "未能初始化扫描设备.");
            }
        }
    }

    private void setTrigger() {
        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            scanner.triggerType = TriggerType.SOFT_ALWAYS;//软解
        }
    }

    private void setDecoders() {
        if (scanner == null) {
            initScanner();
        }
        if ((scanner != null) && (scanner.isEnabled())) {
            try {
                ScannerConfig config = scanner.getConfig();
                // Set EAN8
                config.decoderParams.ean8.enabled = true;
                // Set EAN13
                config.decoderParams.ean13.enabled = true;
                // Set Code39
                config.decoderParams.code39.enabled = true;
                //Set Code128
                config.decoderParams.code128.enabled = true;
                scanner.setConfig(config);
            } catch (ScannerException e) {
                ViseLog.i("Status: " + e.getMessage());
            }
        }
    }

    private void enumerateScannerDevices() {
        if (barcodeManager != null) {

            List<String> friendlyNameList = new ArrayList<String>();
            int spinnerIndex = 0;

            deviceList = barcodeManager.getSupportedDevicesInfo();

            if ((deviceList != null) && (deviceList.size() != 0)) {

                Iterator<ScannerInfo> it = deviceList.iterator();
                while (it.hasNext()) {
                    ScannerInfo scnInfo = it.next();
                    friendlyNameList.add(scnInfo.getFriendlyName());
                    if (scnInfo.isDefaultScanner()) {
                        defaultIndex = spinnerIndex;
                    }
                    ++spinnerIndex;
                }
            } else {
                ViseLog.i("Status: " + "未能获得支持的扫描器设备列表!请重启设备.");
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(QuitLibraryActivity.this, android.R.layout.simple_spinner_item, friendlyNameList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerScannerDevices.setAdapter(spinnerAdapter);
            spinnerScannerDevices.setSelection(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        deInitScanner();
        if (emdkManager != null) {
            ViseLog.i("EMDK不等于空");
            barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
            // Add connection listener 添加连接监听器
            if (barcodeManager != null) {
                barcodeManager.addConnectionListener(this);
            }
            enumerateScannerDevices();//适配设备支持模式
            spinnerScannerDevices.setSelection(1);
            initScanner();
            setTrigger();
            setDecoders();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deInitScanner();
        if (barcodeManager != null) {
            barcodeManager.removeConnectionListener(this);
            barcodeManager = null;
            deviceList = null;
        }
        // Release the barcode manager resources
        if (emdkManager != null) {
            emdkManager.release(FEATURE_TYPE.BARCODE);
        }
        Log.i("MainActivity", "    onPause()");
    }
    private void InitRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_GetInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_GetInfoTable.setLayoutManager(layoutManager);
        scanResult_rvAdapter = new ScanResult_RvAdapter(this, scanResultData);
        RV_GetInfoTable.setAdapter(scanResult_rvAdapter);
        scanResult_rvAdapter.setOnItemClickLitener(new ScanResult_RvAdapter.OnItemClickLitener() {
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
        InputAdapter InputAdapter = new InputAdapter(SpStrList, QuitLibraryActivity.this);
        Sp_house.setAdapter(InputAdapter);
    }

    private void GetOutLibraryBills() {
        GetInputLibraryBillsAsyncTask getOutLibraryBillsAsyncTask = new GetInputLibraryBillsAsyncTask();
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
                OutBills = webService.QuitWareHouseData(ConnectStr.ConnectionToString, FGUID);
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
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
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

    @Override
    public void onOpened(EMDKManager emdkManager) {
        ViseLog.i("Status: " + "EMDK打开成功!");

        this.emdkManager = emdkManager;
        barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
        if (barcodeManager != null) {
            barcodeManager.addConnectionListener(this);
        }
        enumerateScannerDevices();
        spinnerScannerDevices.setSelection(defaultIndex);
    }

    @Override
    public void onClosed() {
        if (emdkManager != null) {
            if (barcodeManager != null) {
                barcodeManager.removeConnectionListener(this);
                barcodeManager = null;
            }
            emdkManager.release();
            emdkManager = null;
        }
        ViseLog.i("Status: " + "EMDK意外关闭!请关闭并重新启动应用程序.");
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
        String status;
        String scannerName = "";

        String statusExtScanner = connectionState.toString();
        String scannerNameExtScanner = scannerInfo.getFriendlyName();

        if (deviceList.size() != 0) {
            scannerName = deviceList.get(scannerIndex).getFriendlyName();
        }

        if (scannerName.equalsIgnoreCase(scannerNameExtScanner)) {//忽略大小写

            switch (connectionState) {
                case CONNECTED:
                    deInitScanner();
                    initScanner();
                    setTrigger();
                    setDecoders();
                    break;
                case DISCONNECTED:
                    deInitScanner();
                    break;
            }
            status = scannerNameExtScanner + ":" + statusExtScanner;
            new AsyncStatusUpdate().execute(status);
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanData> scanData = scanDataCollection.getScanData();
            for (ScanData data : scanData) {

                String dataString = data.getData();

                new AsyncDataUpdate().execute(dataString);
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {
        ScannerStates state = statusData.getState();
        switch (state) {
            case IDLE://关闭
                if (IsStartRead) {
                    try {

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        scanner.read();
                    } catch (ScannerException e) {
                        ViseLog.i(e.getMessage());
                    }
                }
                break;
        }
    }

    private void startScan() {

        if (scanner == null) {
            initScanner();
        }

        if (scanner != null) {
            try {
                if (scanner.isEnabled()) {
                    Log.i("huangmin", "isEnabled" + scanner.isEnabled());
                    // Submit a new read.
                    scanner.read();
                } else {
                    ViseLog.i("Status: 扫描仪未启用");
                }
            } catch (ScannerException e) {
                ViseLog.i("Status: " + e.getMessage());
            }
        }
    }


    private class AsyncStatusUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            ViseLog.i("Status: " + result);
        }
    }

    private class AsyncDataUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return params[0];
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                ViseLog.i("ScanResultData" + String.valueOf(CheckResultList(result)));
                if (CheckResultList(result)) {
                    ScanResStrList.add(result);
                    ScanResultVerifyTask scanResultVerifyTask = new ScanResultVerifyTask();
                    scanResultVerifyTask.execute(result);
                } else {
                    ViseLog.i("存在");
                }
            }
        }
    }

    private class ScanResultVerifyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String Res = null;
            try {
                Res = webService.GetBarcodeAnalyze(params[0], ConnectStr.ConnectionToString);
            } catch (Exception e) {
                ViseLog.i("ScanResultVerifyTask Exception = " + e.getMessage());
            }
            return Res + "," + params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            String[] StrList = result.split(",");
            if (StrList.length > 0) {
                if (!StrList.equals("continue")) {
                    IsStartRead = false;
                }
            }
            ScanResultData scanResult = new ScanResultData();
            scanResult.setScanData(StrList[1]);
            scanResultData.add(scanResult);
            scanResult_rvAdapter.notifyDataSetChanged();
            ViseLog.i("ScanResultVerifyTask Result: " + result);
        }
    }

    private boolean CheckResultList(String result) {
        if (scanResultData.size() >= 0 && !TextUtils.isEmpty(result)) {
            if (ScanResStrList.contains(result)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //资源释放
    public void CleanGC() {
        if (IsStartRead) {
            IsStartRead = false;
        }
        if (barcodeManager != null) {
            barcodeManager.removeConnectionListener(this);
            barcodeManager = null;
        }
        // Release all the resources
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
        //Clean up the objects created by EMDK manager
        if (profileManager != null) {
            profileManager = null;
        }
        if (emdkManager2 != null) {
            emdkManager2.release();
            emdkManager2 = null;
        }

        if (scanner != null) {
            scanner = null;
        }
        if (deviceList != null) {
            deviceList = null;
        }
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

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
            CleanGC();
        }
    }
}
