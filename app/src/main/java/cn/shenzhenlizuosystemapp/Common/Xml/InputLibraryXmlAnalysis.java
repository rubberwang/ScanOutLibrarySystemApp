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
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.Common.WebBean.InputLibraryAllInfo;

public class InputLibraryXmlAnalysis {

    private volatile static InputLibraryXmlAnalysis inputLibraryXmlAnalysis;

    public static InputLibraryXmlAnalysis getSingleton() {
        if (inputLibraryXmlAnalysis == null) {
            synchronized (InputLibraryXmlAnalysis.class) {
                if (inputLibraryXmlAnalysis == null) {
                    inputLibraryXmlAnalysis = new InputLibraryXmlAnalysis();
                }
            }
        }
        return inputLibraryXmlAnalysis;
    }

    public List<InputLibraryAllInfo> GetInputAllInfoList(InputStream inputStream) {
        InputLibraryAllInfo inputLibraryAllInfos = null;
        List<InputLibraryAllInfo> inputLibraryAllInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputLibraryAllInfoList = new ArrayList<InputLibraryAllInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            inputLibraryAllInfos = new InputLibraryAllInfo();
                        } else if (name.equalsIgnoreCase("FStatus")) {
                            inputLibraryAllInfos.setFStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FInfo")) {
                            inputLibraryAllInfos.setFInfo(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("T_Return")
                                && inputLibraryAllInfos != null) {
                            inputLibraryAllInfoList.add(inputLibraryAllInfos);
                            inputLibraryAllInfos = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputLibraryAllInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InputLibraryDetail> GetInputDetailXml(InputStream inputStream) {
        InputLibraryDetail InputDetailXmls = new InputLibraryDetail();
        List<InputLibraryDetail> InputDetailXmlList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        InputDetailXmlList = new ArrayList<InputLibraryDetail>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            InputDetailXmls = new InputLibraryDetail();
                        } else if (name.equalsIgnoreCase("FCode")) {
                            InputDetailXmls.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock")) {
                            InputDetailXmls.setFStock(parser.nextText());
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            InputDetailXmls.setFStock_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType")) {
                            InputDetailXmls.setFTransactionType(parser.nextText());
                        } else if (name.equalsIgnoreCase("FTransactionType_Name")) {
                            InputDetailXmls.setFTransactionType_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FPartner")) {
                            InputDetailXmls.setFPartner(parser.nextText());
                        } else if (name.equalsIgnoreCase("FPartner_Name")) {
                            InputDetailXmls.setFPartner_Name(parser.nextText());
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            InputDetailXmls.setFGuid(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Head")
                                && InputDetailXmls != null) {
                            InputDetailXmlList.add(InputDetailXmls);
                            InputDetailXmls = null;
                            ViseLog.i("Head EndTag");
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return InputDetailXmlList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("GetInputDetailXml Exception = " + e);
        }
        return null;
    }

    public List<InputTaskRvData> GetBodyInfo(InputStream inputStream) {
        InputTaskRvData inputTaskRvDatas = new InputTaskRvData();
        List<InputTaskRvData> inputTaskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputTaskRvDataList = new ArrayList<InputTaskRvData>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            inputTaskRvDatas = new InputTaskRvData();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFMaterial(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Name")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFMaterial_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Code")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFMaterial_Code(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FModel")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFModel(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFBaseUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFUnit(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit_Name")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FPrice")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFPrice(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FAuxQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedBaseQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFExecutedBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedAuxQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFExecutedAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisBaseQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFThisBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisAuxQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFThisAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FIsClosed")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFIsClosed(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnitRate")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFUnitRate(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Body")
                                && inputTaskRvDatas != null) {
                            inputTaskRvDataList.add(inputTaskRvDatas);
                            inputTaskRvDatas = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return inputTaskRvDataList;
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

    public String   CreateInputXmlStr(String FGuid, String FStockID,String FStockCellID, List<InputSubBodyBean> inputSubBodyBeanList) {
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
                for (InputSubBodyBean inputSubBodyBean : inputSubBodyBeanList) {
                    serializer.startTag(null, "SubBody");
                    serializer.startTag(null, "FBillBodyID");
                    serializer.text(inputSubBodyBean.getFBillBodyID());
                    serializer.endTag(null, "FBillBodyID");
                    serializer.startTag(null, "FBarcodeLib");
                    serializer.text(inputSubBodyBean.getFBarcodeLib());
                    serializer.endTag(null, "FBarcodeLib");
                    serializer.startTag(null, "FAuxQty");
                    serializer.text(inputSubBodyBean.getInputLibrarySum());
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
