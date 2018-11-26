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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildTag;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TaskRvData;

public class GetChildTag {

    private volatile static GetChildTag getChildTag;

    public static GetChildTag getSingleton() {
        if (getChildTag == null) {
            synchronized (GetChildTag.class) {
                if (getChildTag == null) {
                    getChildTag = new GetChildTag();
                }
            }
        }
        return getChildTag;
    }

    public List getChildTagXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<ChildTag> childTagsList = handler.getBody();
        return childTagsList;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<ChildTag> childTags;
        private ChildTag childTag;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<ChildTag> getBody() {
            if (childTags != null) {
                return childTags;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            childTags = new ArrayList<ChildTag>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Show")||localName.equals("BarcodeLib")) {
                childTag = new ChildTag();
                ViseLog.i("创建outbody");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Show")||localName.equals("BarcodeLib")) {
                childTags.add(childTag);
                childTag = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("Success")) {
                    childTag.setOneChildTag(data);
                }else if(tag.equals("name")) {
                    childTag.setName(data);
                }
            }
        }
    }
}
