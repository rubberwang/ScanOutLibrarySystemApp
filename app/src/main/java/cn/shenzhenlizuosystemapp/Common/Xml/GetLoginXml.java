package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;

public class GetLoginXml {
    private volatile static GetLoginXml getLoginXml;

    public static GetLoginXml getSingleton() {
        if (getLoginXml == null) {
            synchronized (GetLoginXml.class) {
                if (getLoginXml == null) {
                    getLoginXml = new GetLoginXml();
                }
            }
        }
        return getLoginXml;
    }

    public static String GetInfoTag(InputStream inputStream) {
        String LastInfoStr = "";
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("FInfo")) {
                            LastInfoStr = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("FInfo")
                                && LastInfoStr != null) {
                            return LastInfoStr;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return LastInfoStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<GetProjectResult> GetAPP_Project(InputStream inputStream) {
        GetProjectResult getProjectResults = null;
        List<GetProjectResult> getProjectResultList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        getProjectResultList = new ArrayList<GetProjectResult>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("APP")) {
                            getProjectResults = new GetProjectResult();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            getProjectResults.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FName")) {
                            getProjectResults.setFName(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("APP")
                                && getProjectResults != null) {
                            getProjectResultList.add(getProjectResults);
                            getProjectResults = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return getProjectResultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
