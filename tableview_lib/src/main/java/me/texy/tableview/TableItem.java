package me.texy.tableview;

import android.view.View;

/**
 * Created by xinyuanzhong on 2017/5/9.
 */

public abstract class TableItem {
    private int columnIndex;

    private Interval interval = new Interval(0, 5);

    private Object value;

    private View view;

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public View getView() {
        if (view == null) {
            view = initView();
        }
        return view;
    }

    public abstract View initView();
}
