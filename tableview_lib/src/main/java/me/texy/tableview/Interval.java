package me.texy.tableview;

/**
 * Created by xinyuanzhong on 2017/5/9.
 */

public class Interval {
    private int startIndex;

    private int endIndex;

    public Interval(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
