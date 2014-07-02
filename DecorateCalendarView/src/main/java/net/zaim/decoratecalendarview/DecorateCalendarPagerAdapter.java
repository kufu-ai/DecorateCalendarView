package net.zaim.decoratecalendarview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

public class DecorateCalendarPagerAdapter extends FragmentStatePagerAdapter {

    private int mColorResourceId;

    public DecorateCalendarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setSelectedColorId(int resourceId) {
        mColorResourceId = resourceId;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putLong(DecorateCalendarFragment.BUNDLE_KEY_DISPLAY_TIME, getDisplayTime(i));
        bundle.putInt(DecorateCalendarFragment.BUNDLE_KEY_SELECTED_COLOR, mColorResourceId);
        return DecorateCalendarFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return Calendar.DECEMBER + 1;
    }

    private long getDisplayTime(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, position);
        return calendar.getTimeInMillis();
    }
}
