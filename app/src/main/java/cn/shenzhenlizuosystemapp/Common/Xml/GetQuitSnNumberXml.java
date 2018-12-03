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
                        } else if ("FGuid".equals(parser.getName())) {
                            String Guid = parser.nextText();
                            quitSubmitDataBean.setSn(Guid);
                        } else if ("FQty".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            quitSubmitDataBean.setNumber(Sum);
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

    public String CreateInputXmlStr(List<QuitSubmitDataBean> inputSubmitDataBeans, List<String> Guid, List<String> BodyID) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            StringWriter stringWriter = new StringWriter();
            XmlSerializer serializer = Xml.newSerializer();
            try {
                serializer.setOutput(stringWriter);
                serializer.startDocument("UTF-8", true);
                // persons标签开始
                serializer.startTag(null, "NewDataSet");
                for (int i = 0; i < Guid.size(); i++) {
                    serializer.startTag(null, "Material");
                    serializer.startTag(null, "Guid");
                    serializer.text(Guid.get(i));
                    serializer.endTag(null, "Guid");
                    serializer.startTag(null, "BodyID");
                    serializer.text(BodyID.get(i));
                    serializer.endTag(null, "BodyID");
                    serializer.endTag(null, "Material");
                }
                for (int index = 0; index < inputSubmitDataBeans.size(); index++) {
                    serializer.startTag(null, "SerialNum");
                    serializer.startTag(null, "SN");
                    serializer.text(inputSubmitDataBeans.get(index).getSn());
                    serializer.endTag(null, "SN");
                    serializer.startTag(null, "MaterialId");
                    serializer.text(Guid.get(index));
                    serializer.endTag(null, "MaterialId");
                    serializer.startTag(null, "Qty");
                    serializer.text(inputSubmitDataBeans.get(index).getNumber());
                    serializer.endTag(null, "Qty");
                    serializer.endTag(null, "SerialNum");
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