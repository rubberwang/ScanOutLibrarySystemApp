package cn.shenzhenlizuosystemapp.Common.Xml.QuitXml;

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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.ChildQuitTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.ScanQuitXmlResult;

public class GetQuitChildTag {

    private volatile static GetQuitChildTag getQuitChildTag;

    public static GetQuitChildTag getSingleton() {
        if (getQuitChildTag == null) {
            synchronized (GetQuitChildTag.class) {
                if (getQuitChildTag == null) {
                    getQuitChildTag = new GetQuitChildTag();
                }
            }
        }
        return getQuitChildTag;
    }

    public List getChildQuitTagXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<ChildQuitTag> childQuitTagsList = handler.getBody();
        return childQuitTagsList;
    }

    public List getScanQuitXmlResult(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<ScanQuitXmlResult> xmlResults = handler.getScanXml();
        return xmlResults;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<ChildQuitTag> childQuitTags;
        private List<ScanQuitXmlResult> scanQuitXmlResultList;
        private ChildQuitTag childQuitTag;// 当前解析的student
        private String tag;// 当前解析的标签
        private ScanQuitXmlResult scanQuitXmlResult;

        public List<ChildQuitTag> getBody() {
            if (childQuitTags != null) {
                return childQuitTags;
            }
            return null;
        }

        public List<ScanQuitXmlResult> getScanXml() {
            if (scanQuitXmlResult != null) {
                return scanQuitXmlResultList;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            childQuitTags = new ArrayList<ChildQuitTag>();
            scanQuitXmlResultList = new ArrayList<>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Show")) {
                childQuitTag = new ChildQuitTag();
                ViseLog.i("创建ChildQuitTag");
            } else if (localName.equals("BarcodeLib")) {
                scanQuitXmlResult = new ScanQuitXmlResult();
                ViseLog.i("创建ScanQuitXmlResult");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Show")) {
                childQuitTags.add(childQuitTag);
                childQuitTag = null;
            } else if (localName.equals("BarcodeLib")) {
                scanQuitXmlResultList.add(scanQuitXmlResult);
                scanQuitXmlResult = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("Success")) {
                    scanQuitXmlResult.setResult(data);
                } else if (tag.equals("name")) {
                    childQuitTag.setName(data);
                } else if (tag.equals("FQty")) {
                    scanQuitXmlResult.setFQty(data);
                } else if (tag.equals("value")) {
                    childQuitTag.setValue(data);
                }
            }
        }
    }
}
