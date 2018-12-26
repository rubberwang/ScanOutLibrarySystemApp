package cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotNotificationBean;
import cn.shenzhenlizuosystemapp.Common.Xml.AnalysisMaterialModeXml;

public class AnalyAllXml {
    private volatile static AnalyAllXml analyAllXml;

    public static AnalyAllXml getSingleton() {
        if (analyAllXml == null) {
            synchronized (AnalyAllXml.class) {
                if (analyAllXml == null) {
                    analyAllXml = new AnalyAllXml();
                }
            }
        }
        return analyAllXml;
    }

    public List<DirectAllotNotificationBean> GetBarCodeBody(InputStream inputStream) {
        DirectAllotNotificationBean directAllotNotificationBeans = new DirectAllotNotificationBean();
        List<DirectAllotNotificationBean> directAllotNotificationBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotNotificationBeanList = new ArrayList<DirectAllotNotificationBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcodes")) {
                            directAllotNotificationBeans = new DirectAllotNotificationBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)){
                                directAllotNotificationBeans.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBarcodeName")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)){
                                directAllotNotificationBeans.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBarcodeContent")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)){
                                directAllotNotificationBeans.setFGuid(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcodes")
                                && directAllotNotificationBeans != null) {
                            directAllotNotificationBeanList.add(directAllotNotificationBeans);
                            directAllotNotificationBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return directAllotNotificationBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
        return null;
    }
    
}
