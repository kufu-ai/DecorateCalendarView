package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DecorateCalendarView extends LinearLayout implements View.OnClickListener {

    public static final String HOLIDAY_HIGHLIGHT_TYPE_TEXT = "text";
    public static final String HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND = "background";

    private static final int WEEKDAYS = 7;
    private static final int MAX_WEEK = 6;

    private static final int LABEL_DATE_TEXT_INDEX = 0;
    private static final int LABEL_TOP_TEXT_INDEX = 1;
    private static final int LABEL_MIDDLE_TEXT_INDEX = 2;
    private static final int LABEL_BOTTOM_TEXT_INDEX = 3;

    private OnDecorateCalendarListener mOnDecorateCalendarListener;
    private int displayYear;
    private int displayMonth;

    private BaseGridView gridView;

    private View mSelectedView;
    private TextView mTitleView;

    private LinearLayout mWeekLayout;
    private ArrayList<LinearLayout> mWeeks = new ArrayList<LinearLayout>();
    private String mHolidayHightlightType = HOLIDAY_HIGHLIGHT_TYPE_TEXT;
    private int mBiginningDayOfWeek = Calendar.SUNDAY;
    private int mCurrentDayColor = -1;
    private boolean mIsPagerMode = false;

    public DecorateCalendarView(Context context) {
        this(context, null);
    }

    public DecorateCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);

        gridView = (BaseGridView) inflate(context, R.layout.month, null);

        createTitleView(context);
        createWeekViews();
        createDayViews();

        gridView.setNumRows(mWeeks.size());
        addView(gridView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setmBiginningDayOfWeek(int biginningDayOfWeek) {
        mBiginningDayOfWeek = biginningDayOfWeek;
    }

    public void setHolidayHighlightType(String type) {
        mHolidayHightlightType = type;
    }

    public void setCurrentDayColor(int color) {
        mCurrentDayColor = color;
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

    public void setPagerMode(boolean isPagerMode) {
        mIsPagerMode = isPagerMode;
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

    private void createTitleView(Context context) {
        View header = inflate(context, R.layout.header, null);
        mTitleView = (TextView) header.findViewById(R.id.header_title);
        ((ImageView) header.findViewById(R.id.prev_button)).setOnClickListener(this);
        ((ImageView) header.findViewById(R.id.next_button)).setOnClickListener(this);
        addView(header);
    }

    private void createWeekViews() {
        mWeekLayout = (LinearLayout) gridView.findViewById(R.id.day_of_week_container);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, mBiginningDayOfWeek);

        for (int counter = 0; counter < WEEKDAYS; counter++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void createDayViews() {
        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLine = (LinearLayout) gridView.getChildAt(weekLoop + 1);
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
        setTitle(year, month);
        setWeeks();
        setDays(year, month);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        if (mOnDecorateCalendarListener != null) mOnDecorateCalendarListener.onChangeDisplayMonth(calendar.getTime());
    }

    private void setTitle(int year, int month) {
        Calendar targetCalendar = getTargetCalendar(year, month);

        SimpleDateFormat formatter = new SimpleDateFormat(getResources().getString(R.string.calendar_title_format));
        mTitleView.setText(formatter.format(targetCalendar.getTime()));
    }

    private void setWeeks() {
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, mBiginningDayOfWeek);
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

        if (mBiginningDayOfWeek > firstDayOfWeekOfMonth) {
            skipCount = firstDayOfWeekOfMonth - mBiginningDayOfWeek + WEEKDAYS;
        }
        else {
            skipCount = firstDayOfWeekOfMonth - mBiginningDayOfWeek;
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
    public void onClick(View v) {
        if (v instanceof RelativeLayout) {
            clickCellOfDays(v);
        }
        else if (v instanceof ImageView) {
            clickSwitchCalendar(v);
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
        if (mOnDecorateCalendarListener != null) mOnDecorateCalendarListener.onDayClick(cal.getTime());
    }

    private void clickSwitchCalendar(View view) {
        if ( mIsPagerMode && mOnDecorateCalendarListener != null) {
            int relativePosition = (view.getId() == R.id.prev_button) ? -1 : 1;
            mOnDecorateCalendarListener.onSwitchCalendarClick(relativePosition);
            return;
        }

        if (view.getId() == R.id.prev_button) {
            displayMonth--;
            if (displayMonth < 1) {
                displayMonth = 12;
                displayYear--;
            }
            set(displayYear, displayMonth);
        }
        else if (view.getId() == R.id.next_button) {
            displayMonth++;
            if (displayMonth > 12) {
                displayMonth -= 12;
                displayYear++;
            }
            set(displayYear, displayMonth);
        }
    }

    private void changeCurrentDayColor(View currentDayView) {
        if (mCurrentDayColor < 0) return;
        if (mSelectedView != null) mSelectedView.setBackgroundColor(getResources().getColor(R.color.default_background));
        currentDayView.setBackgroundColor(mCurrentDayColor);
        mSelectedView = currentDayView;
    }


    public void setOnDecorateCalendarListener(OnDecorateCalendarListener listener) {
        mOnDecorateCalendarListener = listener;
    }

    public abstract interface OnDecorateCalendarListener {
        void onDayClick(Date day);
        void onChangeDisplayMonth(Date date);
        void onSwitchCalendarClick(int position);
    }
}
