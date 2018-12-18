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


    public String QuitLibraryNote(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetOutStockNotices");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetOutStockNotices", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }


    public String QuitWareHouseData (String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetOutStockNoticeBill");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("BillGuid", BillGuid);
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


    public String CheckLibraryNote(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetCheckStockNotices");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetCheckStockNotices", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }


    public String CheckWareHouseData (String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetCheckStockNoticeBill");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("BillGuid", BillGuid);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetCheckStockNoticeBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String CommitLibraryNote (String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetAdjustStockNotices");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetAdjustStockNotices", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetBarcodeAnalyze(String MaterielID,String Data,String ConnectionToString,String UserName) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "BarcodeAnalyze");
        
        soapObject.addProperty("MaterielID", MaterielID);
        soapObject.addProperty("UserName", UserName);
        soapObject.addProperty("Data", Data);
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        
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

    public String CreateInStockBill(String ConnectionToString,String UserName,String Stock,String StockCell,String InStockBill) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "CreateInStockBill");

        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("UserName", UserName);
        soapObject.addProperty("StockID", Stock);
        soapObject.addProperty("StockCellID", StockCell);
        soapObject.addProperty("InStockBill", InStockBill);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/CreateInStockBill", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String CreateOutStockBill(String ConnectionToString,String inStockNoticeID,String UserName,String StockName,String StockCellName,String Products) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "CreateOutStockBill");

        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("inStockNoticeID", inStockNoticeID);
        soapObject.addProperty("UserName", UserName);
        soapObject.addProperty("StockName", StockName);
        soapObject.addProperty("StockCellName", StockCellName);
        soapObject.addProperty("Products", Products);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/CreateOutStockBill", envelope);
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
}

