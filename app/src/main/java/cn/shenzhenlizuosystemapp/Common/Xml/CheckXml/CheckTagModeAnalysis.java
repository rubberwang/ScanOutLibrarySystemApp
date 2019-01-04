package cn.shenzhenlizuosystemapp.Common.Xml.CheckXml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.ChildCheckTag;

public class CheckTagModeAnalysis {
    private volatile static CheckTagModeAnalysis inputLibraryXmlAnalysis;

    public static CheckTagModeAnalysis getSingleton() {
        if (inputLibraryXmlAnalysis == null) {
            synchronized (CheckTagModeAnalysis.class) {
                if (inputLibraryXmlAnalysis == null) {
                    inputLibraryXmlAnalysis = new CheckTagModeAnalysis();
                }
            }
        }
        return inputLibraryXmlAnalysis;
    }

    public static List<ChildCheckTag> GetTagMode(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            ChildCheckTag childTags = null;
            List<ChildCheckTag> childTagList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        childTagList = new ArrayList<ChildCheckTag>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcodes")) {
                            childTags = new ChildCheckTag();
                        } else if ("FGuid".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childTags)) {
                                childTags.setGuid(parser.nextText());
                            }
                        } else if ("FBarcodeName".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childTags)) {
                                childTags.setName(parser.nextText());
                            }
                        } else if ("FRowIndex".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childTags)) {
                                childTags.setBarcodeNumber(parser.nextText());
                            }
                        } else if ("FIsMust".equals(parser.getName())) {
                            childTags.setFIsMust(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcodes")
                                && childTags != null) {
                            childTagList.add(childTags);
                            childTags = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return childTagList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
