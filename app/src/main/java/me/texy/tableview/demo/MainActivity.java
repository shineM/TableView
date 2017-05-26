package me.texy.tableview.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.demo.tableview.TableLayout;

import static android.hardware.Sensor.TYPE_ORIENTATION;

public class MainActivity extends BaseActivity {

    TableLayout tableLayout;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        initView();
        TableLayout.DividerGenerator dividerGenerator = new TableLayout.DividerGenerator() {
            @Override
            public int[] generateDivider(int index) {
                return new int[]{dp2px(context, 1), Color.GRAY};
            }
        };
        tableLayout.setRowDividerGenerator(dividerGenerator);
        tableLayout.setColumnDividerGenerator(dividerGenerator);
        tableLayout.addItems(mockItems());
    }

    private List<? extends TableLayout.TableItem> mockItems() {
        List<NormalTableItem> itemList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                NormalTableItem item = new NormalTableItem();
                item.setColumnIndex(i);
                item.setInterval(new TableLayout.Interval(j, j));
                if (i == 0 && j == 0) {
                    item.setValue("");
                } else if (i == 0) {
                    item.setValue("Student " + j);
                } else if (j == 0) {
                    item.setValue("Subject " + i);
                } else {
                    item.setValue(" " + (i * j + 50));
                }
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.schedule_table:
                intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.elements_table:
                intent = new Intent(MainActivity.this, ElementsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class NormalTableItem extends TableLayout.TableItem {

        @Override
        public View initView() {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.normal_table_item, null);
            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getValue().toString());
            return view;
        }
    }

}
