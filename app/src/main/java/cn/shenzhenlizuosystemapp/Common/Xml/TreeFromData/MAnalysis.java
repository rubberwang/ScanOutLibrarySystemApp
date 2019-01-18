package cn.shenzhenlizuosystemapp.Common.Xml.TreeFromData;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.Tree.TreeMBean;

public class MAnalysis {
    private volatile static MAnalysis mAnalysis;

    public static MAnalysis getSingleton() {
        if (mAnalysis == null) {
            synchronized (MAnalysis.class) {
                if (mAnalysis == null) {
                    mAnalysis = new MAnalysis();
                }
            }
        }
        return mAnalysis;
    }

    public List<TreeMBean> GetMXml(InputStream inputStream) {
        TreeMBean treeMBean = null;
        List<TreeMBean> treeMBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        treeMBeanList = new ArrayList<TreeMBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            treeMBean = new TreeMBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            treeMBean.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FCode")) {
                            treeMBean.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FName")) {
                            treeMBean.setFName(parser.nextText());
                        } else if (name.equalsIgnoreCase("FModel")) {
                            treeMBean.setFModel(parser.nextText());
                        } else if (name.equalsIgnoreCase("FBaseUnit")) {
                            treeMBean.setFBaseUnit(parser.nextText());
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            treeMBean.setFBaseUnit_Name(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table0")
                                && treeMBean != null) {
                            treeMBeanList.add(treeMBean);
                            treeMBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return treeMBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
