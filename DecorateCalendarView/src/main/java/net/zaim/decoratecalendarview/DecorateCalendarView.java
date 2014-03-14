package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

    private BaseGridView gridView;

    private TextView mTitleView;

    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();

    public DecorateCalendarView(Context context) {
        this(context, null);
    }

    public DecorateCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);

        gridView = new BaseGridView(context);

        createTitleView(context);
        createWeekViews(context);
        createDayViews(context);

        gridView.setNumRows(mWeeks.size());
        addView(gridView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void createTitleView(Context context) {
        View header = inflate(context, R.layout.header, null);
        mTitleView = (TextView) header.findViewById(R.id.header_title);
        addView(header);
    }

    private void createWeekViews(Context context) {
        mWeekLayout = (LinearLayout) inflate(context, R.layout.day_of_week, null);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, BIGINNING_DAY_OF_WEEK);

        for (int counter = 0; counter < WEEKDAYS; counter++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridView.addView(mWeekLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // width, heightを一緒にセットしないとレイアウト内部のweightが効かなくなる
        //gridView.addView(mWeekLayout);
    }

    private void createDayViews(Context context) {
        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLine = (LinearLayout) inflate(context, R.layout.days, null);
            mWeeks.add(weekLine);
            gridView.addView(weekLine, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
