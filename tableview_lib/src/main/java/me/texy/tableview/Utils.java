package me.texy.tableview;

import android.content.Context;

/**
 * Created by xinyuanzhong on 2017/5/9.
 */

public class Utils {
    public static int dp2px(Context context, double dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
