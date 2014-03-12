package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DecorateCalendarView extends LinearLayout {

    private static final int WEEKDAYS = 7;
    private static final int MAX_WEEK = 6;

    private static final int BIGINNING_DAY_OF_WEEK = Calendar.SUNDAY;
    private static final int TODAY_COLOR = Color.RED;
    private static final int DEFAULT_COLOR = Color.DKGRAY;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

    private TextView mTitleView;

    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();

    public DecorateCalendarView(Context context) {
        this(context, null);
    }

    public DecorateCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);

        createTitleView(context);
        createWeekViews(context);
        createDayViews(context);
    }

    private void createTitleView(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;

        mTitleView = new TextView(context);
        mTitleView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTitleView.setTextSize((int) (scaleDensity * 14));
        mTitleView.setTypeface(null, Typeface.BOLD);
        mTitleView.setPadding(0, 0, 0, (int) (scaleDensity * 16));

        addView(mTitleView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void createWeekViews(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;
        mWeekLayout = new LinearLayout(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, BIGINNING_DAY_OF_WEEK);

        for (int counter = 0; counter < WEEKDAYS; counter++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.RIGHT);
            textView.setPadding(0, 0, (int) (scaleDensity * 4), 0);

            LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            mWeekLayout.addView(textView, layoutParams);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        addView(mWeekLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void createDayViews(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;

        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLine = new LinearLayout(context);
            mWeeks.add(weekLine);

            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.TOP | Gravity.RIGHT);
                textView.setPadding(0, (int) (scaleDensity * 4), (int) (scaleDensity * 4), 0);

                LayoutParams layoutParams = new LayoutParams(0, (int) (scaleDensity * 48));
                layoutParams.weight = 1;
                weekLine.addView(textView, layoutParams);
            }

            this.addView(weekLine, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void set(int year, int month) {
        setTitle(year, month);
        setWeeks();
        setDays(year, month);
    }

    private void setTitle(int year, int month) {
        Calendar targetCalendar = getTargetCalendar(year, month);

        //String formatString = mTitleView.getContext().getString(R.string.format_month_year);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        mTitleView.setText(formatter.format(targetCalendar.getTime()));
    }

    private void setWeeks() {
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, BIGINNING_DAY_OF_WEEK);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("E");

        for (int counter = 0; counter < WEEKDAYS; counter++) {
            TextView textView = (TextView) mWeekLayout.getChildAt(counter);
            textView.setText(weekFormatter.format(week.getTime()));

            week.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void setDays(int year, int month) {
        Calendar targetCalendar = getTargetCalendar(year, month);

        int skipCount = getSkipCount(targetCalendar);
        int lastDay = targetCalendar.getActualMaximum(Calendar.DATE);
        int dayCounter = 1;

        Calendar todayCalendar = Calendar.getInstance();
        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH);
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLayout = mWeeks.get(weekLoop);
            weekLayout.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);

            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                TextView textView = (TextView) weekLayout.getChildAt(dayLoop);

                if (weekLoop == 0 && skipCount > 0) {
                    textView.setText(" ");
                    skipCount--;
                    continue;
                }

                if (lastDay < dayCounter) {
                    textView.setText(" ");
                    continue;
                }

                textView.setText(String.valueOf(dayCounter));

                boolean isToday = (todayYear == year && todayMonth == month && todayDay == dayCounter);

                if (isToday) {
                    textView.setTextColor(TODAY_COLOR);
                    textView.setTypeface(null, Typeface.BOLD);
                    //weekLayout.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
                }
                else {
                    textView.setTextColor(DEFAULT_COLOR);
                    textView.setTypeface(null, Typeface.NORMAL);
                }
                dayCounter++;
            }
        }
    }

    private int getSkipCount(Calendar targetCalendar) {
        int skipCount;
        int firstDayOfWeekOfMonth = targetCalendar.get(Calendar.DAY_OF_WEEK);

        if (BIGINNING_DAY_OF_WEEK > firstDayOfWeekOfMonth) {
            skipCount = firstDayOfWeekOfMonth - BIGINNING_DAY_OF_WEEK + WEEKDAYS;
        }
        else {
            skipCount = firstDayOfWeekOfMonth - BIGINNING_DAY_OF_WEEK;
        }
        return skipCount;
    }

    private Calendar getTargetCalendar(int year, int month) {
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.clear();
        targetCalendar.set(Calendar.YEAR, year);
        targetCalendar.set(Calendar.MONTH, month);
        targetCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return targetCalendar;
    }
}
