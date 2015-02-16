package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Calendar;

public class NewDecorateCalendarView extends FrameLayout {

    private Context mContext;
    private RecyclerView mRecyclerView;

    public NewDecorateCalendarView(Context context) {
        super(context);
        initialize(context);
    }

    public NewDecorateCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public NewDecorateCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        mRecyclerView = (RecyclerView) inflate(context, R.layout.month_recyclerview, null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        addView(mRecyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        Calendar calendar = Calendar.getInstance();
        // The previous is 2 years, the future is 2 years and current year.
//        int defaultPosition = 12 * 2 + calendar.get(Calendar.MONTH);
//        mViewPager.setAdapter(new NewPagerAdapter(fragmentManager, defaultPosition));
//        mViewPager.setCurrentItem(defaultPosition, false);
        int defaultPosition = 49 * 12 * 2 + 49 * calendar.get(Calendar.MONTH);
        mRecyclerView.scrollToPosition(defaultPosition);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        mRecyclerView.setAdapter(new NewAdapter(mContext, year, month, defaultPosition / 49));
    }

//    private int[] getTargetYearAndMonth(int position) {
//        int targetYear = mCurrentYear;
//        int targetMonth = mCurrentMonth;
//        int relativePosition = position - mBasePosition;
//        targetMonth += relativePosition;
//        while (targetMonth < 1 || targetMonth > 12) {
//            if (targetMonth < 1) {
//                targetYear--;
//                targetMonth += 12;
//            } else if (targetMonth > 12) {
//                targetYear++;
//                targetMonth -= 12;
//            }
//        }
//        return new int[] {targetYear, targetMonth};
//    }

    private static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private GradientDrawable mDivider;

        public DividerItemDecoration() {
            mDivider = new GradientDrawable();
            mDivider.setStroke(1, 0x0c000000);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                int left = childView.getLeft();
                int right = childView.getRight();
                int top = childView.getTop();
                int bottom = childView.getBottom();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
