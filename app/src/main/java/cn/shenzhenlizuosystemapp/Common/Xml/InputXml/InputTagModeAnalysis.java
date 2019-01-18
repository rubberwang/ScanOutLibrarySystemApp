package cn.shenzhenlizuosystemapp.Common.Xml.InputXml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.ChildTag;

public class InputTagModeAnalysis {
    private volatile static InputTagModeAnalysis inputLibraryXmlAnalysis;

    public static InputTagModeAnalysis getSingleton() {
        if (inputLibraryXmlAnalysis == null) {
            synchronized (InputTagModeAnalysis.class) {
                if (inputLibraryXmlAnalysis == null) {
                    inputLibraryXmlAnalysis = new InputTagModeAnalysis();
                }
            }
        }
        return inputLibraryXmlAnalysis;
    }

    public static List<ChildTag> GetTagMode(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            ChildTag childTags = null;
            List<ChildTag> childTagList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        childTagList = new ArrayList<ChildTag>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcodes")) {
                            childTags = new ChildTag();
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
