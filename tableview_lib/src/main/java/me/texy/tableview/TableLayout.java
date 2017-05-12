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


import static me.texy.tableview.TableLayout.DividerInfo.DIVIDER_COLOR_INDEX;
import static me.texy.tableview.TableLayout.DividerInfo.DIVIDER_WIDTH_INDEX;

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

    private List<TableItem> tableItems = new ArrayList<>();

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
        dividerInfo.prepareDividers(Axis.X, columnDividerGenerator);
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
            int right = (int) (left + itemWidth);
            int top = (int) topOfIndex;
            int bottom = (int) (topOfIndex + getItemVerticalHeight(tableItem));

            tableItem.getView().layout(left, top, right, bottom);
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
            // TODO: 2017/5/10 add default size of divider
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
