package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryBill;
import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;
import cn.shenzhenlizuosystemapp.Common.WebBean.QuitAllBean;

public class QuitXmlAnalysis {
    private volatile static QuitXmlAnalysis quitXmlAnalysis;

    public static QuitXmlAnalysis getSingleton() {
        if (quitXmlAnalysis == null) {
            synchronized (QuitXmlAnalysis.class) {
                if (quitXmlAnalysis == null) {
                    quitXmlAnalysis = new QuitXmlAnalysis();
                }
            }
        }
        return quitXmlAnalysis;
    }

    public List<QuitAllBean> GetAllQuitList(InputStream inputStream) {
        QuitAllBean quitAllBeans = null;
        List<QuitAllBean> quitAllBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitAllBeanList = new ArrayList<QuitAllBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            quitAllBeans = new QuitAllBean();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            quitAllBeans.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            quitAllBeans.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && quitAllBeans != null) {
                            quitAllBeanList.add(quitAllBeans);
                            quitAllBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitAllBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuitLibraryBill> GetQuitInfoXml(InputStream inputStream) {
        QuitLibraryBill quitInfoBeans = null;
        List<QuitLibraryBill> quitInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitInfoList = new ArrayList<QuitLibraryBill>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            quitInfoBeans = new QuitLibraryBill();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            quitInfoBeans.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FCode")) {
                            quitInfoBeans.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock")) {
                            quitInfoBeans.setFStock(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            quitInfoBeans.setFStock_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            quitInfoBeans.setFTransactionType(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            quitInfoBeans.setFTransactionType_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            quitInfoBeans.setFDate(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && quitInfoBeans != null) {
                            quitInfoList.add(quitInfoBeans);
                            quitInfoBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
