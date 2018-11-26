package cn.shenzhenlizuosystemapp.Common.HttpConnect;

import android.content.Context;
import android.content.SharedPreferences;

import org.ksoap2.SoapEnvelope;
import com.vise.log.ViseLog;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.text.TextUtils;
import cn.shenzhenlizuosystemapp.Common.UI.SettingActivity;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;

import java.io.IOException;

public class WebService {
    private SharedPreferences sharedPreferences;
    private static  String  LastNameSpaceAddress ="http://www.lzbarcode.com/";
    private String urlAddress;


//"http://192.168.1.6:809/DBS/WebService/WebAPI.asmx";
    public WebService(Context context) {
        Tools tools = new Tools();
        sharedPreferences = tools.InitSharedPreferences(context);
        if (TextUtils.isEmpty(tools.GetStringData(sharedPreferences, "ServerIPAddress"))) {
            urlAddress = "";
        } else {
            urlAddress = tools.GetStringData(sharedPreferences, "ServerIPAddress");
            urlAddress = "http://" + urlAddress + "/DBS/WebService/WebAPI.asmx";
        }
        ViseLog.i("初始WebServer IP = " + urlAddress);
    }
    public String getLastUrlAddress() {
        return urlAddress;
    }


    public String getProject() throws IOException, XmlPullParserException, ClassCastException {
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

    public String LoginIn(String username, String password,String str) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "Login");
        soapObject.addProperty("UserName", username);
        soapObject.addProperty("Usepwd", password);
        soapObject.addProperty("ConnectionToString", str);

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


    public String GetLibraryNote(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetInStockNotices");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetInStockNotices", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetWareHouseData(String ConnectionToString,String BillGuid) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetInStockNoticeBill");
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("BillGuid", BillGuid);

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

    public String GetBarcodeAnalyze(String MaterielID,String Data,String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "BarcodeAnalyze ");
        
        soapObject.addProperty("MaterielID", MaterielID);
        soapObject.addProperty("Data", Data);
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/BarcodeAnalyze ", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetStocks(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetStocks");
        soapObject.addProperty("ConnectionToString", ConnectionToString);

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
        soapObject.addProperty("ConnectionToString", ConnectionToString);
        soapObject.addProperty("StockGuid", StockGuid);

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
}

