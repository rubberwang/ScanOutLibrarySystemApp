package cn.shenzhenlizuosystemapp.Common.Xml;

import android.util.Xml;

import com.vise.log.ViseLog;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TaskRvData;
import cn.shenzhenlizuosystemapp.Common.WebBean.GetProjectResult;
import cn.shenzhenlizuosystemapp.Common.WebBean.InputAllBean;
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

    public List<QuitLibraryDetail> GetInputDetailXml(InputStream inputStream) {
        QuitLibraryDetail InputDetailXmls = new QuitLibraryDetail();
        List<QuitLibraryDetail> InputDetailXmlList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        InputDetailXmlList = new ArrayList<QuitLibraryDetail>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Head")) {
                            InputDetailXmls = new QuitLibraryDetail();
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

    public List<TaskRvData> GetBodyInfo(InputStream inputStream) {
        TaskRvData taskRvDatas = new TaskRvData();
        List<TaskRvData> taskRvDataList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        taskRvDataList = new ArrayList<TaskRvData>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Body")) {
                            taskRvDatas = new TaskRvData();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFGuid(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFMaterial(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Name")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFMaterial_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FMaterial_Code")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFMaterial_Code(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FModel")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFModel(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FBaseUnit_Name")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFBaseUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFUnit(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FUnit_Name")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFUnit_Name(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FPrice")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFPrice(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FAuxQty")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedBaseQty")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFExecutedBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FExecutedAuxQty")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFExecutedAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisBaseQty")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFThisBaseQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FThisAuxQty")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFThisAuxQty(parser.nextText());
                            }
                        } else if (name.equalsIgnoreCase("FIsClosed")) {
                            if (taskRvDatas != null) {
                                taskRvDatas.setFIsClosed(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Body")
                                && taskRvDatas != null) {
                            taskRvDataList.add(taskRvDatas);
                            taskRvDatas = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return taskRvDataList;
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.i("Body Exception = " + e);
        }
        return null;
    }
}
