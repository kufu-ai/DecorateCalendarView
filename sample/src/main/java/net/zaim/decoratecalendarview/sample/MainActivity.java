package net.zaim.decoratecalendarview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import net.zaim.decoratecalendarview.DecorateCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements DecorateCalendarView.OnDecorateCalendarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DecorateCalendarView calendarView = (DecorateCalendarView) findViewById(R.id.my_calendar);

        // Set event listener from calendar view
        calendarView.setOnDecorateCalendarListener(this);

        // Custom calendar view
        //calendarView.setmBiginningDayOfWeek(Calendar.MONDAY);
        //calendarView.setHolidayHighlightType(DecorateCalendarView.HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND);

        // Draw calendar view
        calendarView.set(new Date());
        //calendarView.set(2014, 3 - 1);

        // Decorate cell of day
        calendarView.setTopTextOnDay(10, "top", Color.parseColor("#00ff00"));
        calendarView.setMiddleTextOnDay(10, "middle", Color.parseColor("#ff0000"));
        calendarView.setBottomTextOnDay(10, "bottom", Color.parseColor("#0000ff"));

        // Set tapped cell's color
        calendarView.setCurrentDayColor(getResources().getColor(R.color.select_background));
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
    }
}
