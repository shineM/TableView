package me.texy.tableview.demo.tableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.texy.tableview.demo.R;

import static me.texy.tableview.demo.tableview.TableLayout.DividerInfo.DIVIDER_COLOR_INDEX;
import static me.texy.tableview.demo.tableview.TableLayout.DividerInfo.DIVIDER_WIDTH_INDEX;

/**
 * Created by xinyuanzhong on 2017/5/5.
 */

public class TableLayout extends ViewGroup {
    private static final int DEFAULT_ROW_COUNT = 12;
    private static final int DEFAULT_COLUMN_COUNT = 7;

    private static final int DEFAULT_ITEM_WIDTH = 60;
    private static final int DEFAULT_ITEM_HEIGHT = 60;

    private DividerInfo dividerInfo = new DividerInfo();

    private double itemWidth;

    private double itemHeight;

    private int columnCount;

    private int rowCount;

    private List<TableItem> tableItems = new ArrayList<>();

    private boolean itemCanCross = true;

    private boolean drawColumnDividerFirst;

    private Paint dividerPaint = new Paint();

    private boolean specificItemWidth;

    private boolean specificItemHeight;

    enum Axis {
        Y,
        X
    }

    public void setRowDividerGenerator(DividerGenerator rowDividerGenerator) {
        dividerInfo.prepareDividers(Axis.Y, rowDividerGenerator);
    }

    public void setColumnDividerGenerator(DividerGenerator columnDividerGenerator) {
        dividerInfo.prepareDividers(Axis.X, columnDividerGenerator);
    }

