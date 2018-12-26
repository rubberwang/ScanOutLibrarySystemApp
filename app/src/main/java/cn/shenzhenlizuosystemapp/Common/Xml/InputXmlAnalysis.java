package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputLibraryBill;
import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;
import cn.shenzhenlizuosystemapp.Common.WebBean.InputAllBean;

public class InputXmlAnalysis {
    private volatile static InputXmlAnalysis inputXmlAnalysis;

    public static InputXmlAnalysis getSingleton() {
        if (inputXmlAnalysis == null) {
            synchronized (InputXmlAnalysis.class) {
                if (inputXmlAnalysis == null) {
                    inputXmlAnalysis = new InputXmlAnalysis();
                }
            }
        }
        return inputXmlAnalysis;
    }

    public List<InputAllBean> GetAllInputList(InputStream inputStream) {
        InputAllBean inputAllBeans = null;
        List<InputAllBean> inputAllBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputAllBeanList = new ArrayList<InputAllBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            inputAllBeans = new InputAllBean();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            inputAllBeans.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            inputAllBeans.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && inputAllBeans != null) {
                            inputAllBeanList.add(inputAllBeans);
                            inputAllBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputAllBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InputLibraryBill> GetInputInfoXml(InputStream inputStream) {
        InputLibraryBill inputInfoBeans = null;
        List<InputLibraryBill> inputInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputInfoList = new ArrayList<InputLibraryBill>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            inputInfoBeans = new InputLibraryBill();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            inputInfoBeans.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FCode")) {
                            inputInfoBeans.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock")) {
                            inputInfoBeans.setFStock(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            inputInfoBeans.setFStock_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            inputInfoBeans.setFTransactionType(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            inputInfoBeans.setFTransactionType_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            inputInfoBeans.setFDate(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && inputInfoBeans != null) {
                            inputInfoList.add(inputInfoBeans);
                            inputInfoBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
