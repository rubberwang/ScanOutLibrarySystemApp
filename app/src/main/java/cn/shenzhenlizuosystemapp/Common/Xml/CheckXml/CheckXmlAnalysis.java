package cn.shenzhenlizuosystemapp.Common.Xml.CheckXml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryBill;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckBean.CheckAllBean;

public class CheckXmlAnalysis {
    private volatile static CheckXmlAnalysis checkXmlAnalysis;

    public static CheckXmlAnalysis getSingleton() {
        if (checkXmlAnalysis == null) {
            synchronized (CheckXmlAnalysis.class) {
                if (checkXmlAnalysis == null) {
                    checkXmlAnalysis = new CheckXmlAnalysis();
                }
            }
        }
        return checkXmlAnalysis;
    }

    public List<CheckAllBean> GetAllCheckList(InputStream inputStream) {
        CheckAllBean checkAllBeans = null;
        List<CheckAllBean> checkAllBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        checkAllBeanList = new ArrayList<CheckAllBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            checkAllBeans = new CheckAllBean();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            checkAllBeans.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            checkAllBeans.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && checkAllBeans != null) {
                            checkAllBeanList.add(checkAllBeans);
                            checkAllBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return checkAllBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CheckLibraryBill> GetCheckInfoXml(InputStream inputStream) {
        CheckLibraryBill checkInfoBeans = null;
        List<CheckLibraryBill> checkInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        checkInfoList = new ArrayList<CheckLibraryBill>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            checkInfoBeans = new CheckLibraryBill();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            checkInfoBeans.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FCode")) {
                            checkInfoBeans.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock")) {
                            checkInfoBeans.setFStock(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            checkInfoBeans.setFStock_Name(parser.nextText());
                        }  else if (name.equalsIgnoreCase("FDate")) {
                            checkInfoBeans.setFDate(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && checkInfoBeans != null) {
                            checkInfoList.add(checkInfoBeans);
                            checkInfoBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return checkInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
