package me.texy.tableview.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.demo.tableview.TableLayout;

public class ScheduleActivity extends BaseActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        initView();
        setTitle("Schedule Table");
        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        initDivider();
        tableLayout.addItems(mockScheduleList());
    }

    private void initDivider() {
        tableLayout.setRowDividerGenerator(new TableLayout.DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                return new int[]{1, Color.parseColor("#7B7B7B")};
            }
        });
        tableLayout.setColumnDividerGenerator(new TableLayout.DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                return new int[]{dp2px(context, 1), 0};
            }
        });
    }

    private class MyTableItem extends TableLayout.TableItem {
        @Override
        public View initView() {
            View view = LayoutInflater.from(ScheduleActivity.this)
                    .inflate(me.texy.tableview.demo.R.layout.schedule_table_item, null);
            int[] bgs = new int[]{
                    R.drawable.corner_blue,
                    R.drawable.corner_green,
                    R.drawable.corner_yellow,
                    R.drawable.corner_red};
            view.setBackground(getResources().getDrawable(bgs[getColumnIndex() % 4]));
            TextView textView = (TextView) view.findViewById(me.texy.tableview.demo.R.id.item_text);
            textView.setText(getValue().toString());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),
                            getValue().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }

    private List<? extends TableLayout.TableItem> mockScheduleList() {
        List<TableLayout.TableItem> itemList = new ArrayList<>();

        MyTableItem tableItem = new MyTableItem();
        tableItem.setInterval(new TableLayout.Interval(0, 2));
        tableItem.setColumnIndex(0);
        tableItem.setValue("Work out");
        itemList.add(tableItem);

        MyTableItem tableItem1 = new MyTableItem();
        tableItem1.setInterval(new TableLayout.Interval(2, 4));
        tableItem1.setColumnIndex(1);
        tableItem1.setValue("Work out");
        itemList.add(tableItem1);

        MyTableItem tableItem2 = new MyTableItem();
        tableItem2.setInterval(new TableLayout.Interval(3, 5));
        tableItem2.setColumnIndex(2);
        tableItem2.setValue("Work out");
        itemList.add(tableItem2);

        MyTableItem tableItem3 = new MyTableItem();
        tableItem3.setInterval(new TableLayout.Interval(0, 3));
        tableItem3.setColumnIndex(3);
        tableItem3.setValue("Work out");
        itemList.add(tableItem3);

        MyTableItem tableItem4 = new MyTableItem();
        tableItem4.setInterval(new TableLayout.Interval(4, 8));
        tableItem4.setColumnIndex(0);
        tableItem4.setValue("'Silicon Valley' time");
        itemList.add(tableItem4);

        MyTableItem tableItem5 = new MyTableItem();
        tableItem5.setInterval(new TableLayout.Interval(8, 12));
        tableItem5.setColumnIndex(2);
        tableItem5.setValue("Read <<Java Concurrency in Practice>>");
        itemList.add(tableItem5);

        MyTableItem tableItem6 = new MyTableItem();
        tableItem6.setInterval(new TableLayout.Interval(4, 9));
        tableItem6.setColumnIndex(4);
        tableItem6.setValue("Practice Kotlin");
        itemList.add(tableItem6);

        MyTableItem tableItem7 = new MyTableItem();
        tableItem7.setInterval(new TableLayout.Interval(1, 5));
        tableItem7.setColumnIndex(5);
        tableItem7.setValue("Go shopping");
        itemList.add(tableItem7);

        MyTableItem tableItem8 = new MyTableItem();
        tableItem8.setInterval(new TableLayout.Interval(0, 3));
        tableItem8.setColumnIndex(6);
        tableItem8.setValue("Swim");
        itemList.add(tableItem8);
        return itemList;
    }
}
