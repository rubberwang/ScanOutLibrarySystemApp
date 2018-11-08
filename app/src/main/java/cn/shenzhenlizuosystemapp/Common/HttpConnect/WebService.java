package cn.shenzhenlizuosystemapp.Common.HttpConnect;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WebService {
    private static  String  LastNameSpaceAddress ="http://www.lzbarcode.com/";
    private static WebService webService;

    private static final String urlAddress
            = "http://192.168.1.6:809/DBS/WebService/WebAPI.asmx";

    public static WebService getSingleton() {
        if (webService == null) {
            synchronized (WebService.class) {
                if (webService == null) {
                    webService = new WebService();
                }
            }
        }
        return webService;
    }

    public static String getProject() throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetProjects");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(urlAddress);
        httpTransportSE.call("http://www.lzbarcode.com/GetProjects", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public static String LoginIn(String username, String password,String str) throws IOException, XmlPullParserException, ClassCastException {
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


    public static String GetSelectOutListData(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
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

    public static String PutSelectOutListData(String ConnectionToString) throws IOException, XmlPullParserException, ClassCastException {
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

}

