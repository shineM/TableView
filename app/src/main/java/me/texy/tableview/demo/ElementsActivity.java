package me.texy.tableview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.texy.tableview.demo.R;

public class ElementsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);
        initView();
    }
}
