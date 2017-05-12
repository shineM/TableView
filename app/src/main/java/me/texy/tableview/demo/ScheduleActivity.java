package me.texy.tableview.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.DividerGenerator;
import me.texy.tableview.Interval;
import me.texy.tableview.TableItem;
import me.texy.tableview.TableLayout;
import me.texy.tableview.demo.R;

public class ScheduleActivity extends BaseActivity {
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        initView();
        getSupportActionBar().setTitle("Schedule Table");
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        List<TableItem> itemList = new ArrayList<>();

        MyTableItem tableItem = new MyTableItem();
        tableItem.setInterval(new Interval(0, 20));
        tableItem.setColumnIndex(0);
        itemList.add(tableItem);

        MyTableItem tableItem1 = new MyTableItem();
        tableItem1.setInterval(new Interval(20, 35));
        tableItem1.setColumnIndex(1);
        itemList.add(tableItem1);

        MyTableItem tableItem2 = new MyTableItem();
        tableItem2.setInterval(new Interval(10, 30));
        tableItem2.setColumnIndex(2);
        itemList.add(tableItem2);

        MyTableItem tableItem3 = new MyTableItem();
        tableItem3.setInterval(new Interval(0, 15));
        tableItem3.setColumnIndex(3);
        itemList.add(tableItem3);

        MyTableItem tableItem4 = new MyTableItem();
        tableItem4.setInterval(new Interval(40, 55));
        tableItem4.setColumnIndex(0);
        itemList.add(tableItem4);

        MyTableItem tableItem5 = new MyTableItem();
        tableItem5.setInterval(new Interval(45, 65));
        tableItem5.setColumnIndex(2);
        itemList.add(tableItem5);

        MyTableItem tableItem6 = new MyTableItem();
        tableItem6.setInterval(new Interval(25, 35));
        tableItem6.setColumnIndex(4);
        itemList.add(tableItem6);

        MyTableItem tableItem7 = new MyTableItem();
        tableItem7.setInterval(new Interval(15, 40));
        tableItem7.setColumnIndex(5);
        itemList.add(tableItem7);

        MyTableItem tableItem8 = new MyTableItem();
        tableItem8.setInterval(new Interval(0, 20));
        tableItem8.setColumnIndex(6);
        itemList.add(tableItem8);

        tableLayout.setRowDividerGenerator(new DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                if (index % 5 == 0) {
                    return new int[]{3, Color.parseColor("#DCDCDC")};
                } else {
                    return new int[]{3, Color.parseColor("#efefef")};
                }
            }
        });
        tableLayout.setColumnDividerGenerator(new DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                return new int[]{2,0};
            }
        });
        tableLayout.addItems(itemList);
    }
    private class MyTableItem extends TableItem {
        @Override
        public View initView() {
            View view = LayoutInflater.from(ScheduleActivity.this).inflate(me.texy.tableview.demo.R.layout.table_item, null);
            int[] bgs = new int[]{me.texy.tableview.demo.R.drawable.corner_blue, me.texy.tableview.demo.R.drawable.corner_gray, me.texy.tableview.demo.R.drawable.corner_pink};
            view.setBackground(getResources().getDrawable(bgs[getColumnIndex()%3]));
            TextView textView = (TextView) view.findViewById(me.texy.tableview.demo.R.id.item_text);
            textView.setText("程咬金");
            TextView textView2 = (TextView) view.findViewById(me.texy.tableview.demo.R.id.item_text2);
            textView2.setText("八年级");
            TextView textView3 = (TextView) view.findViewById(me.texy.tableview.demo.R.id.item_text3);
            textView3.setText("待上课");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"程咬金 八年级 待上课",Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }
}
