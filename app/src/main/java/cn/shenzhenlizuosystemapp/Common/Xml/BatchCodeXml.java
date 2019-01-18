package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.QuitSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.Xml.InputXml.GetInputChildTag;

public class BatchCodeXml {
    private volatile static BatchCodeXml batchCodeXml;

    public static BatchCodeXml getSingleton() {
        if (batchCodeXml == null) {
            synchronized (GetInputChildTag.class) {
                if (batchCodeXml == null) {
                    batchCodeXml = new BatchCodeXml();
                }
            }
        }
        return batchCodeXml;
    }

    public static String GETBatchCode(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            boolean is = false;
            QuitSubmitDataBean quitSubmitDataBean = null;
            List<QuitSubmitDataBean> quitSubmitDataBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitSubmitDataBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcode")) {
                            quitSubmitDataBean = new QuitSubmitDataBean();
                        } else if ("FCode".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            if (Sum.equals("FBatchNumber")) {
                                is = true;
                            }
                        } else if ("FContent".equals(parser.getName())) {
                            if (is){
                                String Sum = parser.nextText();
                                return Sum;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcode")
                                && quitSubmitDataBean != null) {
                            quitSubmitDataBeanList.add(quitSubmitDataBean);
                            quitSubmitDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
