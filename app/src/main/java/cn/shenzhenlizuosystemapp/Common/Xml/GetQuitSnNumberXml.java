package cn.shenzhenlizuosystemapp.Common.Xml;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.SubQuitBody;

public class GetQuitSnNumberXml {

    private volatile static GetQuitSnNumberXml getChildTag;

    public static GetQuitSnNumberXml getSingleton() {
        if (getChildTag == null) {
            synchronized (GetQuitSnNumberXml.class) {
                if (getChildTag == null) {
                    getChildTag = new GetQuitSnNumberXml();
                }
            }
        }
        return getChildTag;
    }

    public static List<QuitSubmitDataBean> ReadPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            QuitSubmitDataBean quitSubmitDataBean = null;
            List<QuitSubmitDataBean> quitSubmitDataBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitSubmitDataBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            quitSubmitDataBean = new QuitSubmitDataBean();
                        } else if ("FQty".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            quitSubmitDataBean.setFQty(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && quitSubmitDataBean != null) {
                            quitSubmitDataBeanList.add(quitSubmitDataBean);
                            quitSubmitDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitSubmitDataBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<SubQuitBody> ReadSubBodyPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            SubQuitBody subQuitBody = null;
            List<SubQuitBody> subBodyList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        subBodyList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            subQuitBody = new SubQuitBody();
                        } else if ("FGuid".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            subQuitBody.setFBarcodeLib(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && subQuitBody != null) {
                            subBodyList.add(subQuitBody);
                            subQuitBody = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return subBodyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String CreateInputXmlStr(String FGuid, String FPartner, String FTransactionType, List<QuitSubmitDataBean> inputSubmitDataBeans, List<SubQuitBody> subBodyList) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            StringWriter stringWriter = new StringWriter();
            XmlSerializer serializer = Xml.newSerializer();
            try {
                serializer.setOutput(stringWriter);
                serializer.startDocument("UTF-8", true);
                serializer.startTag(null, "NewDataSet");

                serializer.startTag(null, "BillHead");
                serializer.startTag(null, "FGuid");
                serializer.text(FGuid);
                serializer.endTag(null, "FGuid");
                serializer.startTag(null, "FPartner");
                serializer.text(FPartner);
                serializer.endTag(null, "FPartner");
                serializer.startTag(null, "FTransactionType");
                serializer.text(FTransactionType);
                serializer.endTag(null, "FTransactionType");
                serializer.endTag(null, "BillHead");

                for (int index = 0; index < inputSubmitDataBeans.size(); index++) {
                    serializer.startTag(null, "BillBody");
                    serializer.startTag(null, "FGuid");
                    serializer.text(inputSubmitDataBeans.get(index).getFGuid());
                    serializer.endTag(null, "FGuid");
                    serializer.startTag(null, "FBillID");
                    serializer.text(inputSubmitDataBeans.get(index).getFBillID());
                    serializer.endTag(null, "FBillID");
                    serializer.startTag(null, "FMaterial");
                    serializer.text(inputSubmitDataBeans.get(index).getFMaterial());
                    serializer.endTag(null, "FMaterial");
                    serializer.startTag(null, "FUnit");
                    serializer.text(inputSubmitDataBeans.get(index).getFUnit());
                    serializer.endTag(null, "FUnit");
                    serializer.startTag(null, "FQty");
                    serializer.text(inputSubmitDataBeans.get(index).getFQty());
                    serializer.endTag(null, "FQty");
                    serializer.endTag(null, "BillBody");
                }

                for (int s = 0; s < subBodyList.size(); s++) {
                    serializer.startTag(null, "SubBody");
                    serializer.startTag(null, "FGuid");
                    serializer.text(subBodyList.get(s).getFGuid());
                    serializer.endTag(null, "FGuid");
                    serializer.startTag(null, "FBillBodyID");
                    serializer.text(subBodyList.get(s).getFBillBodyID());
                    serializer.endTag(null, "FBillBodyID");
                    serializer.startTag(null, "FBarcodeLib");
                    serializer.text(subBodyList.get(s).getFBarcodeLib());
                    serializer.endTag(null, "FBarcodeLib");
                    serializer.endTag(null, "SubBody");
                }
                serializer.endTag(null, "NewDataSet");

                serializer.endDocument();
                stringWriter.close();
                return String.valueOf(stringWriter);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    stringWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return "";
    }
}