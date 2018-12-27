package cn.shenzhenlizuosystemapp.Common.Xml;

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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitSubmitDataBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.SubBody;
import cn.shenzhenlizuosystemapp.Common.WebBean.QuitLibraryAllInfo;

public class QuitLibraryXmlAnalysis {

    private volatile static QuitLibraryXmlAnalysis quitLibraryXmlAnalysis;

    public static QuitLibraryXmlAnalysis getSingleton() {
        if (quitLibraryXmlAnalysis == null) {
            synchronized (QuitLibraryXmlAnalysis.class) {
                if (quitLibraryXmlAnalysis == null) {
                    quitLibraryXmlAnalysis = new QuitLibraryXmlAnalysis();
                }
            }
        }
        return quitLibraryXmlAnalysis;
    }

    public List<QuitLibraryAllInfo> GetQuitAllInfoList(InputStream inputStream) {
        QuitLibraryAllInfo quitLibraryAllInfos = null;
        List<QuitLibraryAllInfo> quitLibraryAllInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitLibraryAllInfoList = new ArrayList<QuitLibraryAllInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            quitLibraryAllInfos = new QuitLibraryAllInfo();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            quitLibraryAllInfos.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            quitLibraryAllInfos.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && quitLibraryAllInfos != null) {
                            quitLibraryAllInfoList.add(quitLibraryAllInfos);
                            quitLibraryAllInfos = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitLibraryAllInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuitLibraryDetail> GetQuitDetailXml(InputStream inputStream) {
        QuitLibraryDetail QuitDetailXmls = new QuitLibraryDetail();
        List<QuitLibraryDetail> QuitDetailXmlList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        QuitDetailXmlList = new ArrayList<QuitLibraryDetail>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            QuitDetailXmls = new QuitLibraryDetail();
                        } else if (name.equalsIgnoreCase("FCode")) {
                            QuitDetailXmls.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock")) {
                            QuitDetailXmls.setFStock(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            QuitDetailXmls.setFStock_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            QuitDetailXmls.setFTransactionType(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            QuitDetailXmls.setFTransactionType_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FPartner")) {
                            QuitDetailXmls.setFPartner(parser.nextText());
                        } else if (name.equalsIgnoreCase("FPartner_Name")) {
                            QuitDetailXmls.setFPartner_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            QuitDetailXmls.setFGuid(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && QuitDetailXmls != null) {
                            QuitDetailXmlList.add(QuitDetailXmls);
                            QuitDetailXmls = null;
                            ViseLog.i("Head EndTag");
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return QuitDetailXmlList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetQuitDetailXml Exception = " + e);
        }
        return null;
    }

    public List<QuitTaskRvData> GetBodyInfo(InputStream inputStream) {
        QuitTaskRvData quitTaskRvDatas = new QuitTaskRvData();
        List<QuitTaskRvData> quitTaskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        quitTaskRvDataList = new ArrayList<QuitTaskRvData>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            quitTaskRvDatas = new QuitTaskRvData();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFMaterial(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Name")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFMaterial_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Code")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFMaterial_Code(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FModel")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFModel(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFBaseUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFUnit(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit_Name")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FPrice")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFPrice(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FAuxQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedBaseQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFExecutedBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedAuxQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFExecutedAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisBaseQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFThisBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisAuxQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFThisAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FIsClosed")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFIsClosed(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseQty")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnitRate")) {
                            if (quitTaskRvDatas != null) {
                                quitTaskRvDatas.setFUnitRate(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Body")
                                && quitTaskRvDatas != null) {
                            quitTaskRvDataList.add(quitTaskRvDatas);
                            quitTaskRvDatas = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return quitTaskRvDataList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("Body Exception = " + e);
        }
        return null;
    }

    public List<BarCodeHeadBean> GetBarCodeHead(InputStream inputStream) {
        BarCodeHeadBean barCodeHeadBean = new BarCodeHeadBean();
        List<BarCodeHeadBean> barCodeHeadBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        boolean FUnitRate = false;
        boolean FBarcodeType = false;
        boolean FQty = false;
        boolean FGuid = false;
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        barCodeHeadBeanList = new ArrayList<BarCodeHeadBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcode")) {
                            barCodeHeadBean = new BarCodeHeadBean();
                        } else if (name.equalsIgnoreCase("FCode")) {
                            String FCode = parser.nextText();
                            if (FCode.equals("FUnitRate")) {
                                FUnitRate = true;
                            } else if (FCode.equals("FBarcodeType")) {
                                FBarcodeType = true;
                            } else if (FCode.equals("FQty")) {
                                FQty = true;
                            } else if (FCode.equals("FGuid")) {
                                FGuid = true;
                            }
                        } else if (name.equalsIgnoreCase("FContent")) {
                            if (FUnitRate) {
                                barCodeHeadBean.setFUnitRate(parser.nextText());
                                FUnitRate = false;
                            } else if (FBarcodeType) {
                                barCodeHeadBean.setFBarcodeType(parser.nextText());
                                FBarcodeType = false;
                            } else if (FGuid) {
                                barCodeHeadBean.setFGudi(parser.nextText());
                                FGuid = false;
                            } else if (FQty) {
                                barCodeHeadBean.setFQty(parser.nextText());
                                FQty = false;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcode")
                                && barCodeHeadBean != null) {
                            barCodeHeadBeanList.add(barCodeHeadBean);
                            barCodeHeadBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return barCodeHeadBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeHead Exception = " + e);
        }
        return null;
    }


    public List<BarcodeXmlBean> GetBarCodeBody(InputStream inputStream) {
        BarcodeXmlBean barcodeXmlBeans = new BarcodeXmlBean();
        List<BarcodeXmlBean> barcodeXmlBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        barcodeXmlBeanList = new ArrayList<BarcodeXmlBean>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcodes")) {
                            barcodeXmlBeans = new BarcodeXmlBean();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            barcodeXmlBeans.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FBarcodeName")) {
                            barcodeXmlBeans.setFBarcodeName(parser.nextText());
                        } else if (name.equalsIgnoreCase("FBarcodeContent")) {
                            barcodeXmlBeans.setFBarcodeContent(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcodes")
                                && barcodeXmlBeans != null) {
                            barcodeXmlBeanList.add(barcodeXmlBeans);
                            barcodeXmlBeans = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return barcodeXmlBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetBarCodeBody Exception = " + e);
        }
        return null;
    }

    public String CreateQuitXmlStr(String FGuid, String FStockID,String FStockCellID, List<QuitSubBodyBean> quitSubBodyBeanList) {
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

                serializer.startTag(null, "FStockID");
                serializer.text(FStockID);
                serializer.endTag(null, "FStockID");

                serializer.startTag(null, "FStockCellID");
                serializer.text(FStockCellID);
                serializer.endTag(null, "FStockCellID");
                serializer.endTag(null, "BillHead");
                for (QuitSubBodyBean quitSubBodyBean : quitSubBodyBeanList) {
                    serializer.startTag(null, "SubBody");
                    serializer.startTag(null, "FBillBodyID");
                    serializer.text(quitSubBodyBean.getFBillBodyID());
                    serializer.endTag(null, "FBillBodyID");
                    serializer.startTag(null, "FBarcodeLib");
                    serializer.text(quitSubBodyBean.getFBarcodeLib());
                    serializer.endTag(null, "FBarcodeLib");
                    serializer.startTag(null, "FAuxQty");
                    serializer.text(quitSubBodyBean.getInputLibrarySum());
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
