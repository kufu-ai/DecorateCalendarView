package net.zaim.decoratecalendarview;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Calendar;

public class NewDecorateCalendarView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private ViewPager mViewPager;

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
        mViewPager = (ViewPager) inflate(context, R.layout.month_pager, null);
        mViewPager.setOnPageChangeListener(this);
        addView(mViewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        Calendar calendar = Calendar.getInstance();
        // The previous is 2 years, the future is 2 years and current year.
        int defaultPosition = 12 * 2 + calendar.get(Calendar.MONTH);
        mViewPager.setAdapter(new NewPagerAdapter(fragmentManager, defaultPosition));
        mViewPager.setCurrentItem(defaultPosition, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
