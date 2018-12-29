package cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotAdapterReturn;

public class DirectAllotAnalysisReturnsXml {
    
    private volatile static DirectAllotAnalysisReturnsXml analysisReturnsXml;

    public static DirectAllotAnalysisReturnsXml getSingleton() {
        if (analysisReturnsXml == null) {
            synchronized (DirectAllotAnalysisReturnsXml.class) {
                if (analysisReturnsXml == null) {
                    analysisReturnsXml = new DirectAllotAnalysisReturnsXml();
                }
            }
        }
        return analysisReturnsXml;
    }
    
    public List<DirectAllotAdapterReturn> GetReturn(InputStream inputStream) {
        DirectAllotAdapterReturn aReturns = null;
        List<DirectAllotAdapterReturn> adapterReturnList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        adapterReturnList = new ArrayList<DirectAllotAdapterReturn>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            aReturns = new DirectAllotAdapterReturn();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            aReturns.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            aReturns.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && aReturns != null) {
                            adapterReturnList.add(aReturns);
                            aReturns = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return adapterReturnList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
