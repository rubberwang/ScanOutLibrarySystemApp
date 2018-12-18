package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.AdapterReturn;

public class AnalysisReturnsXml {
    
    private volatile static AnalysisReturnsXml analysisReturnsXml;

    public static AnalysisReturnsXml getSingleton() {
        if (analysisReturnsXml == null) {
            synchronized (AnalysisReturnsXml.class) {
                if (analysisReturnsXml == null) {
                    analysisReturnsXml = new AnalysisReturnsXml();
                }
            }
        }
        return analysisReturnsXml;
    }
    
    public List<AdapterReturn> GetReturn(InputStream inputStream) {
        AdapterReturn aReturns = null;
        List<AdapterReturn> adapterReturnList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        adapterReturnList = new ArrayList<AdapterReturn>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            aReturns = new AdapterReturn();
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
