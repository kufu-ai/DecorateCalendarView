package net.zaim.decoratecalendarview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DecorateCalendarView extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String BUNDLE_KEY_DISPLAY_TIME = "display_time";
    public static final String BUNDLE_KEY_SELECTED_COLOR = "selected_color";
    public static final String BUNDLE_KEY_BEGINNING_DAY_OF_WEEK = "begnning_day_of_week";
    public static final String BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE = "holiday_highlight_type";

    private OnDecorateCalendarListener mOnDecorateCalendarListener;

    private Context mContext;
    private ViewPager mMonthPager;
    private DecorateCalendarPagerAdapter mMonthPagerAdapter;
    private TextView mTitleView;
    private boolean mInitFlag = false;
    private DecorateCalendarFragment mCurrentFragment;

    public DecorateCalendarView(Context context) {
        this(context, null);
    }

    public DecorateCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);

        mContext = context;
        mMonthPager = (ViewPager) inflate(mContext, R.layout.month_pager, null);
        mMonthPager.setOnPageChangeListener(this);
    }

    public void initCalendar(FragmentManager fragmentManager, Bundle bundle) {
        createTitleView(mContext);
        addView(mMonthPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mMonthPagerAdapter = new DecorateCalendarPagerAdapter(fragmentManager, bundle);
        mMonthPager.setAdapter(mMonthPagerAdapter);
    }

    public void setTopTextOnDay(int day, String text, int color) {
        if (mCurrentFragment != null) mCurrentFragment.setTopTextOnDay(day, text, color);
    }

    public void setMiddleTextOnDay(int day, String text, int color) {
        if (mCurrentFragment != null) mCurrentFragment.setMiddleTextOnDay(day, text, color);
    }

    public void setBottomTextOnDay(int day, String text, int color) {
        if (mCurrentFragment != null) mCurrentFragment.setBottomTextOnDay(day, text, color);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && !mInitFlag) {
            setDefaultTitleAndMovePage();
            mInitFlag = true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Calendar calendar = mMonthPagerAdapter.getDisplayCalendar(position);
        setTitle(calendar);
        mCurrentFragment = mMonthPagerAdapter.getCurrentFragment(position);
        if (mOnDecorateCalendarListener != null) {
            mOnDecorateCalendarListener.onChangeDisplayMonth(calendar.getTime());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void createTitleView(Context context) {
        View header = inflate(context, R.layout.header, null);
        mTitleView = (TextView) header.findViewById(R.id.header_title);
        ((ImageView) header.findViewById(R.id.prev_button)).setOnClickListener(this);
        ((ImageView) header.findViewById(R.id.next_button)).setOnClickListener(this);
        addView(header);
    }

    private void setDefaultTitleAndMovePage() {
        Calendar calendar = Calendar.getInstance();
        // The previous is 2 years, the future is 2 years and current year.
        int defaultPosition = 12 * 2 + calendar.get(Calendar.MONTH);
        mMonthPager.setCurrentItem(defaultPosition, false);
    }

    private void setTitle(int year, int month) {
        Calendar targetCalendar = getTargetCalendar(year, month);
        setTitle(targetCalendar);
    }

    private void setTitle(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(getResources().getString(R.string.calendar_title_format));
        mTitleView.setText(formatter.format(calendar.getTime()));
    }

    private Calendar getTargetCalendar(int year, int month) {
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.clear();
        targetCalendar.set(Calendar.YEAR, year);
        targetCalendar.set(Calendar.MONTH, month);
        targetCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return targetCalendar;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            clickSwitchCalendar(v);
        }
    }

    private void clickSwitchCalendar(View view) {
        int relativePageNum = (view.getId() == R.id.prev_button) ? -1 : 1;
        mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() + relativePageNum);
    }


    public void setOnDecorateCalendarListener(OnDecorateCalendarListener listener) {
        mOnDecorateCalendarListener = listener;
    }

    public abstract interface OnDecorateCalendarListener {
        void onDayClick(Date day);
        void onChangeDisplayMonth(Date date);
    }
}
