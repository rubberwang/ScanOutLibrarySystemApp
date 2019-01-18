package cn.shenzhenlizuosystemapp.Common.Xml.QuitXml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitDataAnalysis.ChildQuitTag;

public class QuitTagModeAnalysis {
    private volatile static QuitTagModeAnalysis quitLibraryXmlAnalysis;

    public static QuitTagModeAnalysis getSingleton() {
        if (quitLibraryXmlAnalysis == null) {
            synchronized (QuitTagModeAnalysis.class) {
                if (quitLibraryXmlAnalysis == null) {
                    quitLibraryXmlAnalysis = new QuitTagModeAnalysis();
                }
            }
        }
        return quitLibraryXmlAnalysis;
    }

    public static List<ChildQuitTag> GetTagMode(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            ChildQuitTag childQuitTags = null;
            List<ChildQuitTag> childQuitTagList = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        childQuitTagList = new ArrayList<ChildQuitTag>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Barcodes")) {
                            childQuitTags = new ChildQuitTag();
                        } else if ("FGuid".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childQuitTags)) {
                                childQuitTags.setGuid(parser.nextText());
                            }
                        } else if ("FBarcodeName".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childQuitTags)) {
                                childQuitTags.setName(parser.nextText());
                            }
                        } else if ("FRowIndex".equals(parser.getName())) {
                            if (Tools.IsObjectNull(childQuitTags)) {
                                childQuitTags.setBarcodeNumber(parser.nextText());
                            }
                        } else if ("FIsMust".equals(parser.getName())) {
                            childQuitTags.setFIsMust(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Barcodes")
                                && childQuitTags != null) {
                            childQuitTagList.add(childQuitTags);
                            childQuitTags = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return childQuitTagList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
