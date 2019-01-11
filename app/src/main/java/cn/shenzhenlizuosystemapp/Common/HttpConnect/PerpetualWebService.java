package cn.shenzhenlizuosystemapp.Common.HttpConnect;

import android.content.Context;
import android.text.TextUtils;

import com.vise.log.ViseLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;

public class PerpetualWebService {

    private static PerpetualWebService perpetualWebService;
    private static String LastNameSpaceAddress = "http://dyrj.net/";

    public static PerpetualWebService getSingleton(Context context) {
        if (perpetualWebService == null) {
            synchronized (PerpetualWebService.class) {
                if (perpetualWebService == null) {
                    perpetualWebService = new PerpetualWebService();
                }
            }
        }
        return perpetualWebService;
    }

    public String GetCloudServers(String Account, String Password) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetCloudServers");
        soapObject.addProperty("Account", Account);
        soapObject.addProperty("Password", Password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(ConnectStr.Perpetual_URL);
        httpTransportSE.call("http://dyrj.net/GetCloudServers", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }

    public String GetCloudServer(String ServerID) throws IOException, XmlPullParserException, ClassCastException {
        SoapObject soapObject = new SoapObject(LastNameSpaceAddress, "GetCloudServer");
        soapObject.addProperty(ServerID, ServerID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(ConnectStr.Perpetual_URL);
        httpTransportSE.call("http://dyrj.net/GetCloudServer", envelope);
        Object object = (Object) envelope.getResponse();
        String Result = object.toString();
        return Result;
    }
}
