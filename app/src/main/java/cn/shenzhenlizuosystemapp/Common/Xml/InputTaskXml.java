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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TaskRvData;
import cn.shenzhenlizuosystemapp.Common.UI.InputLibraryActivity;

public class InputTaskXml {

    private volatile static InputTaskXml inputTaskXml;

    public static InputTaskXml getSingleton() {
        if (inputTaskXml == null) {
            synchronized (InputTaskXml.class) {
                if (inputTaskXml == null) {
                    inputTaskXml = new InputTaskXml();
                }
            }
        }
        return inputTaskXml;
    }
    
    public List GetInputBodyXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
       BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<TaskRvData> outbodys = handler.getBody();
        return outbodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<TaskRvData> OutBodys;
        private TaskRvData outbody;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<TaskRvData> getBody() {
            if (OutBodys != null) {
                return OutBodys;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            OutBodys = new ArrayList<TaskRvData>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table") || localName.equals("Table1")) {
                outbody = new TaskRvData();
                ViseLog.i("创建outbody");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table") || localName.equals("Table1")) {
                OutBodys.add(outbody);
                outbody = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("FGuid")) {
                    outbody.setFGUID(data);
                    ViseLog.i(data);
                } else if (tag.equals("FMaterial_Code")) {//物料编号
                    outbody.setTV_materID(data);
                } else if (tag.equals("FAuxQty")) {//应发
                    outbody.setTV_shouldSend(data);
                } else if (tag.equals("FExecutedAuxQty")) {//已发
                    outbody.setTV_alreadySend(data);
                } else if (tag.equals("FThisAuxQty")) {//本次
                    outbody.setTV_thisSend(data);
                } else if (tag.equals("FMaterial_Name")) {//品名
                    outbody.setTV_nameRoot(data);
                } else if (tag.equals("FModel")) {//规格
                    outbody.setTV_size(data);
                }else if (tag.equals("FUnit_Name")) {//常用单位
                    outbody.setTV_commonunit(data);
                }else if (tag.equals("FBaseUnit_Name")) {//基本单位
                    outbody.setTV_statistics(data);
                } else if (tag.equals("FMaterial")) {//物料编号
                    outbody.setFMaterial(data);
                }
            }

        }
    }
}
