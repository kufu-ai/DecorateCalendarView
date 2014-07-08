package net.zaim.decoratecalendarview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import net.zaim.decoratecalendarview.DecorateCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements DecorateCalendarView.OnDecorateCalendarListener {

    private DecorateCalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarView = (DecorateCalendarView) findViewById(R.id.my_calendar);
        Bundle bundle = new Bundle();
        bundle.putString(DecorateCalendarView.BUNDLE_KEY_SELECTED_COLOR, String.valueOf(R.color.select_background));
        mCalendarView.setOnDecorateCalendarListener(this);
        mCalendarView.initCalendar(getSupportFragmentManager(), bundle);

        // Set event listener from calendar view
        //calendarView.setOnDecorateCalendarListener(this);

        // Custom calendar view
        //calendarView.setmBiginningDayOfWeek(Calendar.MONDAY);
        //calendarView.setHolidayHighlightType(DecorateCalendarView.HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND);

        // Draw calendar view
        //calendarView.set(new Date());
        //calendarView.set(2014, 3 - 1);
    }


    @Override
    public void onDayClick(Date day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Toast.makeText(this, format.format(day), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeDisplayMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Toast.makeText(this, format.format(date), Toast.LENGTH_SHORT).show();

        // Decorate cell of day
        mCalendarView.setTopTextOnDay(10, "top", Color.parseColor("#00ff00"));
        mCalendarView.setMiddleTextOnDay(10, "middle", Color.parseColor("#ff0000"));
        mCalendarView.setBottomTextOnDay(10, "bottom", Color.parseColor("#0000ff"));
    }
}
