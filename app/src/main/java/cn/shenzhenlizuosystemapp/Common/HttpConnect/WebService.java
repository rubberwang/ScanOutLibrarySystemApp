package cn.shenzhenlizuosystemapp.Common.HttpConnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;

public class WebService {
    private SharedPreferences sharedPreferences;
    private static  String  LastNameSpaceAddress ="http://www.lzbarcode.com/";
    private String urlAddress;
    private static WebService webService;

    public static WebService getSingleton(Context context) {
        if (webService == null) {
            synchronized (WebService.class) {
                if (webService == null) {
                    webService = new WebService(context);
                }
            }
        }
        return webService;
    }
    
//"http://192.168.1.6:809/DCS/WebService/DBS.WebAPI.asmx";
    public WebService(Context context) {
        Tools tools = new Tools();
        sharedPreferences = tools.InitSharedPreferences(context);
        if (TextUtils.isEmpty(tools.GetStringData(sharedPreferences, "ServerIPAddress"))) {
            urlAddress = "";
        } else {
            urlAddress = tools.GetStringData(sharedPreferences, "ServerIPAddress");
            urlAddress = "http://" + urlAddress + "/DCS/WebService/DBS.WebAPI.asmx";
        }
        ViseLog.i("初始WebServer IP = " + urlAddress);
    }
    public String getLastUrlAddress() {
        return urlAddress;
    }


    public String getProject()throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetProjects");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress );
        httpTransportSE.call("http://www.lzbarcode.com/GetProjects", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String LoginIn(String ProjectID, String UserName,String Password) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "Login");
        soapObject.addProperty("ProjectID", ProjectID);
        soapObject.addProperty("UserName", UserName);
        soapObject.addProperty("Password", Password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/Login", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }


    public String GetLibraryNote(String ConnectionID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetInStockNoticeBillsList");
        soapObject.addProperty("ConnectionID", ConnectionID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetInStockNoticeBillsList", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetWareHouseData(String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetInStockNoticeBill");
        soapObject.addProperty("ConnectionID", ConnectionToString);
        soapObject.addProperty("BillID", BillGuid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetInStockNoticeBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }


    public String GetCheckLibraryNote(String ConnectionID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetInStockNoticeBillsList");
        soapObject.addProperty("ConnectionID", ConnectionID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetInStockNoticeBillsList", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetCheckWareHouseData(String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetCheckStockBill");
        soapObject.addProperty("ConnectionID", ConnectionToString);
        soapObject.addProperty("BillID", BillGuid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetCheckStockBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetBarcodeAnalyze(String ConnectionID,String MaterialID,String LabelTempletID,String Barcodes,boolean AllowAddNew) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "BarcodeAnalyze");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("MaterialID", MaterialID);
        soapObject.addProperty("LabelTempletID", LabelTempletID);
        soapObject.addProperty("Barcodes", Barcodes);
        soapObject.addProperty("AllowAddNew", AllowAddNew);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/BarcodeAnalyze", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetStocks(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetStocks");
        soapObject.addProperty("ConnectionID", ConnectionToString);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetStocks", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetStocksCell(String ConnectionToString,String StockGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetStocksCell");
        soapObject.addProperty("ConnectionID", ConnectionToString);
        soapObject.addProperty("StockID", StockGuid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetStocksCell", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }


    public String GetMaterialLabelTemplet(String ConnectionID,String MaterialID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetMaterialLabelTemplet");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("MaterialID", MaterialID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetMaterialLabelTemplet", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetLabelTempletBarcodes(String ConnectionID,String LabelTempletID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetLabelTempletBarcodes");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("LabelTempletID", LabelTempletID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetLabelTempletBarcodes", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String LockedBillBody(String ConnectionID,String BillBodyID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "LockedBillBody");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("BillBodyID", BillBodyID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/LockedBillBody", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String ClearLockedBillBody(String ConnectionID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "ClearLockedBillBody");
        soapObject.addProperty("ConnectionID", ConnectionID);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/ClearLockedBillBody", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String CreatInStockBill(String ConnectionID,String UserID,String XmlData) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "CreatInStockBill");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("UserCode", UserID);
        soapObject.addProperty("XmlData", XmlData);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/CreatInStockBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String CreatQuitStockBill(String ConnectionID,String UserID,String XmlData) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "CreatOutStockBill");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("UserCode", UserID);
        soapObject.addProperty("XmlData", XmlData);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/CreatOutStockBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }
    
    public String UnLockedBillBody(String ConnectionID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "UnLockedBillBody");
        soapObject.addProperty("ConnectionID", ConnectionID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/UnLockedBillBody", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetQuitLibraryNote(String ConnectionID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetOutStockNoticeBillsList");
        soapObject.addProperty("ConnectionID", ConnectionID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetOutStockNoticeBillsList", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }
    
    public String GetQuitWareHouseData (String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetOutStockNoticeBill");
        soapObject.addProperty("ConnectionID", ConnectionToString);
        soapObject.addProperty("BillID", BillGuid);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetOutStockNoticeBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetAdjustStockNoticeBillsList (String ConnectionID,String AdjustStockType) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetAdjustStockNoticeBillsList");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("AdjustStockType", AdjustStockType);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetAdjustStockNoticeBillsList", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetAdjustStockNoticeBill(String ConnectionID,String BillID,String AdjustStockType) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetAdjustStockNoticeBill");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("BillID", BillID);
        soapObject.addProperty("AdjustStockType", AdjustStockType);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetAdjustStockNoticeBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }
    
    public String CreatAdjustStockBill(String ConnectionID,String UserCode,String XmlData) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "CreatAdjustStockBill");
        soapObject.addProperty("ConnectionID", ConnectionID);
        soapObject.addProperty("UserCode", UserCode);
        soapObject.addProperty("XmlData", XmlData);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/CreatAdjustStockBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }
    
}

