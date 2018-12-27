package cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor;

import android.util.Xml;
import com.vise.log.ViseLog;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotNotificationBean;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;

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

    public List<DirectAllotNotificationBean> GetDirectAllotNotification(InputStream inputStream) {
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
                        if (name.equalsIgnoreCase("Head")) {
                            directAllotNotificationBeans = new DirectAllotNotificationBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FCode")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFCode(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FDate")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFDate(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFOutStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock_Name")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFOutStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFInStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock_Name")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFInStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFTransactionType(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            if (Tools.IsObjectNull(directAllotNotificationBeans)) {
                                directAllotNotificationBeans.setFTransactionType_Name(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
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

    public void GetDirectAllotDetailHeadInfo(InputStream inputStream, DirectPortDetailHeadXmlPort directPortDetailXmlPort) {
        DirectAllotHeadBean directAllotHeadBean = new DirectAllotHeadBean();
        List<DirectAllotHeadBean> directAllotHeadBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotHeadBeanList = new ArrayList<DirectAllotHeadBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            directAllotHeadBean = new DirectAllotHeadBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FCode")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFCode(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FDate")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFDate(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFOutStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock_Name")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFOutStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFInStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock_Name")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFInStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFTransactionType(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            if (Tools.IsObjectNull(directAllotHeadBean)) {
                                directAllotHeadBean.setFTransactionType_Name(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && (directAllotHeadBean != null)) {
                                directAllotHeadBeanList.add(directAllotHeadBean);
                                directAllotHeadBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            directPortDetailXmlPort.OnHead(directAllotHeadBeanList);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
    }

    public void GetDirectAllotDetailBodyInfo(InputStream inputStream, DirectPortDetailBodyXmlPort directPortDetailBodyXmlPort) {
        DirectAllotBodyBean directAllotBodyBean = new DirectAllotBodyBean();
        List<DirectAllotBodyBean> directAllotBodyBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotBodyBeanList = new ArrayList<DirectAllotBodyBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            directAllotBodyBean = new DirectAllotBodyBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFMaterial(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Code")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFMaterial_Code(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Name")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFMaterial_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FModel")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFModel(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFBaseUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFUnit(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit_Name")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnitRate")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFUnitRate(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FPrice")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFPrice(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FAuxQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFAuxQty(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FBaseQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFBaseQty(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FExecutedBaseQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFExecutedBaseQty(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FExecutedAuxQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFExecutedAuxQty(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FIsClosed")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFIsClosed(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FThisBaseQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFThisBaseQty(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FThisAuxQty")) {
                            if (Tools.IsObjectNull(directAllotBodyBean)) {
                                directAllotBodyBean.setFThisAuxQty(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Body")
                                && (directAllotBodyBean != null)) {
                            directAllotBodyBeanList.add(directAllotBodyBean);
                            directAllotBodyBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            directPortDetailBodyXmlPort.OnBody(directAllotBodyBeanList);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
    }
}
