package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.LoginResInfo;
import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;

public class LoginGetResXml {
    private volatile static LoginGetResXml loginGetResXml;

    public static LoginGetResXml getSingleton() {
        if (loginGetResXml == null) {
            synchronized (LoginGetResXml.class) {
                if (loginGetResXml == null) {
                    loginGetResXml = new LoginGetResXml();
                }
            }
        }
        return loginGetResXml;
    }

    public static List<LoginResInfo> GetResInfo(InputStream inputStream) {
        LoginResInfo loginResInfo = null;
        List<LoginResInfo> loginResInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        loginResInfoList = new ArrayList<LoginResInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            loginResInfo = new LoginResInfo();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            loginResInfo.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            loginResInfo.setFInfo(parser.nextText());
                        }
                        break; 
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && loginResInfo != null) {
                            loginResInfoList.add(loginResInfo);
                            loginResInfo = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return loginResInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
