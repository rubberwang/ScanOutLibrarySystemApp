package cn.shenzhenlizuosystemapp.Common.Xml;

import com.vise.log.ViseLog;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;

public class QuitTaskXml {

    private volatile static QuitTaskXml QuitTaskXml;

    public static QuitTaskXml getSingleton() {
        if (QuitTaskXml == null) {
            synchronized (QuitTaskXml.class) {
                if (QuitTaskXml == null) {
                    QuitTaskXml = new QuitTaskXml();
                }
            }
        }
        return QuitTaskXml;
    }
    
    public List GetInputBodyXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
       BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<QuitTaskRvData> quitBodys = handler.getBody();
        return quitBodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<QuitTaskRvData> QuitBodys;
        private QuitTaskRvData quitBody;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<QuitTaskRvData> getBody() {
            if (QuitBodys != null) {
                return QuitBodys;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            QuitBodys = new ArrayList<QuitTaskRvData>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table") || localName.equals("Table1")) {
                quitBody = new QuitTaskRvData();
                ViseLog.i("创建quitBody");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table") || localName.equals("Table1")) {
                QuitBodys.add(quitBody);
                quitBody = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("FGuid")) {
                    quitBody.setFGUID(data);
                    ViseLog.i(data);
                } else if (tag.equals("FMaterial_Code")) {//物料编号
                    quitBody.setTV_materID(data);
                } else if (tag.equals("FAuxQty")) {//应发
                    quitBody.setTV_shouldSend(data);
                } else if (tag.equals("FExecutedAuxQty")) {//已发
                    quitBody.setTV_alreadySend(data);
                } else if (tag.equals("FThisAuxQty")) {//本次
                    quitBody.setTV_thisSend(data);
                } else if (tag.equals("FMaterial_Name")) {//品名
                    quitBody.setTV_nameRoot(data);
                } else if (tag.equals("FModel")) {//规格
                    quitBody.setTV_size(data);
                }else if (tag.equals("FUnit_Name")) {//常用单位
                    quitBody.setTV_commonunit(data);
                }else if (tag.equals("FBaseUnit_Name")) {//基本单位
                    quitBody.setTV_statistics(data);
                } else if (tag.equals("FUnit")) {//物料编号
                    quitBody.setFUnit(data);
                }
            }

        }
    }
}
