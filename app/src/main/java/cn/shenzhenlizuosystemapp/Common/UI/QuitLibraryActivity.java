package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.Adapter.ScanResult_RvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_QuitRvAdapter;
import cn.shenzhenlizuosystemapp.Common.Adapter.ScanTask_RvAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.ItemData;
//import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.LoginAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.QuitAdapter;
//import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.StockAdapter;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.QuitStockAdapter;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.GetChildTag;
import cn.shenzhenlizuosystemapp.Common.Xml.InputTaskXml;
import cn.shenzhenlizuosystemapp.Common.Xml.QuitTaskXml;
import cn.shenzhenlizuosystemapp.Common.Xml.StocksCallXml;
import cn.shenzhenlizuosystemapp.Common.Xml.StocksXml;
import cn.shenzhenlizuosystemapp.R;

public class QuitLibraryActivity extends BaseActivity implements EMDKListener, DataListener, StatusListener, ScannerConnectionListener{

    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private TextView TV_Time;
    private Spinner Sp_house;
    private Spinner spinnerScannerDevices;
    private Spinner Sp_QuitHouseSpace;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private TextView TV_Scaning;
    private String FGUID = "";
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;

    private WebService webService;
    private OutLibraryObServer outLibraryObServer;
    private ScanResult_RvAdapter scanResult_rvAdapter;
    private ScanTask_QuitRvAdapter scanTask_Quit_rvAdapter;
    private List<ScanResultData> scanResultData;
    private List<ItemData> SpStrList;
    private List<QuitLibraryDetail> quitLibraryDetails;
    private List<QuitTaskRvData> QuittaskRvDataList;
    private List<ScannerInfo> deviceList = null;
    private List<String> ScanResStrList = null;
    List<StockBean> stockBeans = null;
    List<StockBean> stockCellBeans = null;
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
    private String StartSpXml = "";
    private String EndSpXml = "";
    private String Res = null;
    private int State = 1;
    private MyProgressDialog myProgressDialog;

