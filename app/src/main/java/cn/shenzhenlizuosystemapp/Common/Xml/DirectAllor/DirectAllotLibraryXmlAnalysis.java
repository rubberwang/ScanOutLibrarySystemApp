package cn.shenzhenlizuosystemapp.Common.Xml.DirectAllor;

import android.os.Environment;
import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CreateAdjustStockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotLibraryBill;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailBodyXmlPort;
import cn.shenzhenlizuosystemapp.Common.Port.DirectPortDetailHeadXmlPort;

public class DirectAllotLibraryXmlAnalysis {
    private volatile static DirectAllotLibraryXmlAnalysis directAllotLibraryXmlAnalysis;

    public static DirectAllotLibraryXmlAnalysis getSingleton() {
        if (directAllotLibraryXmlAnalysis == null) {
            synchronized (DirectAllotLibraryXmlAnalysis.class) {
                if (directAllotLibraryXmlAnalysis == null) {
                    directAllotLibraryXmlAnalysis = new DirectAllotLibraryXmlAnalysis();
                }
            }
        }
        return directAllotLibraryXmlAnalysis;
    }

    public List<DirectAllotLibraryBill> GetDirectAllotNotification(InputStream inputStream) {
        DirectAllotLibraryBill directAllotNotificationBeans = new DirectAllotLibraryBill();
        List<DirectAllotLibraryBill> directAllotLibraryBillList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotLibraryBillList = new ArrayList<DirectAllotLibraryBill>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            directAllotNotificationBeans = new DirectAllotLibraryBill();
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
                            directAllotLibraryBillList.add(directAllotNotificationBeans);
                            directAllotNotificationBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return directAllotLibraryBillList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
        return null;
    }

    public void GetDirectAllotDetailHeadInfo(InputStream inputStream, DirectPortDetailHeadXmlPort directPortDetailXmlPort) {
        DirectAllotDetail directAllotDetail = new DirectAllotDetail();
        List<DirectAllotDetail> directAllotDetailList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotDetailList = new ArrayList<DirectAllotDetail>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            directAllotDetail = new DirectAllotDetail();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FCode")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFCode(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FDate")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFDate(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFOutStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FOutStock_Name")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFOutStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFInStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FInStock_Name")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFInStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFTransactionType(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            if (Tools.IsObjectNull(directAllotDetail)) {
                                directAllotDetail.setFTransactionType_Name(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && (directAllotDetail != null)) {
                            directAllotDetailList.add(directAllotDetail);
                            directAllotDetail = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            directPortDetailXmlPort.OnHead(directAllotDetailList);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
    }

    public void GetDirectAllotDetailBodyInfo(InputStream inputStream, DirectPortDetailBodyXmlPort directPortDetailBodyXmlPort) {
        DirectAllotTaskRvData directAllotTaskRvData = new DirectAllotTaskRvData();
        List<DirectAllotTaskRvData> directAllotTaskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        directAllotTaskRvDataList = new ArrayList<DirectAllotTaskRvData>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            directAllotTaskRvData = new DirectAllotTaskRvData();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFMaterial(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Code")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFMaterial_Code(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Name")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFMaterial_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FModel")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFModel(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFBaseUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFUnit(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit_Name")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnitRate")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFUnitRate(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FPrice")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFPrice(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FAuxQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedBaseQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFExecutedBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedAuxQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFExecutedAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FIsClosed")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFIsClosed(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisBaseQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFThisBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisAuxQty")) {
                            if (Tools.IsObjectNull(directAllotTaskRvData)) {
                                directAllotTaskRvData.setFThisAuxQty(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Body")
                                && (directAllotTaskRvData != null)) {
                            directAllotTaskRvDataList.add(directAllotTaskRvData);
                            directAllotTaskRvData = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            directPortDetailBodyXmlPort.OnBody(directAllotTaskRvDataList);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
    }

    public static String CreateAdjustStockBillXmlStr(String FGuid, String FTransactionTypeID, String FOutStockID,String FOutStockCellID, String FInStockID, String FInStockCellID, List<CreateAdjustStockBean> createAdjustStockBeanList) {
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
                serializer.startTag(null, "FTransactionTypeID");
                serializer.text(FTransactionTypeID);
                serializer.endTag(null, "FTransactionTypeID");
                serializer.startTag(null, "FOutStockID");
                serializer.text(FOutStockID);
                serializer.endTag(null, "FOutStockID");
                serializer.startTag(null, "FOutStockCellID");
                serializer.text(FOutStockCellID);
                serializer.endTag(null, "FOutStockCellID");
                serializer.startTag(null, "FInStockID");
                serializer.text(FInStockID);
                serializer.endTag(null, "FInStockID");
                serializer.startTag(null, "FInStockCellID");
                serializer.text(FInStockCellID);
                serializer.endTag(null, "FInStockCellID");
                serializer.endTag(null, "BillHead");
                for (CreateAdjustStockBean adjustStockBean : createAdjustStockBeanList) {
                    serializer.startTag(null, "SubBody");
                    serializer.startTag(null, "FBillBodyID");
                    serializer.text(adjustStockBean.getFBillBodyID());
                    serializer.endTag(null, "FBillBodyID");
                    serializer.startTag(null, "FBarcodeLib");
                    serializer.text(adjustStockBean.getFBarcodeLib());
                    serializer.endTag(null, "FBarcodeLib");
                    serializer.startTag(null, "FAuxQty");
                    serializer.text(adjustStockBean.getFAuxQty());
                    serializer.endTag(null, "FAuxQty");
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
