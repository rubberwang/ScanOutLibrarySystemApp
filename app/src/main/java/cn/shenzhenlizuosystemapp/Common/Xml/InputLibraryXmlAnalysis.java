package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputLibraryDetail;
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
}
