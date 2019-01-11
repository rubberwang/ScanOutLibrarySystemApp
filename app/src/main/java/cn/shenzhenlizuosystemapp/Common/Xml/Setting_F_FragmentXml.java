package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.WebBean.QuitAllBean;
import cn.shenzhenlizuosystemapp.Common.WebBean.Setting_PublicDataBean;

public class Setting_F_FragmentXml {

    private volatile static Setting_F_FragmentXml setting_f_fragmentXml;

    public static Setting_F_FragmentXml getSingleton() {
        if (setting_f_fragmentXml == null) {
            synchronized (Setting_F_FragmentXml.class) {
                if (setting_f_fragmentXml == null) {
                    setting_f_fragmentXml = new Setting_F_FragmentXml();
                }
            }
        }
        return setting_f_fragmentXml;
    }

    public List<Setting_PublicDataBean> GetTable0(InputStream inputStream) {
        Setting_PublicDataBean setting_publicDataBean = null;
        List<Setting_PublicDataBean> setting_publicDataBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        setting_publicDataBeanList = new ArrayList<Setting_PublicDataBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            setting_publicDataBean = new Setting_PublicDataBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            setting_publicDataBean.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FName")) {
                            setting_publicDataBean.setFName(parser.nextText());
                        } else if (name.equalsIgnoreCase("FServer")) {
                            setting_publicDataBean.setFServer(parser.nextText());
                        } else if (name.equalsIgnoreCase("FPort")) {
                            setting_publicDataBean.setFPort(parser.nextText());
                        } else if (name.equalsIgnoreCase("FVirtualPath")) {
                            setting_publicDataBean.setFVirtualPath(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table0")
                                && setting_publicDataBean != null) {
                            setting_publicDataBeanList.add(setting_publicDataBean);
                            setting_publicDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return setting_publicDataBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
