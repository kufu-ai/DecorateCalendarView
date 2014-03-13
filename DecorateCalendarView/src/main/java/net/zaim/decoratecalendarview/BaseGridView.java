package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class BaseGridView extends ViewGroup {

    private final Paint dividerPaint = new Paint();
    private int oldWidthMeasureSize;
    private int oldNumRows;

    public BaseGridView(Context context) {
        this(context, null);
    }
    public BaseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dividerPaint.setColor(getResources().getColor(android.R.color.darker_gray));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final ViewGroup row = (ViewGroup) getChildAt(1);
        int top = row.getTop();
        int bottom = getBottom();

        final int left = row.getChildAt(0).getLeft() + getLeft();
        canvas.drawLine(left, top, left, bottom, dividerPaint);

        for (int counter = 0; counter < 7; counter++) {
            int x = left + row.getChildAt(counter).getRight() - 1;
            canvas.drawLine(x, top, x, bottom, dividerPaint);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawResutl = super.drawChild(canvas, child, drawingTime);

        final int bottom = child.getBottom() - 1;
        canvas.drawLine(child.getLeft(), bottom, child.getRight(), bottom, dividerPaint);
        return drawResutl;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        if (oldWidthMeasureSize == widthMeasureSize) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            return;
        }

        oldWidthMeasureSize = widthMeasureSize;
        int cellSize = widthMeasureSize / 7;
        widthMeasureSize = cellSize * 7;
        int totalHeight = 0;
        final int rowWidthSpec = makeMeasureSpec(widthMeasureSize, EXACTLY);
        final int rowHeightSpec = makeMeasureSpec(cellSize, EXACTLY);
        for (int counter = 0, numChildren = getChildCount(); counter < numChildren; counter++) {
            final View child = getChildAt(counter);
            if (child.getVisibility() == VISIBLE) {
                if (counter == 0) {
                    measureChild(child, rowWidthSpec, makeMeasureSpec(cellSize, AT_MOST));
                }
                else {
                    measureChild(child, rowWidthSpec, rowHeightSpec);
                }
                totalHeight += child.getMeasuredHeight();
            }
        }

        final int measureWidth = widthMeasureSize + 2;
        setMeasuredDimension(measureWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        top = 0;
        for (int counter = 0, numChildren = getChildCount(); counter < numChildren; counter++) {
            final View child = getChildAt(counter);
            final int rowHeight = child.getMeasuredHeight();
            child.layout(left, top, right, top + rowHeight);
            top += rowHeight;
        }
    }

    public void setNumRows(int numRows) {
        if (oldNumRows != numRows) {
            oldWidthMeasureSize = 0;
        }
        oldNumRows = numRows;
    }
}
