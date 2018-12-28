package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.MaterialModeBean;

public class AnalysisMaterialModeXml {

    private volatile static AnalysisMaterialModeXml analysisMaterialModeXml;

    public static AnalysisMaterialModeXml getSingleton() {
        if (analysisMaterialModeXml == null) {
            synchronized (AnalysisMaterialModeXml.class) {
                if (analysisMaterialModeXml == null) {
                    analysisMaterialModeXml = new AnalysisMaterialModeXml();
                }
            }
        }
        return analysisMaterialModeXml;
    }

    public static List<MaterialModeBean> GetMaterialModeInfo(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            MaterialModeBean materialModeBean = null;
            List<MaterialModeBean> materialModeBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        materialModeBeanList = new ArrayList<MaterialModeBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            materialModeBean = new MaterialModeBean();
                        } else if ("FGuid".equals(parser.getName())) {
                            if (Tools.IsObjectNull(materialModeBean)) {
                                materialModeBean.setFGuid(parser.nextText());
                            }
                        } else if ("FName".equals(parser.getName())) {
                            if (Tools.IsObjectNull(materialModeBean)) {
                                materialModeBean.setFName(parser.nextText());
                            }
                        } else if ("FBarcodeCount".equals(parser.getName())) {
                            if (Tools.IsObjectNull(materialModeBean)) {
                                materialModeBean.setFBarCoeeCount(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table0")
                                && materialModeBean != null) {
                            materialModeBeanList.add(materialModeBean);
                            materialModeBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return materialModeBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