    private int GetSpinnerPos(List<StockBean> Datas, String value) {
        for (int i = 0; i < Datas.size(); i++) {
            if (Datas.get(i).getFName().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_quit_layout;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        ScanResStrList = new ArrayList<String>();
        deviceList = new ArrayList<ScannerInfo>();
        SpStrList = new ArrayList<>();
        scanResultData = new ArrayList<>();
        QuittaskRvDataList = new ArrayList<>();
        stockBeans = new ArrayList<>();
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            ViseLog.i("Scan: " + "调用失败!");//调用失败
            return;
        }
        tools = Tools.getTools();
        outLibraryObServer = new OutLibraryObServer();
        getLifecycle().addObserver(outLibraryObServer);
        //webService = WebService.getSingleton();
        GetOutLibraryBills();
        InitClick();
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
        Sp_QuitHouseSpace = $(R.id.Sp_QuitHouseSpace);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
        webService = new WebService(this);
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
                if (scanTask_Quit_rvAdapter != null) {
                    if (scanTask_Quit_rvAdapter.getselection() >= 0) {
                        IsStartRead = true;
                        startScan();
                    }
                } else {
                    tools.ShowDialog(QuitLibraryActivity.this, "请选择一张任务单再点击扫描");
                }
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
        //扫描数据适配
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
        scanTask_Quit_rvAdapter = new ScanTask_QuitRvAdapter(this,QuittaskRvDataList);
        RV_ScanInfoTable.setAdapter(scanTask_Quit_rvAdapter);
        scanTask_Quit_rvAdapter.setOnItemClickLitener(new ScanTask_QuitRvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (scanTask_Quit_rvAdapter.getselection() == position) {
                    scanTask_Quit_rvAdapter.setSelection(-1);
                    scanTask_Quit_rvAdapter.notifyDataSetChanged();//未选中
                } else {
                    scanTask_Quit_rvAdapter.setSelection(position);
                    scanTask_Quit_rvAdapter.notifyDataSetChanged();//选中
                    GetNullXml(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitSp(List<StockBean> stockBeans, String StockName) {
        QuitStockAdapter QuitStockAdapter = new QuitStockAdapter(stockBeans, QuitLibraryActivity.this);
        Sp_house.setAdapter(QuitStockAdapter);
        int Pos = GetSpinnerPos(stockBeans, StockName);
        Sp_house.setSelection(Pos);
        AsyncGetStocksCell asyncGetStocksCell = new AsyncGetStocksCell();
        asyncGetStocksCell.execute();
    }

    private void GetOutLibraryBills() {
        GetInputLibraryBillsAsyncTask getOutLibraryBillsAsyncTask = new GetInputLibraryBillsAsyncTask();
        getOutLibraryBillsAsyncTask.execute();
    }

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<QuitLibraryDetail>> {

        private RecyclerView recyclerView;

        @Override
        protected List<QuitLibraryDetail> doInBackground(Integer... params) {
            List<QuitLibraryDetail> outLibraryBills = new ArrayList<>();
            stockBeans = new ArrayList<>();
            String QuitBills = "";
            String Stocks = "";
            InputStream in_Heard = null;
            InputStream in_Body = null;
            InputStream in_Stocks = null;
            try {
                QuitBills = webService.QuitWareHouseData(ConnectStr.ConnectionToString, FGUID);
                ViseLog.i("QuitBills = " + QuitBills);
                in_Heard = new ByteArrayInputStream(QuitBills.getBytes("UTF-8"));
                outLibraryBills = GetInputArray(in_Heard);
                in_Body = new ByteArrayInputStream(QuitBills.getBytes("UTF-8"));
                QuittaskRvDataList = QuitTaskXml.getSingleton().GetInputBodyXml(in_Body);
                Stocks = webService.GetStocks(ConnectStr.ConnectionToString);
                in_Stocks = new ByteArrayInputStream(Stocks.getBytes("UTF-8"));
                stockBeans = StocksXml.getSingleton().GetStocksXml(in_Stocks);
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
                    if (QuittaskRvDataList.size() >= 0) {
                        QuittaskRvDataList.remove(0);
                        InitScanRecycler();
                    }
                    if (stockBeans.size() >= 0) {
                        InitSp(stockBeans, result.get(0).getFStock_Name());
                    }
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    TV_BusType.setText(result.get(0).getFTransactionType_Name());
                    TV_Unit.setText(result.get(0).getFPartner_Name());
                    ViseLog.i("quitLibraryDetails 赋值");
                }
                myProgressDialog.dismiss();
            } catch (Exception e) {
                ViseLog.d("Select适配数据错误" + e);
            }
            ViseLog.i("出库单返回数据" + result);
        }

        @Override
        protected void onPreExecute() {
            myProgressDialog.ShowPD("加载数据中...");
        }
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

    private void stopScan() {
        if (scanner != null) {
            try {
                // Reset continuous flag
                IsStartRead = false;
                // Cancel the pending read.
                scanner.cancelRead();

            } catch (ScannerException e) {
            }
        }
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

    private class AsyncGetStocksCell extends AsyncTask<String, Void, List<StockBean>> {

        @Override
        protected List<StockBean> doInBackground(String... params) {
            List<StockBean> stockBeanList = new ArrayList<>();
            try {
                int pos = GetSpinnerPos(stockBeans, Sp_house.getSelectedItem().toString());
                String StocksCell = webService.GetStocksCell(ConnectStr.ConnectionToString, stockBeans.get(pos).getFGuid());
                InputStream inStockCell = new ByteArrayInputStream(StocksCell.getBytes("UTF-8"));
                stockBeanList = StocksCallXml.getSingleton().GetStocksCallXml(inStockCell);
            } catch (Exception e) {
                ViseLog.i("AsyncGetStocksCellException = " + e.getMessage());
            }
            return stockBeanList;
        }

        protected void onPostExecute(List<StockBean> result) {
            if (result.size() >= 0) {
                QuitStockAdapter QuitStockAdapter = new QuitStockAdapter(result, QuitLibraryActivity.this);
                Sp_QuitHouseSpace.setAdapter(QuitStockAdapter);
            }
        }
    }

    private class ScanResultVerifyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String EndStr = null;
            List<ChildTag> childTagList = new ArrayList<>();
            try {
                String MiddleStr = String.format("<Show><name></name><value >%s</value></Show>", params[0]);
                if (State == 1) {
                    EndStr = StartSpXml + MiddleStr + EndSpXml;
                    State = 2;
                } else {
                    EndStr = addSpace(Res, MiddleStr);
                }
                Res = webService.GetBarcodeAnalyze("12B1FFE4-9800-48BB-ACD9-87C99D39C976", EndStr, ConnectStr.ConnectionToString);
                InputStream in_Str = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                childTagList = GetChildTag.getSingleton().getChildTagXml(in_Str);
            } catch (Exception e) {
                ViseLog.i("ScanResultVerifyTask Exception = " + e.getMessage());
            }
            ViseLog.i("ChildTagList[0] = " + childTagList.get(0).getOneChildTag());
            return childTagList.get(0).getOneChildTag() + "," + params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            myProgressDialog.dismiss();
            String[] StrList = result.split(",");
            if (StrList.length > 0) {
                if (!StrList[0].equals("false")) {
                    ViseLog.i("ScanResultVerifyTask Result 关灯");
                    IsStartRead = false;
                }
            }
            ScanResultData scanResult = new ScanResultData();
            scanResult.setScanData(StrList[1]);
            scanResultData.add(scanResult);
            scanResult_rvAdapter.notifyDataSetChanged();
            ViseLog.i("ScanResultVerifyTask Result: " + result);
        }

        @Override
        protected void onPreExecute() {
            myProgressDialog.ShowPD("加载数据中....");
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
        if (tools != null) {
            tools = null;
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

    private QuitLibraryActivity.MyHandler handler = new MyHandler(this);

    class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<QuitLibraryActivity> weakReference;

        public MyHandler(QuitLibraryActivity QuitLibraryActivity) {
            weakReference = new WeakReference<QuitLibraryActivity>(QuitLibraryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            QuitLibraryActivity handlerMemoryActivity = weakReference.get();
            if (handlerMemoryActivity != null) {
                switch (msg.what) {
                    case 1: {
                        String Xml = msg.getData().getString("Xml");
                        ViseLog.i("Xml = " + Xml);
                        StartSpXml = Xml.substring(0, Xml.indexOf("<Show>"));
                        EndSpXml = Xml.substring(Xml.indexOf("<BarcodeLib>"), Xml.length());
                        tools.show(QuitLibraryActivity.this, "选取任务成功");
                        break;
                    }
                    case 2: {
                        tools.ShowDialog(QuitLibraryActivity.this, "选择入库任务失败");
                        break;
                    }
                }
            } else {
                ViseLog.i("没有得到Activity实例不进行操作");
            }
        }
    }

    private class GetNullXmlSyncThread extends Thread {

        private String materialID = "";

        GetNullXmlSyncThread(String materialID) {
            this.materialID = materialID;
        }

        @Override
        public void run() {
            //执行耗时操作
            Message msg = new Message();
            try {
                String Result = webService.GetBarcodeAnalyze("12B1FFE4-9800-48BB-ACD9-87C99D39C976", "", ConnectStr.ConnectionToString);
                if (!TextUtils.isEmpty(Result)) {
                    msg.what = 1;
                    msg.getData().putString("Xml", Result);
                    handler.sendMessage(msg);
                } else {
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ViseLog.d("GetNullXmlSyncThread Exception" + e);
            }
        }
    }

    private void GetNullXml(int pos) {
        GetNullXmlSyncThread getNullXmlSyncThread = new GetNullXmlSyncThread(QuittaskRvDataList.get(pos).getFGUID());
        getNullXmlSyncThread.start();
    }

    private String addSpace(String bankAccountNumber, String AddStr) {
        if (bankAccountNumber == null) {
            return "";
        }
        char[] strs = bankAccountNumber.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if (i == 11) {
                sb.append(AddStr);
            }
        }
        String trim = sb.toString().trim();
        return trim;
    }
}