    public TableLayout(Context context) {
        this(context, null);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.
                obtainStyledAttributes(attrs, R.styleable.TableLayout, defStyleAttr, 0);
        itemWidth = typedArray.
                getDimensionPixelSize(R.styleable.TableLayout_item_width, 0);

        itemHeight = typedArray.
                getDimensionPixelSize(R.styleable.TableLayout_item_height, 0);
        columnCount = typedArray.
                getInteger(R.styleable.TableLayout_column_count, DEFAULT_COLUMN_COUNT);
        rowCount = typedArray.
                getInteger(R.styleable.TableLayout_row_count, DEFAULT_ROW_COUNT);
        drawColumnDividerFirst = typedArray.
                getBoolean(R.styleable.TableLayout_column_divider_first, false);

        dividerInfo.init();
        dividerPaint.setAntiAlias(true);

        specificItemWidth = itemWidth != 0;
        specificItemHeight = itemHeight != 0;

        setWillNotDraw(false);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //If you did not assign the width or height of item then the attr
        // itemWidth or itemHeight will calculated by layout size or use default size.
        if (!specificItemWidth) {
            if (widthMode == MeasureSpec.EXACTLY) {
                itemWidth = (((widthSize - getPaddingLeft() - getPaddingRight())
                        - dividerInfo.columnDividersWidth)) / columnCount;
            } else {
                itemWidth = DEFAULT_ITEM_WIDTH;
            }
        }
        if (!specificItemHeight) {
            if (heightMode == MeasureSpec.EXACTLY) {
                itemHeight = (((heightSize - getPaddingTop() - getPaddingBottom())
                        - dividerInfo.rowDividersWidth)) / rowCount;
            } else {
                itemHeight = DEFAULT_ITEM_HEIGHT;
            }
        }


        for (TableItem item : tableItems) {
            View child = item.getView();

            child.measure(MeasureSpec.makeMeasureSpec((int) itemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) getItemVerticalHeight(item), MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize
                        : (int) (columnCount * itemWidth + dividerInfo.columnDividersWidth
                        + getPaddingLeft() + getPaddingRight()),
                heightMode == MeasureSpec.EXACTLY ? heightSize
                        : (int) (rowCount * itemHeight + dividerInfo.rowDividersWidth
                        + getPaddingBottom() + getPaddingTop()));
    }

    private double getItemVerticalHeight(TableItem item) {
        int crossCount = item.getInterval().getEndIndex() - item.getInterval().getStartIndex();
        if (!itemCanCross || crossCount == 0) {
            if (crossCount == 0) return itemHeight;
            throw new IllegalArgumentException(
                    "The table item Divider must less than 1 when item can not cross!");
        } else {
            return getTopOfIndex(item.getInterval().getEndIndex() - 1)
                    - getTopOfIndex(item.getInterval().getStartIndex())
                    + itemHeight;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (tableItems == null || tableItems.size() == 0) {
            return;
        }

        for (TableItem tableItem : tableItems) {
            double topOfIndex = getTopOfIndex(tableItem.getInterval().getStartIndex());

            int left = (int) getLeftOfIndex(tableItem.getColumnIndex());
            int top = (int) topOfIndex;

            tableItem.getView().layout(left, top, left + tableItem.getView().getMeasuredWidth(),
                    top + tableItem.getView().getMeasuredHeight());
        }
    }

    private double getTopOfIndex(int index) {
        return getPaddingTop() + index * itemHeight
                + dividerInfo.getDividersWidthBefore(Axis.Y, index);
    }

    private double getLeftOfIndex(int index) {
        return getPaddingLeft() + index * itemWidth
                + dividerInfo.getDividersWidthBefore(Axis.X, index);
    }

    public void addItem(TableItem tableItem) {
        if (tableItems == null) {
            tableItems = new ArrayList<>();
        }
        if (!tableItems.contains(tableItem)) {
            tableItems.add(tableItem);
            View view = tableItem.getView();
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            if (indexOfChild(view) == -1) {
                addView(view);
            }
        } else {
            requestLayout();
        }
    }

    public void addItems(List<? extends TableItem> tableItemList) {
        if (tableItemList == null) {
            return;
        }
        if (tableItems == null) {
            tableItems = new ArrayList<>();
        }

        for (TableItem item : tableItemList) {
            addItem(item);
        }
    }

    public void resetItems(List<? extends TableItem> tableItemList) {
        if (tableItemList == null) {
            return;
        }
        tableItems.clear();
        removeAllViews();

        for (TableItem item : tableItemList) {
            tableItems.add(item);
            item.setView(null);
            addView(item.getView());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawColumnDividerFirst) {
            drawColumnDivider(canvas);
            drawRowDivider(canvas);
        } else {
            drawRowDivider(canvas);
            drawColumnDivider(canvas);
        }
    }

    private void drawRowDivider(Canvas canvas) {
        int[][] rowDividers = dividerInfo.rowDividers;
        for (int i = 0; i < rowDividers.length; i++) {
            int rowDivider = rowDividers[i][DIVIDER_WIDTH_INDEX];
            int rowDividerColor = rowDividers[i][DIVIDER_COLOR_INDEX];

            if (rowDividerColor != 0) {
                dividerPaint.setColor(rowDividerColor);
                dividerPaint.setStrokeWidth(rowDivider);

                int startX = getPaddingLeft();
                int stopX = getWidth() - getPaddingRight();
                int startY = (int) (getTopOfIndex(i) - rowDivider / 2);

                canvas.drawLine(startX, startY, stopX, startY, dividerPaint);
            }
        }
    }

    private void drawColumnDivider(Canvas canvas) {
        int[][] columnDividers = dividerInfo.columnDividers;
        for (int i = 0; i < columnDividers.length; i++) {
            int columnDivider = columnDividers[i][DIVIDER_WIDTH_INDEX];
            int columnDividerColor = columnDividers[i][DIVIDER_COLOR_INDEX];

            if (columnDividerColor != 0) {
                dividerPaint.setColor(columnDividerColor);
                dividerPaint.setStrokeWidth(columnDivider);

                int startX = (int) (getLeftOfIndex(i) - columnDivider / 2);
                int startY = getPaddingTop();
                int stopY = getHeight() - getPaddingBottom();

                canvas.drawLine(startX, startY, startX, stopY, dividerPaint);
            }
        }
    }

    public class DividerInfo {
        public static final int DIVIDER_WIDTH_INDEX = 0;
        public static final int DIVIDER_COLOR_INDEX = 1;

        public int[][] columnDividers;

        public int[][] rowDividers;

        public double columnDividersWidth;

        public double rowDividersWidth;

        public double getDividersWidthBefore(Axis axis, int index) {
            double result = 0.0;
            for (int i = 0; i < index + 1; i++) {
                result += axis == Axis.X ? columnDividers[i][DIVIDER_WIDTH_INDEX] :
                        rowDividers[i][DIVIDER_WIDTH_INDEX];
            }
            return result;
        }

        public void prepareDividers(Axis axis, DividerGenerator dividerGenerator) {
            if (axis == Axis.X) {
                columnDividersWidth = 0;
                for (int i = 0; i < columnCount + 1; i++) {
                    columnDividers[i] = dividerGenerator.generateDivider(i);
                    columnDividersWidth += columnDividers[i][DIVIDER_WIDTH_INDEX];
                }
            } else {
                rowDividersWidth = 0;
                for (int i = 0; i < rowCount + 1; i++) {
                    rowDividers[i] = dividerGenerator.generateDivider(i);
                    rowDividersWidth += rowDividers[i][DIVIDER_WIDTH_INDEX];
                }
            }
        }

        public void init() {
            columnDividers = new int[columnCount + 1][2];
            rowDividers = new int[rowCount + 1][2];

            prepareDividers(Axis.X, new DividerGenerator() {
                @Override
                public int[] generateDivider(int index) {
                    return new int[]{0, 0};
                }
            });
            prepareDividers(Axis.Y, new DividerGenerator() {
                @Override
                public int[] generateDivider(int index) {
                    return new int[]{0, 0};
                }
            });
        }
    }

    public interface DividerGenerator {
        //return the divider info before index
        //[width][color]
        int[] generateDivider(int index);
    }

    public static class Interval {
        private int startIndex;

        private int endIndex;

        public Interval(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }
    }

    public abstract static class TableItem {
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

        public void setView(View view) {
            this.view = view;
        }
    }
}
