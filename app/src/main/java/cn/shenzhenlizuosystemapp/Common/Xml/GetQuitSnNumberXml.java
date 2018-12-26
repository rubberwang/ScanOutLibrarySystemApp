package cn.shenzhenlizuosystemapp.Common.Xml;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.SubQuitBody;

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
    public static List<SubQuitBody> ReadSubBodyPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            SubQuitBody subQuitBody = null;
            List<SubQuitBody> subBodyList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        subBodyList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            subQuitBody = new SubQuitBody();
                        } else if ("FGuid".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            subQuitBody.setFBarcodeLib(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && subQuitBody != null) {
                            subBodyList.add(subQuitBody);
                            subQuitBody = null;
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