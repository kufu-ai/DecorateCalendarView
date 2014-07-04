package net.zaim.decoratecalendarview;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DecorateCalendarFragment extends Fragment implements View.OnClickListener {

    public static final String HOLIDAY_HIGHLIGHT_TYPE_TEXT = "text";
    public static final String HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND = "background";

    private static final int WEEKDAYS = 7;
    private static final int MAX_WEEK = 6;

    private static final int LABEL_DATE_TEXT_INDEX = 0;
    private static final int LABEL_TOP_TEXT_INDEX = 1;
    private static final int LABEL_MIDDLE_TEXT_INDEX = 2;
    private static final int LABEL_BOTTOM_TEXT_INDEX = 3;

    private DecorateCalendarView.OnDecorateCalendarListener mListener;
    private BaseGridView vGridView;

    private int displayYear;
    private int displayMonth;

    private View mSelectedView;

    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();
    private String mHolidayHightlightType = HOLIDAY_HIGHLIGHT_TYPE_TEXT;
    private int mBeginningDayOfWeek = Calendar.SUNDAY;
    private int mCurrentDayColor = -1;

    public static DecorateCalendarFragment newInstance(Bundle bundle) {
        DecorateCalendarFragment instance = new DecorateCalendarFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DecorateCalendarView.OnDecorateCalendarListener) activity;
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vGridView = (BaseGridView) inflater.inflate(R.layout.month, container, false);
        createWeekViews();
        createDayViews();
        vGridView.setNumRows(mWeeks.size());

        Bundle bundle = getArguments();
        long displayTime = bundle.getLong(DecorateCalendarView.BUNDLE_KEY_DISPLAY_TIME, 0);
        set(new Date(displayTime));

        mBeginningDayOfWeek = bundle.getInt(DecorateCalendarView.BUNDLE_KEY_BEGINNING_DAY_OF_WEEK);
        mHolidayHightlightType = bundle.getString(DecorateCalendarView.BUNDLE_KEY_HOLIDAY_HIGHLIGHT_TYPE);
        if (bundle.getInt(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR) > 0) {
            mCurrentDayColor = getResources().getColor(bundle.getInt(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR));
        }

        return vGridView;
    }

    private void createWeekViews() {
        mWeekLayout = (LinearLayout) vGridView.findViewById(R.id.day_of_week_container);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, mBeginningDayOfWeek);

        for (int counter = 0; counter < WEEKDAYS; counter++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void createDayViews() {
        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLine = (LinearLayout) vGridView.getChildAt(weekLoop + 1);
            mWeeks.add(weekLine);
        }
    }

    public void set(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }

    public void set(int year, int month) {
        this.displayYear = year;
        this.displayMonth = month;
        setWeeks();
        setDays(year, month);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
    }

    private void setWeeks() {
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, mBeginningDayOfWeek);
        SimpleDateFormat weekFormatter = new SimpleDateFormat(getResources().getString(R.string.calendar_dayofweek_format));

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

            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                RelativeLayout dayContainer = (RelativeLayout) weekLayout.getChildAt(dayLoop);

                initializeCellOfDay(dayContainer);

                if (weekLoop == 0 && skipCount > 0) {
                    skipCount--;
                    continue;
                }

                if (lastDay < dayCounter) {
                    continue;
                }

                boolean isToday = (todayYear == year && todayMonth == month && todayDay == dayCounter);
                int dayOfWeek = targetCalendar.get(Calendar.DAY_OF_WEEK);
                buildCellOfDay(dayContainer, dayCounter, dayOfWeek, isToday);

                targetCalendar.add(Calendar.DAY_OF_MONTH, 1);
                dayCounter++;
            }
        }
    }

    private void initializeCellOfDay(RelativeLayout cellOfDay) {
        cellOfDay.setBackgroundColor(getResources().getColor(R.color.disable_background));
        ((TextView) cellOfDay.getChildAt(LABEL_DATE_TEXT_INDEX)).setText(" ");
        ((TextView) cellOfDay.getChildAt(LABEL_TOP_TEXT_INDEX)).setText(" ");
        ((TextView) cellOfDay.getChildAt(LABEL_MIDDLE_TEXT_INDEX)).setText(" ");
        ((TextView) cellOfDay.getChildAt(LABEL_BOTTOM_TEXT_INDEX)).setText(" ");
    }

    private void buildCellOfDay(RelativeLayout cellOfDay, int dayCounter, int dayOfWeek, boolean isToday) {
        cellOfDay.setOnClickListener(this);
        cellOfDay.setBackgroundColor(getResources().getColor(R.color.default_background));

        TextView dateText = (TextView) cellOfDay.getChildAt(LABEL_DATE_TEXT_INDEX);
        dateText.setText(String.valueOf(dayCounter));

        if (isToday) dateText.setTypeface(null, Typeface.BOLD);
        else dateText.setTypeface(null, Typeface.NORMAL);

        if (mHolidayHightlightType != null && mHolidayHightlightType.equals(HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND)) {
            if (dayOfWeek == Calendar.SUNDAY) cellOfDay.setBackgroundColor(getResources().getColor(R.color.sunday_background));
            else if (dayOfWeek == Calendar.SATURDAY) cellOfDay.setBackgroundColor(getResources().getColor(R.color.saturday_background));
        }
        else {
            if (dayOfWeek == Calendar.SUNDAY) dateText.setTextColor(getResources().getColor(R.color.sunday_text));
            else if (dayOfWeek == Calendar.SATURDAY) dateText.setTextColor(getResources().getColor(R.color.saturday_text));
        }
    }

    private int getSkipCount(Calendar targetCalendar) {
        int skipCount;
        int firstDayOfWeekOfMonth = targetCalendar.get(Calendar.DAY_OF_WEEK);

        if (mBeginningDayOfWeek > firstDayOfWeekOfMonth) {
            skipCount = firstDayOfWeekOfMonth - mBeginningDayOfWeek + WEEKDAYS;
        }
        else {
            skipCount = firstDayOfWeekOfMonth - mBeginningDayOfWeek;
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

    @Override
    public void onClick(View view) {
        if (view instanceof RelativeLayout) {
            clickCellOfDays(view);
        }
    }

    private void clickCellOfDays(View view) {
        Calendar cal = Calendar.getInstance();
        try {
            int cellDay = Integer.parseInt(((TextView) ((RelativeLayout) view).getChildAt(LABEL_DATE_TEXT_INDEX)).getText().toString());
            cal.set(displayYear, displayMonth, cellDay);
            changeCurrentDayColor(view);
        }
        catch (NumberFormatException e) {
            return;
        }
        if (mListener != null) mListener.onDayClick(cal.getTime());
    }

    private void changeCurrentDayColor(View currentDayView) {
        if (mCurrentDayColor < 0) return;
        if (mSelectedView != null) mSelectedView.setBackgroundColor(getResources().getColor(R.color.default_background));
        currentDayView.setBackgroundColor(mCurrentDayColor);
        mSelectedView = currentDayView;
    }

    public void setTopTextOnDay(int day, String text, int color) {
        setLabelWithPosition(day, text, LABEL_TOP_TEXT_INDEX, color);
    }

    public void setMiddleTextOnDay(int day, String text, int color) {
        setLabelWithPosition(day, text, LABEL_MIDDLE_TEXT_INDEX, color);
    }

    public void setBottomTextOnDay(int day, String text, int color) {
        setLabelWithPosition(day, text, LABEL_BOTTOM_TEXT_INDEX, color);
    }

    private void setLabelWithPosition(int day, String text, int position, int color) {
        if (day < 0 || text == null || text.equals("")) return;

        if (position < LABEL_TOP_TEXT_INDEX || position > LABEL_BOTTOM_TEXT_INDEX) return;

        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLayout = mWeeks.get(weekLoop);
            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                RelativeLayout dayContainer = (RelativeLayout) weekLayout.getChildAt(dayLoop);
                try {
                    int cellDay = Integer.parseInt(((TextView) dayContainer.getChildAt(LABEL_DATE_TEXT_INDEX)).getText().toString());
                    if (cellDay == day) {
                        ((TextView) dayContainer.getChildAt(position)).setText(text);
                        ((TextView) dayContainer.getChildAt(position)).setTextColor(color);
                        break;
                    }
                }
                catch (NumberFormatException e) {
                }
            }
        }
    }
}
