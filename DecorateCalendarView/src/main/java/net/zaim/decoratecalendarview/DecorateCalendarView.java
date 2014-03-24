package net.zaim.decoratecalendarview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DecorateCalendarView extends LinearLayout implements View.OnClickListener {

    private static final int WEEKDAYS = 7;
    private static final int MAX_WEEK = 6;

    private static final int LABEL_DATE_INDEX_AT_CELL = 0;
    private static final int LABEL_FIRST_INDEX_AT_CELL = 1;
    private static final int LABEL_SECOND_INDEX_AT_CELL = 2;

    private static final int BIGINNING_DAY_OF_WEEK = Calendar.SUNDAY;
    private static final int TODAY_COLOR = Color.RED;
    private static final int DEFAULT_COLOR = Color.DKGRAY;

    private OnDecorateCalendarListener mOnDecorateCalendarListener;
    private int displayYear;
    private int displayMonth;

    private BaseGridView gridView;

    private View mSelectedView;
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

    public void setPaymentLabel(int day, String text) {
        setLabelWithPosition(day, text, LABEL_FIRST_INDEX_AT_CELL);
    }

    public void setIncomeLabel(int day, String text) {
        setLabelWithPosition(day, text, LABEL_SECOND_INDEX_AT_CELL);
    }

    private void setLabelWithPosition(int day, String text, int position) {
        for (int weekLoop = 0; weekLoop < MAX_WEEK; weekLoop++) {
            LinearLayout weekLayout = mWeeks.get(weekLoop);
            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                LinearLayout dayContainer = (LinearLayout) weekLayout.getChildAt(dayLoop);
                try {
                    int cellDay = Integer.parseInt(((TextView) dayContainer.getChildAt(LABEL_DATE_INDEX_AT_CELL)).getText().toString());
                    if (cellDay == day) {
                        ((TextView) dayContainer.getChildAt(position)).setText(text);
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
        ((TextView) header.findViewById(R.id.prev_button)).setOnClickListener(this);
        ((TextView) header.findViewById(R.id.next_button)).setOnClickListener(this);
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
            LinearLayout weekLine = (LinearLayout) inflate(context, R.layout.row_week, null);
            mWeeks.add(weekLine);
            gridView.addView(weekLine, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

            for (int dayLoop = 0; dayLoop < WEEKDAYS; dayLoop++) {
                LinearLayout dayContainer = (LinearLayout) weekLayout.getChildAt(dayLoop);
                TextView dateLabel = (TextView) dayContainer.getChildAt(LABEL_DATE_INDEX_AT_CELL);

                ((TextView) dayContainer.getChildAt(LABEL_FIRST_INDEX_AT_CELL)).setText(" ");
                ((TextView) dayContainer.getChildAt(LABEL_SECOND_INDEX_AT_CELL)).setText(" ");

                if (weekLoop == 0 && skipCount > 0) {
                    dateLabel.setText(" ");
                    dayContainer.setBackgroundColor(getResources().getColor(R.color.disable_background));
                    skipCount--;
                    continue;
                }

                if (lastDay < dayCounter) {
                    dateLabel.setText(" ");
                    dayContainer.setBackgroundColor(getResources().getColor(R.color.disable_background));
                    continue;
                }

                dayContainer.setOnClickListener(this);
                dateLabel.setText(String.valueOf(dayCounter));

                boolean isToday = (todayYear == year && todayMonth == month && todayDay == dayCounter);

                if (isToday) {
                    dateLabel.setTextColor(TODAY_COLOR);
                    dateLabel.setTypeface(null, Typeface.BOLD);
                }
                else {
                    dateLabel.setTextColor(DEFAULT_COLOR);
                    dateLabel.setTypeface(null, Typeface.NORMAL);
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

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout) {
            clickCellOfDays(v);
        }
        else if (v instanceof TextView) {
            clickSwitchCalendar(v);
        }
    }

    private void clickCellOfDays(View view) {
        Calendar cal = Calendar.getInstance();
        try {
            int cellDay = Integer.parseInt(((TextView) ((LinearLayout) view).getChildAt(LABEL_DATE_INDEX_AT_CELL)).getText().toString());
            cal.set(displayYear, displayMonth, cellDay);

        }
        catch (NumberFormatException e) {
            return;
        }
        if (mOnDecorateCalendarListener != null) mOnDecorateCalendarListener.onDayClick(cal.getTime());
    }

    private void clickSwitchCalendar(View view) {
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

    public void setOnDecorateCalendarListener(OnDecorateCalendarListener listener) {
        mOnDecorateCalendarListener = listener;
    }

    public abstract interface OnDecorateCalendarListener {
        void onDayClick(Date day);
        void onChangeDisplayMonth(Date date);
    }
}
