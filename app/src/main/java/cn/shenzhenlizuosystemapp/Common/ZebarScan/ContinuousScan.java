package cn.shenzhenlizuosystemapp.Common.ZebarScan;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Port.ZebarScanResult;

public class ContinuousScan implements EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener, BarcodeManager.ScannerConnectionListener {

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    private Context context;
    private static volatile ContinuousScan cancelRead;
    private ZebarScanResult zebarScanError;

    private List<ScannerInfo> deviceList = null;

    private int defaultIndex = 0;
    private int scannerIndex = 0;

    public static ContinuousScan getCancelRead(Context context) {
        if (cancelRead == null) {
            synchronized (ContinuousScan.class) {
                if (cancelRead == null) {
                    cancelRead = new ContinuousScan(context);
                }
            }
        }
        return cancelRead;
    }

    private ContinuousScan(Context context) {
        this.context = context;
        if (emdkManager != null) {
            barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

            if (barcodeManager != null) {
                barcodeManager.addConnectionListener(this);
            }
            enumerateScannerDevices();//适配设备支持模式
            initScanner();
            setTrigger();
            setDecoders();
        }
    }

    public boolean HowState() {
        EMDKResults results = EMDKManager.getEMDKManager(context, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            return false;
        }
        return true;
    }

    public void SetResultPort(ZebarScanResult zebarScanResult) {
        this.zebarScanError = zebarScanResult;
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();
            } catch (Exception e) {
                zebarScanError.OnBad("Status: " + e.getMessage());
            }
            try {
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
            } catch (Exception e) {
                zebarScanError.OnBad("Status: " + e.getMessage());
            }
            try {
                scanner.release();
            } catch (Exception e) {
                zebarScanError.OnBad("Status: " + e.getMessage());
            }
            scanner = null;
        }
    }

    private void initScanner() {
        if (scanner == null) {
            if ((deviceList != null) && (deviceList.size() != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(scannerIndex));
                Log.i("huangmin", "deviceListdata" + deviceList.get(scannerIndex));
            } else {
                zebarScanError.OnBad("Status: " + "未能获得指定的扫描仪设备!请关闭并重新启动应用程序。");
                return;
            }

            if (scanner != null) {

                scanner.addDataListener(this);
                scanner.addStatusListener(this);

                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    zebarScanError.OnBad(e.getMessage());
                }
            } else {
                zebarScanError.OnBad("未能初始化设备");
            }
        }
    }

    private void setTrigger() {
        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            scanner.triggerType = Scanner.TriggerType.SOFT_ALWAYS;//软解
        }
    }

    private void setDecoders() {

        if (scanner == null) {
            initScanner();
        }

        if ((scanner != null) && (scanner.isEnabled())) {
            try {
                ScannerConfig config = scanner.getConfig();

                config.decoderParams.ean8.enabled = true;

                config.decoderParams.ean13.enabled = true;

                config.decoderParams.code39.enabled = true;

                config.decoderParams.code128.enabled = true;

                scanner.setConfig(config);

            } catch (ScannerException e) {
                zebarScanError.OnBad(e.getMessage());
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
                zebarScanError.OnBad("Status: " + "未能获得支持的扫描器设备列表!请重启设备.");
            }
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {

        this.emdkManager = emdkManager;
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

        if (barcodeManager != null) {
            barcodeManager.addConnectionListener(this);
        }
        enumerateScannerDevices();
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
        zebarScanError.OnBad("Status: " + "EMDK意外关闭!请关闭并重新启动应用程序.");
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, BarcodeManager.ConnectionState connectionState) {
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
            zebarScanError.OnBad(status);
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            for (ScanDataCollection.ScanData data : scanData) {
                String dataString = data.getData();
                new SuccessDataUpdate().execute(dataString);
            }
        }
    }

    private void StopScan() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
            } catch (ScannerException e) {
                zebarScanError.OnBad("Status: " + e.getMessage());
            }
        }
    }

    public ContinuousScan StartScan() {
        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            try {
                if (scanner.isEnabled()) {
                    scanner.read();
                } else {
                    zebarScanError.OnBad("Status: 扫描仪未启用");
                }
            } catch (ScannerException e) {
                zebarScanError.OnBad("Status: " + e.getMessage());
            }
        }
        return getCancelRead(context);
    }

    @Override
    public void onStatus(StatusData statusData) {

        StatusData.ScannerStates state = statusData.getState();
        switch (state) {
            case IDLE://关闭
                try {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scanner.read();
                } catch (ScannerException e) {
                    zebarScanError.OnBad(e.getMessage());
                }
                break;
        }
    }

    private void Close() {
        deInitScanner();

        // Remove connection listener
        if (barcodeManager != null) {
            barcodeManager.removeConnectionListener(this);
            barcodeManager = null;
        }

        // Release all the resources
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    private class SuccessDataUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            zebarScanError.OnSuccess(result);
        }
    }
}
