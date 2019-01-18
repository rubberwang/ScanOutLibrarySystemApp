package cn.shenzhenlizuosystemapp.Common.Xml.InputXml;

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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.ScanInputXmlResult;

public class GetInputChildTag {

    private volatile static GetInputChildTag getInputChildTag;

    public static GetInputChildTag getSingleton() {
        if (getInputChildTag == null) {
            synchronized (GetInputChildTag.class) {
                if (getInputChildTag == null) {
                    getInputChildTag = new GetInputChildTag();
                }
            }
        }
        return getInputChildTag;
    }

    public List getChildTagXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<ChildTag> childTagsList = handler.getBody();
        return childTagsList;
    }

    public List getScanXmlResult(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<ScanInputXmlResult> xmlResults = handler.getScanXml();
        return xmlResults;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<ChildTag> childTags;
        private List<ScanInputXmlResult> scanInputXmlResultList;
        private ChildTag childTag;// 当前解析的student
        private String tag;// 当前解析的标签
        private ScanInputXmlResult scanInputXmlResult;

        public List<ChildTag> getBody() {
            if (childTags != null) {
                return childTags;
            }
            return null;
        }

        public List<ScanInputXmlResult> getScanXml() {
            if (scanInputXmlResultList != null) {
                return scanInputXmlResultList;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            childTags = new ArrayList<ChildTag>();
            scanInputXmlResultList = new ArrayList<>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Show")) {
                childTag = new ChildTag();
                ViseLog.i("创建ChildTag");
            } else if (localName.equals("BarcodeLib")) {
                scanInputXmlResult = new ScanInputXmlResult();
                ViseLog.i("创建ScanXmlResult");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Show")) {
                childTags.add(childTag);
                childTag = null;
            } else if (localName.equals("BarcodeLib")) {
                scanInputXmlResultList.add(scanInputXmlResult);
                scanInputXmlResult = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("Success")) {
                    scanInputXmlResult.setResult(data);
                } else if (tag.equals("name")) {
                    childTag.setName(data);
                } else if (tag.equals("FQty")) {
                    scanInputXmlResult.setFQty(data);
                } else if (tag.equals("value")) {
                    childTag.setValue(data);
                }
            }
        }
    }
}
