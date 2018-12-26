package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Stock_Return;

public class InputStockXmlAnalysis {

    private volatile static InputStockXmlAnalysis inputStockXmlAnalysis;

    public static InputStockXmlAnalysis getSingleton() {
        if (inputStockXmlAnalysis == null) {
            synchronized (InputStockXmlAnalysis.class) {
                if (inputStockXmlAnalysis == null) {
                    inputStockXmlAnalysis = new InputStockXmlAnalysis();
                }
            }
        }
        return inputStockXmlAnalysis;
    }

    public List<Stock_Return> GetXmlStockReturn(InputStream inputStream) {
        Stock_Return stock_return = null;
        List<Stock_Return> stock_returnsList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        stock_returnsList = new ArrayList<Stock_Return>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            stock_return = new Stock_Return();
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

    public List<StockBean> GetXmlStockInfo(InputStream inputStream) {
        StockBean stockBeans = null;
        List<StockBean> stockBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        stockBeanList = new ArrayList<StockBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            stockBeans = new StockBean();
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
