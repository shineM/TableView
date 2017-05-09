package me.texy.tableview;

/**
 * Created by xinyuanzhong on 2017/5/9.
 */

public interface DividerGenerator {
    //return the divider info before index
    //[width][color]
    int[] generateDivider(int index);
}
