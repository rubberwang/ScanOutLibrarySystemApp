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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.SubBody;

public class GetInputSnNumberXml {

    private volatile static GetInputSnNumberXml getChildTag;

    public static GetInputSnNumberXml getSingleton() {
        if (getChildTag == null) {
            synchronized (GetInputSnNumberXml.class) {
                if (getChildTag == null) {
                    getChildTag = new GetInputSnNumberXml();
                }
            }
        }
        return getChildTag;
    }

    public static List<InputSubmitDataBean> ReadPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            InputSubmitDataBean inputSubmitDataBean = null;
            List<InputSubmitDataBean> inputSubmitDataBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputSubmitDataBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            inputSubmitDataBean = new InputSubmitDataBean();
                        } else if ("FQty".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            inputSubmitDataBean.setFQty(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && inputSubmitDataBean != null) {
                            inputSubmitDataBeanList.add(inputSubmitDataBean);
                            inputSubmitDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputSubmitDataBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<SubBody> ReadSubBodyPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            SubBody subBody = null;
            List<SubBody> subBodyList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        subBodyList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            subBody = new SubBody();
                        } else if ("FGuid".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            subBody.setFBarcodeLib(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && subBody != null) {
                            subBodyList.add(subBody);
                            subBody = null;
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