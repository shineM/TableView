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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.DividerGenerator;
import me.texy.tableview.Interval;
import me.texy.tableview.TableItem;
import me.texy.tableview.TableLayout;

import static android.hardware.Sensor.TYPE_ORIENTATION;

public class MainActivity extends BaseActivity implements SensorEventListener{

    TableLayout tableLayout;
    private static int[] intervals = new int[]{43, 25, 7, 8, 40, 20, 33};

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        setContentView(R.layout.activity_main);
        initView();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        SensorManager.getOrientation()
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        event.values
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class MyTableItem extends TableItem {
        @Override
        public View initView() {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.table_item, null);

            return view;
        }
    }

}
