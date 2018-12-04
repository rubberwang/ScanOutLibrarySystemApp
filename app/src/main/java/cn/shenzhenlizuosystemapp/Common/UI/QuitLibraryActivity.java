package cn.shenzhenlizuosystemapp.Common.UI;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.ItemData;
import cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.QuitStockAdapter;
import cn.shenzhenlizuosystemapp.Common.View.MyProgressDialog;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.Xml.GetChildTag;
import cn.shenzhenlizuosystemapp.Common.Xml.GetQuitSnNumberXml;
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
    private TextView TV_Cancel;
    private TextView TV_Sumbit;
    private String FGUID = "";
    private String HeardID = "";
    private RecyclerView RV_GetInfoTable;
    private RecyclerView RV_ScanInfoTable;

    private WebService webService;
    private OutLibraryObServer outLibraryObServer;
    private ScanResult_RvAdapter scanResult_rvAdapter;
    private ScanTask_QuitRvAdapter scanTask_Quit_rvAdapter;
    private List<ScanResultData> scanResultData;
    private List<ItemData> SpStrList;
    private List<QuitTaskRvData> QuittaskRvDataList;
    private List<ScannerInfo> deviceList = null;
    private List<String> ScanResStrList = null;
    private List<StockBean> stockBeans = null;
    private List<StockBean> stockBeanList = null;
    private List<QuitSubmitDataBean> OverallSituationList = null;
    private List<String> MaterialIDList = null;
    private List<String> BodyIdList = null;
    private Tools tools;

    /**
     * 扫描
     */
    private int scannerIndex = 0;
    private int SpHouseIndex = 0;
    private int SpInputHouseSpaceIndex = 0;
    private int defaultIndex = 0;
    private Scanner scanner = null;
    private BarcodeManager barcodeManager = null;
    private EMDKManager emdkManager = null;
    private ProfileManager profileManager = null;
    private EMDKManager emdkManager2 = null;
    private boolean IsStartRead = false;
    private String StartSpXml = "";
    private String EndSpXml = "";
    private String Res = "";
    private int State = 1;
    private MyProgressDialog myProgressDialog;
    private Context MContect;
    private boolean IsScaning = false;
    private boolean IsCatch = true;
    private boolean IsClean = false;
    private int RV_ScanInfoTableIndex = 0;

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
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            ViseLog.i("Scan: " + "调用失败!");//调用失败
            return;
        }
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        ScanResStrList = new ArrayList<String>();
        deviceList = new ArrayList<ScannerInfo>();
        SpStrList = new ArrayList<>();
        scanResultData = new ArrayList<>();
        QuittaskRvDataList = new ArrayList<>();
        stockBeans = new ArrayList<>();
        OverallSituationList = new ArrayList<>();
        MaterialIDList = new ArrayList<>();
        BodyIdList = new ArrayList<>();
        MContect = new WeakReference<>(QuitLibraryActivity.this).get();
        tools = Tools.getTools();
        outLibraryObServer = new OutLibraryObServer();
        getLifecycle().addObserver(outLibraryObServer);
        webService = WebService.getSingleton(this);
        InitClick();
        InitRecycler();
        GetOutLibraryBills();
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
        TV_Cancel = $(R.id.TV_Cancel);
        TV_Sumbit = $(R.id.TV_Sumbit);
        myProgressDialog = new MyProgressDialog(this, R.style.CustomDialog);
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
                        if (IsScaning) {
                            //停止扫描
                            IsScaning = false;
                            TV_Scaning.setText(R.string.scaning);
                            ShowCancelDialog(MContect, "是否确定取消本次扫描");
                        } else {
                            IsStartRead = true;
                            IsScaning = true;
                            TV_Scaning.setText(R.string.stopScan);
                            startScan();
                        }
                    } else {
                        tools.ShowDialog(QuitLibraryActivity.this, "请选择一张任务单再点击扫描");
                    }
                } else {
                    tools.show(MContect, "网络连接错误");
                }
            }
        });

        TV_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.ShowOnClickDialog(MContect, "确定取消本次扫描吗？扫描所有数据全部被清空", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewManager.getInstance().finishActivity(QuitLibraryActivity.this);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tools.DisappearDialog();
                    }
                }, false);
            }
        });

        TV_Sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SumbitData();
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
        Log.i("MainActivity", "onPause()");
    }

    private void InitRecycler() {
        //扫描数据适配
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_GetInfoTable.addItemDecoration(new RvLinearManageDivider(this, LinearLayoutManager.VERTICAL));
        RV_GetInfoTable.setLayoutManager(layoutManager);
//        scanResult_rvAdapter = new ScanResult_RvAdapter(this, scanResultData);
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
                if (Integer.parseInt(QuittaskRvDataList.get(position).getTV_shouldSend().split("\\.")[0]) <= Integer.parseInt(QuittaskRvDataList.get(position).getTV_thisSend().split("\\.")[0]) +
                        Integer.parseInt(QuittaskRvDataList.get(position).getTV_alreadySend().split("\\.")[0])) {
                    tools.ShowDialog(MContect, "这张单已扫描完成");
                } else {
                    if (scanTask_Quit_rvAdapter.getselection() == -1) {
                        if (RV_ScanInfoTableIndex != position) {
                            RV_ScanInfoTableIndex = position;
                        }
                        scanTask_Quit_rvAdapter.setSelection(position);
                        scanTask_Quit_rvAdapter.notifyDataSetChanged();//选中
                        GetNullXml(position);
                    } else {
                        tools.show(MContect, "请扫描完当前任务");
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void InitSp(List<StockBean> stockBeans, String StockName) {
        if (stockBeans.size() >= 0) {
            QuitStockAdapter QuitStockAdapter = new QuitStockAdapter(stockBeans, QuitLibraryActivity.this);
            Sp_house.setAdapter(QuitStockAdapter);
            int Pos = GetSpinnerPos(stockBeans, StockName);
            Sp_house.setSelection(Pos);
            Sp_house.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (SpHouseIndex != i) {
                        SpHouseIndex = i;
                    }
                    AsyncGetStocksCell asyncGetStocksCell = new AsyncGetStocksCell(i);
                    asyncGetStocksCell.execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
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
                    HeardID = result.get(0).getFGuid();
                    ViseLog.i("quitLibraryDetails 赋值");
                }
                myProgressDialog.dismiss();
            } catch (Exception e) {
                ViseLog.d("GetQuitLibraryBillsAsyncTask" + e.getMessage());
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
                if (CheckResultList(result) && IsCatch) {
                    IsCatch = false;
                    ScanResStrList.add(result);
                    ScanResultVerifyTask scanResultVerifyTask = new ScanResultVerifyTask();
                    scanResultVerifyTask.execute(result);
                } else {
                    ViseLog.i("没进入上传到服务器验证");
                }
            } else {
                ViseLog.i("ScanResult = NUll");
            }
        }
    }

    private class AsyncGetStocksCell extends AsyncTask<String, Void, List<StockBean>> {

        private int pos = 0;

        AsyncGetStocksCell(int pos) {
            this.pos = pos;
        }

        @Override
        protected List<StockBean> doInBackground(String... params) {
            stockBeanList = new ArrayList<>();
            try {
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
                Sp_QuitHouseSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (SpInputHouseSpaceIndex != i) {
                            SpInputHouseSpaceIndex = i;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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
                } else {
                    EndStr = addSpace(Res, MiddleStr);
                }
                ViseLog.i("QuittaskRvDataList.get(RV_ScanInfoTableIndex).getTV_materID() = " + QuittaskRvDataList.get(RV_ScanInfoTableIndex).getFGUID());
                Res = webService.GetBarcodeAnalyze(QuittaskRvDataList.get(RV_ScanInfoTableIndex).getFGUID(), EndStr, ConnectStr.ConnectionToString, ConnectStr.USERNAME);
                if (TextUtils.isEmpty(Res)) {
                    ViseLog.i("res为空" + Res);
                    return "";
                }
                InputStream in_Str = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                childTagList = GetChildTag.getSingleton().getChildTagXml(in_Str);
                in_Str.close();
                Log.i("huangmin", "AsyncDataSuccess = " + Res);
//                return childTagList.get(childTagList.size() - 1).getOneChildTag() + "," + params[0] + "," + childTagList.get(0).getName();
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            myProgressDialog.dismiss();
            if (!TextUtils.isEmpty(result)) {
                String[] StrList = result.split(",");
                Log.i("huangmin", "result = " + result);
                if (StrList.length > 0) {
                    if (StrList[0].equals("true")) {
                        Log.i("huangmin", "货物上传成功");
                        IsCatch = true;
                        GetNullXml(RV_ScanInfoTableIndex);
                        SaveSNSum();
                        Res = "";
                        State = 1;
                    } else {
                        if (State == 1) {
                            State = 2;
                            MaterialIDList.add(QuittaskRvDataList.get(RV_ScanInfoTableIndex).getTV_materID());
                            BodyIdList.add(QuittaskRvDataList.get(scanTask_Quit_rvAdapter.getselection()).getFGUID());
                        }
                        ScanResultData scanResult = new ScanResultData();
                        scanResult.setScanData(StrList[1]);
                        scanResult.setName(StrList[2]);
                        scanResultData.add(scanResult);
                        scanResult_rvAdapter.notifyDataSetChanged();
                        IsCatch = true;
                        ViseLog.i("ScanResultVerifyTask Result: " + result);
                    }
                }
            } else {
                ViseLog.i("异常返回result = NULL");
                IsCatch = true;
                GetNullXml(RV_ScanInfoTableIndex);
                scanResultData.clear();
                ScanResStrList.clear();
                scanResult_rvAdapter.notifyDataSetChanged();
                Res = "";
                State = 1;
                tools.ShowDialog(MContect, "扫描数据错误");
            }
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
            tools.ToastCancel();
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
                        tools.show(MContect, "获取模版");
                        break;
                    }
                    case 2: {
                        tools.ShowDialog(QuitLibraryActivity.this, "选择出库任务失败");
                        break;
                    }
                    case 3: {
                        myProgressDialog.dismiss();
                        break;
                    }
                    case 4: {
                        myProgressDialog.dismiss();
                        tools.ShowOnClickDialog(MContect, "提交成功", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                                ViewManager.getInstance().finishActivity(QuitLibraryActivity.this);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }, true);
                        break;
                    }
                    case 5: {
                        myProgressDialog.dismiss();
                        tools.ShowOnClickDialog(MContect, "提交数据有错误", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tools.DisappearDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }, true);
                        break;
                    }
                    case 6: {
                        tools.ShowDialog(MContect, "没有这个物料规则请选择其它物料规则");
                        scanTask_Quit_rvAdapter.setSelection(-1);
                        scanResult_rvAdapter.notifyDataSetChanged();
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
                String Result = webService.GetBarcodeAnalyze(QuittaskRvDataList.get(RV_ScanInfoTableIndex).getTV_materID(), "", ConnectStr.ConnectionToString, ConnectStr.USERNAME);
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
                msg.what = 6;
                handler.sendMessage(msg);
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

    public void ShowCancelDialog(Context context, String msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialoglayout_cancel, null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        TextView Tv_Ensure = view.findViewById(R.id.Tv_Ensure);
        TextView Tv_Close = view.findViewById(R.id.Tv_Close);
        TextView Tv_Msg = view.findViewById(R.id.TvMessageDialog);
        Tv_Msg.setText(msg);

        Tv_Ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopScanUpdata();
                dialog.dismiss();
            }
        });

        Tv_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        ViseLog.i("屏幕宽高" + screenWidth + "  " + screenHeight);
        if (screenWidth > 900 && screenHeight > 1600) {
            lp.width = 900; // 宽度
            lp.height = 600; // 高度
        } else {
            lp.width = 520; // 宽度
            lp.height = 400; // 高度
        }
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void SaveSNSum() {
        try {
            if (!TextUtils.isEmpty(Res) && Res.length() > 0) {
                ViseLog.i("扫描完一个物料");
                InputStream in_Str = null;
                in_Str = new ByteArrayInputStream(Res.getBytes("UTF-8"));
                List<QuitSubmitDataBean> inputSubmitDataBeanList = GetQuitSnNumberXml.getSingleton().ReadPullXML(in_Str);
                QuitSubmitDataBean InputBean = new QuitSubmitDataBean();
                InputBean.setSn(inputSubmitDataBeanList.get(0).getSn());
                InputBean.setNumber(inputSubmitDataBeanList.get(0).getNumber());
                OverallSituationList.add(InputBean);
                int ThisSendSum = Integer.parseInt(QuittaskRvDataList.get(scanTask_Quit_rvAdapter.getselection()).getTV_thisSend().split("\\.")[0]);
                ViseLog.i("ThisSendSum = " + ThisSendSum);
                QuittaskRvDataList.get(scanTask_Quit_rvAdapter.getselection()).setTV_thisSend(String.valueOf(ThisSendSum + Integer.parseInt(inputSubmitDataBeanList.get(0).getNumber())));
                scanTask_Quit_rvAdapter.notifyDataSetChanged();
                scanResultData.clear();
                ScanResStrList.clear();
                scanResult_rvAdapter.notifyDataSetChanged();
                IsClean = true;
                ViseLog.i("inputSubmitDataBeanList Sn= " + inputSubmitDataBeanList.get(0).getSn() + "Number = " + inputSubmitDataBeanList.get(0).getNumber());
                in_Str.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("SaveSNSumException = " + e.getMessage());
        }
    }

    private void StopScanUpdata() {
        stopScan();
        scanResultData.clear();
        ScanResStrList.clear();
        scanResult_rvAdapter.notifyDataSetChanged();
        scanTask_Quit_rvAdapter.setSelection(-1);
        scanTask_Quit_rvAdapter.notifyDataSetChanged();
    }

    private void SumbitData() {
        if (OverallSituationList.size() > 0) {
            myProgressDialog.ShowPD("正在提交...");
            SumbitDataThread sumbitDataThread = new SumbitDataThread();
            sumbitDataThread.start();
        } else {
            tools.ShowDialog(MContect, "一个扫描完成的物料都没有");
        }
    }

    private class SumbitDataThread extends Thread {

        @Override
        public void run() {
            //执行耗时操作
            Message msg = new Message();
            try {
                String DetailedListXml = GetQuitSnNumberXml.getSingleton().CreateInputXmlStr(OverallSituationList, MaterialIDList,BodyIdList);
                ViseLog.i("DetailedListXml = " + DetailedListXml + " Sp_house.getSelectedItem().toString() = " + stockBeans.get(SpHouseIndex).getFName()
                        + "Sp_InputHouseSpace.getSelectedItem().toString() = " + stockBeanList.get(SpInputHouseSpaceIndex).getFName());

                String Result = webService.CreateInStockBill(ConnectStr.ConnectionToString, HeardID,
                        ConnectStr.USERNAME, stockBeans.get(SpHouseIndex).getFName(), stockBeanList.get(SpInputHouseSpaceIndex).getFName(), DetailedListXml);
                ViseLog.i("SumbitResult = " + Result);
                if (Result.equals("Success")) {
                    msg.what = 4;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 5;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ViseLog.d("GetNullXmlSyncThread Exception" + e);
            }
        }
    }
}
