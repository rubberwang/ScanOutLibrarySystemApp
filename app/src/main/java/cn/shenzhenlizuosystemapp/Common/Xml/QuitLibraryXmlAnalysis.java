package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;
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
}
