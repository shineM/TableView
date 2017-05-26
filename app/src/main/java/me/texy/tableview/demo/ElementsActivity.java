package me.texy.tableview.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.demo.element.Element;
import me.texy.tableview.demo.element.ElementUtil;
import me.texy.tableview.demo.tableview.TableLayout;

public class ElementsActivity extends BaseActivity {
    private static final String[] ELEMENTS_TYPE_COLORS = new String[]{
            "#1E88E5", "#27953C", "#FFAB00", "#D32F2F", "#7E57C2", "#944625", "#EF5350"
    };
    private TableLayout tableLayout;
    private List<Element> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);
        initView();
        setTitle("Elements Table");

        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        elements = ElementUtil.getElements(this);

        initDivider();

        tableLayout.addItems(getElementsItems());
    }

    private void initDivider() {
        TableLayout.DividerGenerator dividerGenerator = new TableLayout.DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                return new int[]{dp2px(context, 2), 0};
            }
        };
        tableLayout.setRowDividerGenerator(dividerGenerator);
        tableLayout.setColumnDividerGenerator(dividerGenerator);
    }

    private List<? extends TableLayout.TableItem> getElementsItems() {
        List<ElementTableItem> itemList = new ArrayList<>();
        for (Element e : elements) {
            ElementTableItem tableItem = new ElementTableItem();
            tableItem.setValue(e);
            tableItem.setColumnIndex(ElementUtil.getColumnIndexOfElement(e));
            int rowIndexOfElement = ElementUtil.getRowIndexOfElement(e);
            tableItem.setInterval(new TableLayout.Interval(rowIndexOfElement, rowIndexOfElement));
            itemList.add(tableItem);
        }
        ElementTableItem specialItem1 = new ElementTableItem();
        specialItem1.setColumnIndex(2);
        specialItem1.setInterval(new TableLayout.Interval(5, 5));
        Element specialElement1 = new Element();
        specialElement1.charName = "";
        specialElement1.chineseName = "";
        specialElement1.showIndex = "57-71";
        specialElement1.type = Element.Type.NONE;
        specialItem1.setValue(specialElement1);
        itemList.add(specialItem1);

        ElementTableItem specialItem2 = new ElementTableItem();
        specialItem2.setColumnIndex(2);
        specialItem2.setInterval(new TableLayout.Interval(6, 6));
        Element specialElement2 = new Element();
        specialElement2.charName = "";
        specialElement2.chineseName = "";
        specialElement2.showIndex = "89-103";
        specialElement2.type = Element.Type.NONE;
        specialItem2.setValue(specialElement2);
        itemList.add(specialItem2);

        return itemList;
    }

    private class ElementTableItem extends TableLayout.TableItem {
        @Override
        public View initView() {
            Element element = (Element) getValue();
            View view = LayoutInflater.from(ElementsActivity.this)
                    .inflate(R.layout.elements_table_item, null);
            View bottomLine = view.findViewById(R.id.bottom_line);

            TextView idText = (TextView) view.findViewById(R.id.e_id_text);
            TextView charText = (TextView) view.findViewById(R.id.e_char_text);
            TextView chineseText = (TextView) view.findViewById(R.id.e_chinese_text);

            idText.setText(element.showIndex);
            charText.setText(element.charName);
            chineseText.setText(element.chineseName);
            bottomLine.setBackgroundColor(getElementColorByType(element.type));

            return view;
        }
    }

    private int getElementColorByType(Element.Type type) {
        if (type == null) {
            return Color.GRAY;
        }
        switch (type) {
            case NOBLE_GAS:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[3]);
            case HALOGEN:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[0]);
            case NON_METAL:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[2]);
            case ALKALI_METAL:
            case ALKALI_EARTH_METAL:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[1]);
            case ACTINIDE_METAL:
            case LANTHANUM_METAL:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[5]);
            case MAIN_GROUP_METAL:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[4]);
            case NONE:
                return Color.TRANSPARENT;
            case TRANSITION_METAL:
            default:
                return Color.parseColor(ELEMENTS_TYPE_COLORS[6]);

        }
    }
}
