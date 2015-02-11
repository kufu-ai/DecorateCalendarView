package net.zaim.decoratecalendarview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

public class NewPagerAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_PAGE_NUMBER = 12 * 5;

    private int mBasePosition;
    private int mCurrentYear;
    private int mCurrentMonth;

    public NewPagerAdapter(FragmentManager fm, int basePosition) {
        super(fm);
        mBasePosition = basePosition;
        Calendar calendar = Calendar.getInstance();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public Fragment getItem(int position) {
        int targetYear = mCurrentYear;
        int targetMonth = mCurrentMonth;
        int relativePosition = position - mBasePosition;
        targetMonth += relativePosition;
        while (targetMonth < 1 || targetMonth > 12) {
            if (targetMonth < 1) {
                targetYear--;
                targetMonth += 12;
            } else if (targetMonth > 12) {
                targetYear++;
                targetMonth -= 12;
            }
        }
        return NewDecorateCalendarFragment.newInstance(targetYear, targetMonth);
    }

    @Override
    public int getCount() {
        return MAX_PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
}
