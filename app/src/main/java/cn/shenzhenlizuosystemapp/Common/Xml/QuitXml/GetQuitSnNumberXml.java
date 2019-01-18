package cn.shenzhenlizuosystemapp.Common.Xml.QuitXml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.QuitSubBody;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.QuitSubmitDataBean;

public class GetQuitSnNumberXml {

    private volatile static GetQuitSnNumberXml getChildTag;

    public static GetQuitSnNumberXml getSingleton() {
        if (getChildTag == null) {
            synchronized (GetQuitSnNumberXml.class) {
                if (getChildTag == null) {
                    getChildTag = new GetQuitSnNumberXml();
                }
            }
        }
        return getChildTag;
    }

    public static List<QuitSubmitDataBean> ReadPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            QuitSubmitDataBean quitSubmitDataBean = null;
            List<QuitSubmitDataBean> quitSubmitDataBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitSubmitDataBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            quitSubmitDataBean = new QuitSubmitDataBean();
                        } else if ("FQty".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            quitSubmitDataBean.setFQty(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && quitSubmitDataBean != null) {
                            quitSubmitDataBeanList.add(quitSubmitDataBean);
                            quitSubmitDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitSubmitDataBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<QuitSubBody> ReadSubBodyPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            QuitSubBody quitSubBody = null;
            List<QuitSubBody> subBodyList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        subBodyList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            quitSubBody = new QuitSubBody();
                        } else if ("FGuid".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            quitSubBody.setFBarcodeLib(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && quitSubBody != null) {
                            subBodyList.add(quitSubBody);
                            quitSubBody = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return subBodyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}