package me.texy.tableview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.Interval;
import me.texy.tableview.TableItem;
import me.texy.tableview.TableLayout;


public class MainActivity extends AppCompatActivity {
    TableLayout tableLayout;
    private static int[] intervals = new int[]{43,25,7,8,40,20,33};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        List<TableItem> itemList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
            MyTableItem tableItem = new MyTableItem();
            tableItem.setInterval(new Interval(0, 20));
            tableItem.setColumnIndex(1);
            tableItem.setValue(new String("黑泽明五年级"));
            itemList.add(tableItem);
//        }
        tableLayout.addItem(tableItem);
    }

    private class MyTableItem extends TableItem {
        @Override
        public View initView() {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.table_item, null);
            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getValue().toString());
            return view;
        }
    }

}
