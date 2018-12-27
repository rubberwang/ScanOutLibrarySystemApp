package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitStock_Return;

public class QuitStockXmlAnalysis {

    private volatile static QuitStockXmlAnalysis quitStockXmlAnalysis;

    public static QuitStockXmlAnalysis getSingleton() {
        if (quitStockXmlAnalysis == null) {
            synchronized (QuitStockXmlAnalysis.class) {
                if (quitStockXmlAnalysis == null) {
                    quitStockXmlAnalysis = new QuitStockXmlAnalysis();
                }
            }
        }
        return quitStockXmlAnalysis;
    }

    public List<QuitStock_Return> GetXmlStockReturn(InputStream inputStream) {
        QuitStock_Return stock_return = null;
        List<QuitStock_Return> stock_returnsList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        stock_returnsList = new ArrayList<QuitStock_Return>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            stock_return = new QuitStock_Return();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            stock_return.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            stock_return.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && stock_return != null) {
                            stock_returnsList.add(stock_return);
                            stock_return = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return stock_returnsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuitStockBean> GetXmlStockInfo(InputStream inputStream) {
        QuitStockBean stockBeans = null;
        List<QuitStockBean> stockBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        stockBeanList = new ArrayList<QuitStockBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            stockBeans = new QuitStockBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            stockBeans.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FName")) {
                            stockBeans.setFName(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table0")
                                && stockBeans != null) {
                            stockBeanList.add(stockBeans);
                            stockBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return stockBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetXmlStockInfo = " + e);
        }
        return null;
    }

}
