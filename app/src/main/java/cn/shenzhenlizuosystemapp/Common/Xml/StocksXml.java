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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TaskRvData;

public class StocksXml {

    private volatile static StocksXml stocksXml;

    public static StocksXml getSingleton() {
        if (stocksXml == null) {
            synchronized (StocksXml.class) {
                if (stocksXml == null) {
                    stocksXml = new StocksXml();
                }
            }
        }
        return stocksXml;
    }

    public List GetStocksXml(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<StockBean> outbodys = handler.getBody();
        return outbodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<StockBean> stockBeanList;
        private StockBean stockBean;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<StockBean> getBody() {
            if (stockBeanList != null) {
                return stockBeanList;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            stockBeanList = new ArrayList<StockBean>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table")) {
                stockBean = new StockBean();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table")) {
                stockBeanList.add(stockBean);
                stockBean = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("FGuid")) {
                    stockBean.setFGuid(data);
                } else if (tag.equals("FName")) {//物料编号
                    stockBean.setFName(data);
                }
            }
        }
    }
}
