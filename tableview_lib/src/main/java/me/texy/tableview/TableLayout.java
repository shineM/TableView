package me.texy.tableview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinyuanzhong on 2017/5/5.
 */

public class TableLayout extends ViewGroup {
    private static final int DEFAULT_COLUMN_COUNT = 7;
    private static final int DEFAULT_ROW_COUNT = 12;
    private static final int DEFAULT_ITEM_WIDTH = 30;
    private static final int DEFAULT_ITEM_HEIGHT = 30;
    private static final int DEFAULT_COLUMN_Divider = 0;
    private static final int DEFAULT_ROW_Divider = 0;

    private Context context;

    private DividerGenerator rowDividerGenerator;

    private DividerGenerator columnDividerGenerator;

    private DividerInfo dividerInfo = new DividerInfo();

    private double itemWidth;

    private double itemHeight;

    private int columnCount;

    private int rowCount;

    private List<TableItem> tableItems;

    private boolean itemCanCross = true;

    private boolean drawColumnDividerFirst;

    private boolean drawColumnEdgeDivider = true;

    private boolean drawRowEdgeDivider = true;

    private Paint dividerPaint = new Paint();

    public void setRowDividerGenerator(DividerGenerator rowDividerGenerator) {
        this.rowDividerGenerator = rowDividerGenerator;
        dividerInfo.prepareDividers(Axis.Y, rowDividerGenerator);
    }

    public void setColumnDividerGenerator(DividerGenerator columnDividerGenerator) {
        this.columnDividerGenerator = columnDividerGenerator;
        dividerInfo.prepareDividers(Axis.X, rowDividerGenerator);
    }

    enum Axis {
        Y,
        X
    }

    public TableLayout(Context context) {
        this(context, null);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.
                obtainStyledAttributes(attrs, R.styleable.TableLayout, defStyleAttr, 0);
        itemWidth = typedArray.
                getDimensionPixelSize(R.styleable.TableLayout_item_width,
                        Utils.dp2px(context, DEFAULT_ITEM_WIDTH));
        itemHeight = typedArray.
                getDimensionPixelSize(R.styleable.TableLayout_item_height,
                        Utils.dp2px(context, DEFAULT_ITEM_HEIGHT));
        columnCount = typedArray.
                getInteger(R.styleable.TableLayout_column_count, DEFAULT_COLUMN_COUNT);
        rowCount = typedArray.
                getInteger(R.styleable.TableLayout_row_count, DEFAULT_ROW_COUNT);
        drawColumnDividerFirst = typedArray.
                getBoolean(R.styleable.TableLayout_column_divider_first, false);

        dividerInfo.init();
        dividerPaint.setAntiAlias(true);

        setWillNotDraw(false);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (TableItem item : tableItems) {
            View child = item.getView();
            //If you has assigned the width of TableLayout（match_parent or specific dimension,
            // then the attr itemWidth will not work
            if (widthMode == MeasureSpec.EXACTLY) {
                itemWidth = (((widthSize - getPaddingLeft() - getPaddingRight())
                        - dividerInfo.columnDividersWidth)) / columnCount;
            }

            measureChild(child,
                    MeasureSpec.makeMeasureSpec((int) itemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getItemVerticalHeight(item), MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize
                        : (int) (columnCount * itemWidth
                        + dividerInfo.columnDividersWidth
                        + getPaddingLeft() + getPaddingRight()),
                heightMode == MeasureSpec.EXACTLY ? heightSize
                        : (int) (rowCount * itemHeight
                        + dividerInfo.rowDividersWidth
                        + getPaddingBottom() + getPaddingTop()));
    }

    private int getItemVerticalHeight(TableItem item) {
        int Divider = item.getInterval().getEndIndex() - item.getInterval().getStartIndex();
        if (!itemCanCross) {
            if (Divider > 0) {
                throw new IllegalArgumentException(
                        "The table item Divider must less than 1 when item can not cross!");
            }
            return (int) itemHeight;
        } else {
            return (int) (getTopOfIndex(item.getInterval().getEndIndex())
                    - getTopOfIndex(item.getInterval().getStartIndex()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (tableItems == null || tableItems.size() == 0) {
            return;
        }

        for (TableItem tableItem : tableItems) {
            int left = (int) getLeftOfIndex(tableItem.getColumnIndex());
            int right = (int) (left + itemWidth);
            int top = (int) getTopOfIndex(tableItem.getInterval().getStartIndex());
            int bottom = top + getItemVerticalHeight(tableItem);

            tableItem.getView().layout(left, top, right, bottom);
        }
    }

    private double getTopOfIndex(int index) {
        return getPaddingTop() + index * itemHeight
                + dividerInfo.getDividerWidthBefore(Axis.Y, index);
    }

    private double getLeftOfIndex(int index) {
        return getPaddingLeft() + index * itemWidth
                + dividerInfo.getDividerWidthBefore(Axis.X, index);
    }

    public void addItem(TableItem tableItem) {
        if (tableItems == null) {
            tableItems = new ArrayList<>();
        }
        tableItems.add(tableItem);
        addView(tableItem.getView());
    }

    public void addItems(List<TableItem> tableItems) {
        if (this.tableItems == null) {
            this.tableItems = new ArrayList<>();
        }
        this.tableItems.addAll(tableItems);
        for (TableItem item : tableItems) {
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
            int rowDivider = rowDividers[i][0];
            int rowDividerColor = rowDividers[i][1];

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
            int columnDivider = columnDividers[i][0];
            int columnDividerColor = columnDividers[i][1];

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

    private class DividerInfo {
        public int[][] columnDividers;

        public int[][] rowDividers;

        public double columnDividersWidth;

        public double rowDividersWidth;

        public double getDividerWidthBefore(Axis axis, int index) {
            double result = 0.0;
            for (int i = 0; i < index; i++) {
                result += axis == Axis.X ? columnDividers[i][0] : rowDividers[i][0];
            }
            return result;
        }

        public void prepareDividers(Axis axis, DividerGenerator dividerGenerator) {
            if (axis == Axis.X) {
                columnDividersWidth = 0;
                for (int i = 0; i < columnCount + 1; i++) {
                    columnDividers[i] = dividerGenerator.generateDivider(i);
                    columnDividersWidth += columnDividers[i][0];
                }
            } else {
                rowDividersWidth = 0;
                for (int i = 0; i < rowCount + 1; i++) {
                    rowDividers[i] = dividerGenerator.generateDivider(i);
                    rowDividersWidth += rowDividers[i][0];
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
                    return new int[]{1, Color.parseColor("#efefef")};
                }
            });
        }
    }
}
