package me.texy.tableview.demo.element;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.texy.tableview.demo.ElementsActivity;
import me.texy.tableview.demo.R;

/**
 * Created by xinyuanzhong on 2017/5/24.
 */

public class ElementUtil {
    public static int getColumnIndexOfElement(Element e) {
        int id = e.index;
        if (id < 3) {
            return id == 1 ? 0 : 17;
        } else if (id < 19) {
            int remainder = (id - 3) % 8;
            return remainder < 2 ? remainder : remainder + 10;
        } else if (id < 57) {
            return (id - 1) % 18;
        } else if (id < 72) {
            return (id - 57) % 15 + 2;
        } else if (id < 89) {
            return (id - 69) % 18;
        } else if (id < 104) {
            return (id - 89) % 15 + 2;
        } else {
            return id % 104 + 3;
        }
    }

    public static int getRowIndexOfElement(Element e) {
        int id = e.index;
        if (id < 3) {
            return 0;
        } else if (id < 19) {
            return (id - 3) / 8 + 1;
        } else if (id < 57) {
            return (id - 1) / 18 + 2;
        } else if (id < 72) {
            return 8;
        } else if (id < 89) {
            return (id - 69) / 18 + 5;
        } else if (id < 104) {
            return 9;
        } else {
            return 6;
        }
    }

    public static List<Element> getElements(Context context) {
        List<Element> elements = new ArrayList<>();

        Element element = null;
        XmlResourceParser xrp = context.getResources().getXml(R.xml.elements);

        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {

                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();

                    if (tagName.equals("elementid")) {
                        String id = xrp.nextText();
                        element = new Element();
                        element.index = Integer.parseInt(id);
                        element.showIndex = id;
                    } else if (tagName.equals("elementname")) {
                        element.chineseName = xrp.nextText();
                    } else if (tagName.equals("symbol")) {
                        element.charName = xrp.nextText();
                    } else if (tagName.equals("types")) {
                        element.type = Element.Type.getEnum(xrp.nextText());
                    }
                } else if (xrp.getEventType() == XmlPullParser.END_TAG) {
                    if (xrp.getName().equals("element") && element != null) {
                        elements.add(element);
                        element = null;
                    }
                }

                xrp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elements;
    }
}
