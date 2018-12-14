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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.SubBody;

public class GetSnNumberXml {

    private volatile static GetSnNumberXml getChildTag;

    public static GetSnNumberXml getSingleton() {
        if (getChildTag == null) {
            synchronized (GetSnNumberXml.class) {
                if (getChildTag == null) {
                    getChildTag = new GetSnNumberXml();
                }
            }
        }
        return getChildTag;
    }

    public static List<InputSubmitDataBean> ReadPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            InputSubmitDataBean inputSubmitDataBean = null;
            List<InputSubmitDataBean> inputSubmitDataBeanList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputSubmitDataBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            inputSubmitDataBean = new InputSubmitDataBean();
                        } else if ("FQty".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            inputSubmitDataBean.setFQty(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && inputSubmitDataBean != null) {
                            inputSubmitDataBeanList.add(inputSubmitDataBean);
                            inputSubmitDataBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputSubmitDataBeanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<SubBody> ReadSubBodyPullXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            SubBody subBody = null;
            List<SubBody> subBodyList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        subBodyList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("BarcodeLib")) {
                            subBody = new SubBody();
                        } else if ("FGuid".equals(parser.getName())) {
                            String Sum = parser.nextText();
                            subBody.setFBarcodeLib(Sum);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("BarcodeLib")
                                && subBody != null) {
                            subBodyList.add(subBody);
                            subBody = null;
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
    

    public String CreateInputXmlStr(String FGuid, String FPartner, String FTransactionType, List<InputSubmitDataBean> inputSubmitDataBeans, List<SubBody> subBodyList) {
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
                    serializer.startTag(null, "FPrice");
                    serializer.text(inputSubmitDataBeans.get(index).getFPrice());
                    serializer.endTag(null, "FPrice");
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
                // persons标签开始
//                serializer.startTag(null, "NewDataSet");
//                for (int i = 0; i < Guid.size(); i++) {
//                    serializer.startTag(null, "Material");
//                    serializer.startTag(null, "Guid");
//                    serializer.text(Guid.get(i));
//                    serializer.endTag(null, "Guid");
//                    serializer.startTag(null, "BodyID");
//                    serializer.text(BodyID.get(i));
//                    serializer.endTag(null, "BodyID");
//                    serializer.endTag(null, "Material");
//                }
//                for (int index = 0; index < inputSubmitDataBeans.size(); index++) {
//                    serializer.startTag(null, "SerialNum");
//                    serializer.startTag(null, "SN");
//                    serializer.text(inputSubmitDataBeans.get(index).getSn());
//                    serializer.endTag(null, "SN");
//                    serializer.startTag(null, "MaterialId");
//                    serializer.text(Guid.get(index));
//                    serializer.endTag(null, "MaterialId");
//                    serializer.startTag(null, "Qty");
//                    serializer.text(inputSubmitDataBeans.get(index).getNumber());
//                    serializer.endTag(null, "Qty");
//                    serializer.endTag(null, "SerialNum");
//                }
//                serializer.endTag(null, "NewDataSet");
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