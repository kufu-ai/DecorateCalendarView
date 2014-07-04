package net.zaim.decoratecalendarview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.Calendar;

public class DecorateCalendarPagerAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_PAGE_NUMBER = 12 * 5;

    private SparseArray<DecorateCalendarFragment> mCalendarFragments;
    private String mBeginningDayOfWeek;
    private String mHolidayHighlightType;
    private String mColorResourceId;
    private int mCurrentYear;

    public DecorateCalendarPagerAdapter(FragmentManager fragmentManager, Bundle bundle) {
        super(fragmentManager);
        mCalendarFragments = new SparseArray<DecorateCalendarFragment>();
        mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (bundle != null) {
            mBeginningDayOfWeek = bundle.getString(DecorateCalendarView.BUNDLE_KEY_BEGINNING_DAY_OF_WEEK);
            mHolidayHighlightType = bundle.getString(DecorateCalendarView.BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE);
            mColorResourceId = bundle.getString(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR);
        }
    }

    public DecorateCalendarFragment getCurrentFragment(int position) {
        return mCalendarFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putLong(DecorateCalendarView.BUNDLE_KEY_DISPLAY_TIME, getDisplayCalendar(position).getTimeInMillis());
        if (mBeginningDayOfWeek != null) bundle.putInt(DecorateCalendarView.BUNDLE_KEY_BEGINNING_DAY_OF_WEEK, Integer.parseInt(mBeginningDayOfWeek));
        if (mHolidayHighlightType != null) bundle.putString(DecorateCalendarView.BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE, mHolidayHighlightType);
        if (mColorResourceId != null) bundle.putInt(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR, Integer.parseInt(mColorResourceId));
        DecorateCalendarFragment fragment = DecorateCalendarFragment.newInstance(bundle);
        mCalendarFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return MAX_PAGE_NUMBER;
    }

    public Calendar getDisplayCalendar(int position) {
        int displayYear = mCurrentYear;
        if (0 <= position && position < 12) displayYear -= 2;
        else if (12 <= position && position < 12 * 2) displayYear -= 1;
        else if (12 * 2 <= position && position < 12 * 3) ; // current
        else if (12 * 3 <= position && position < 12 * 4) displayYear += 1;
        else if (12 * 4 <= position && position < 12 * 5) displayYear += 2;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(displayYear, position % 12, 1);
        return calendar;
    }
}
