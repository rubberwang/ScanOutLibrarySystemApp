package cn.shenzhenlizuosystemapp.Common.Xml.TreeFromData;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.TreeParent;

public class TreeAnalysis {
    private volatile static TreeAnalysis treeAnalysis;

    public static TreeAnalysis getSingleton() {
        if (treeAnalysis == null) {
            synchronized (TreeAnalysis.class) {
                if (treeAnalysis == null) {
                    treeAnalysis = new TreeAnalysis();
                }
            }
        }
        return treeAnalysis;
    }

    public List<TreeParent> GetTreeFrom(InputStream inputStream) {
        TreeParent tree = null;
        List<TreeParent> TreeList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        TreeList = new ArrayList<TreeParent>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table0")) {
                            tree = new TreeParent();
                        } else if (name.equalsIgnoreCase("FGuid")) {
                            tree.setFGuid(parser.nextText());
                        } else if (name.equalsIgnoreCase("FCode")) {
                            tree.setFCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FName")) {
                            tree.setFName(parser.nextText());
                        } else if (name.equalsIgnoreCase("FParent")) {
                            tree.setFParent(parser.nextText());
                        }else if (name.equalsIgnoreCase("FLevel")) {
                            tree.setLevel(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table0")
                                && tree != null) {
                            TreeList.add(tree);
                            tree = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();
            return TreeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
