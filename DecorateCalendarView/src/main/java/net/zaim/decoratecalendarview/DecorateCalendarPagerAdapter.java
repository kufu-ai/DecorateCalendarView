package net.zaim.decoratecalendarview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.Calendar;

public class DecorateCalendarPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<DecorateCalendarFragment> mCalendarFragments;
    private String mBeginningDayOfWeek;
    private String mHolidayHighlightType;
    private String mColorResourceId;
    private int mDisplayYear;

    public DecorateCalendarPagerAdapter(FragmentManager fragmentManager, Bundle bundle) {
        super(fragmentManager);
        mCalendarFragments = new SparseArray<DecorateCalendarFragment>();
        mDisplayYear = Calendar.getInstance().get(Calendar.YEAR);
        if (bundle != null) {
            mBeginningDayOfWeek = bundle.getString(DecorateCalendarView.BUNDLE_KEY_BEGINNING_DAY_OF_WEEK);
            mHolidayHighlightType = bundle.getString(DecorateCalendarView.BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE);
            mColorResourceId = bundle.getString(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR);
        }
    }

    public void setDisplayYear(int displayYear) {
        mDisplayYear = displayYear;
    }

    public DecorateCalendarFragment getCurrentFragment(int position) {
        return mCalendarFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putLong(DecorateCalendarView.BUNDLE_KEY_DISPLAY_TIME, getDisplayTime(position));
        if (mBeginningDayOfWeek != null) bundle.putInt(DecorateCalendarView.BUNDLE_KEY_BEGINNING_DAY_OF_WEEK, Integer.parseInt(mBeginningDayOfWeek));
        if (mHolidayHighlightType != null) bundle.putString(DecorateCalendarView.BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE, mHolidayHighlightType);
        if (mColorResourceId != null) bundle.putInt(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR, Integer.parseInt(mColorResourceId));
        DecorateCalendarFragment fragment = DecorateCalendarFragment.newInstance(bundle);
        mCalendarFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return Calendar.DECEMBER + 1;
    }

    private long getDisplayTime(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mDisplayYear);
        calendar.set(Calendar.MONTH, position);
        return calendar.getTimeInMillis();
    }
}
