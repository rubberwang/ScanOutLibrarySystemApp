package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStock_Return;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.RFIDInfo;

public class RFIDXmlAnalysis {

    private volatile static RFIDXmlAnalysis rfidXmlAnalysis;

    public static RFIDXmlAnalysis getSingleton(){
        if (rfidXmlAnalysis == null){
            synchronized (RFIDXmlAnalysis.class){
                if (rfidXmlAnalysis == null){
                    rfidXmlAnalysis = new RFIDXmlAnalysis();
                }
            }
        }
        return rfidXmlAnalysis;
    }

    public List<CheckStock_Return>GetRFIDXml(InputStream inputStream){
        CheckStock_Return checkStock_return = null;
        List<CheckStock_Return> checkStock_returns = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        checkStock_returns = new ArrayList<CheckStock_Return>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            checkStock_return = new CheckStock_Return();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            checkStock_return.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            checkStock_return.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && checkStock_return != null) {
                            checkStock_returns.add(checkStock_return);
                            checkStock_return = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return checkStock_returns;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RFIDInfo> GetRFIDInfo(InputStream inputStream) {
        RFIDInfo stockBeans = null;
        List<RFIDInfo> stockBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        stockBeanList = new ArrayList<RFIDInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            stockBeans = new RFIDInfo();
                        } else if (name.equalsIgnoreCase("FValue")) {
                            stockBeans.setFValue(parser.nextText());
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
