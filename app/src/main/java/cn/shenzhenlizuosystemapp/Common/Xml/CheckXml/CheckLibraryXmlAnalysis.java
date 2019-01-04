package cn.shenzhenlizuosystemapp.Common.Xml.CheckXml;

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
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarCodeHeadBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.BarcodeXmlBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBodyBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckLibraryAllInfo;

public class CheckLibraryXmlAnalysis {

    private volatile static CheckLibraryXmlAnalysis inputLibraryXmlAnalysis;

    public static CheckLibraryXmlAnalysis getSingleton() {
        if (inputLibraryXmlAnalysis == null) {
            synchronized (CheckLibraryXmlAnalysis.class) {
                if (inputLibraryXmlAnalysis == null) {
                    inputLibraryXmlAnalysis = new CheckLibraryXmlAnalysis();
                }
            }
        }
        return inputLibraryXmlAnalysis;
    }

    public List<CheckLibraryAllInfo> GetCheckAllInfoList(InputStream inputStream) {
        CheckLibraryAllInfo inputLibraryAllInfos = null;
        List<CheckLibraryAllInfo> inputLibraryAllInfoList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputLibraryAllInfoList = new ArrayList<CheckLibraryAllInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("T_Return")) {
                            inputLibraryAllInfos = new CheckLibraryAllInfo();
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

    public List<CheckLibraryDetail> GetCheckDetailXml(InputStream inputStream) {
        CheckLibraryDetail InputDetailXmls = new CheckLibraryDetail();
        List<CheckLibraryDetail> InputDetailXmlList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        InputDetailXmlList = new ArrayList<CheckLibraryDetail>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            InputDetailXmls = new CheckLibraryDetail();
                        } else if (name.equalsIgnoreCase("FCode")) {
                            if (Tools.IsObjectNull(InputDetailXmls)){
                                InputDetailXmls.setFCode(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FStock")) {
                            if (Tools.IsObjectNull(InputDetailXmls)) {
                                InputDetailXmls.setFStock(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FStock_Name")) {
                            if (Tools.IsObjectNull(InputDetailXmls)) {
                                InputDetailXmls.setFStock_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (Tools.IsObjectNull(InputDetailXmls)) {
                                InputDetailXmls.setFGuid(parser.nextText());
                            }
                        }else if (name.equalsIgnoreCase("FAllowOtherMaterial")) {
                            if (Tools.IsObjectNull(InputDetailXmls)) {
                                InputDetailXmls.setFAllowOtherMaterial(parser.nextText());
                            }
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
            ViseLog.i("GetCheckDetailXml Exception = " + e);
        }
        return null;
    }

    public List<CheckTaskRvData> GetBodyInfo(InputStream inputStream) {
        CheckTaskRvData inputTaskRvDatas = new CheckTaskRvData();
        List<CheckTaskRvData> inputTaskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputTaskRvDataList = new ArrayList<CheckTaskRvData>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            inputTaskRvDatas = new CheckTaskRvData();
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
                        }  else if (name.equalsIgnoreCase("FAccountQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFAccountQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FCheckQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFCheckQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FDiffQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFDiffQty(parser.nextText());
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

    public List<CheckSubBody> GetSubBodyInfo(InputStream inputStream) {
        CheckSubBody inputTaskRvDatas = new CheckSubBody();
        List<CheckSubBody> inputTaskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        inputTaskRvDataList = new ArrayList<CheckSubBody>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("SubBody")) {
                            inputTaskRvDatas = new CheckSubBody();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBarcodeLib")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFBarcodeLib(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBarcodeLib_Name")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFBarcodeLib_Name(parser.nextText());
                            }
                        }  else if (name.equalsIgnoreCase("FCheckStockStatus")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFCheckStockStatus(parser.nextText());
                            }
                        }  else if (name.equalsIgnoreCase("FAccountQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFAccountQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FCheckQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFCheckQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FDiffQty")) {
                            if (inputTaskRvDatas != null) {
                                inputTaskRvDatas.setFDiffQty(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("SubBody")
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
            ViseLog.i("SubBody Exception = " + e);
        }
        return null;
    }

    /*public List<BarCodeHeadBean> GetBarCodeHead(InputStream inputStream) {
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
    }*/

    public String   CreateCheckXmlStr(String FGuid, String FStockID,String FStockCellID, List<CheckSubBodyBean> inputSubBodyBeanList) {
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
                for (CheckSubBodyBean inputSubBodyBean : inputSubBodyBeanList) {
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
