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

        calendarView.setOnDecorateCalendarListener(this);
        //calendarView.setmBiginningDayOfWeek(Calendar.MONDAY);
        //calendarView.setHolidayHighlightType(DecorateCalendarView.HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND);
        calendarView.set(new Date());
        //calendarView.set(2014, 3 - 1);

        calendarView.setTopTextOnDay(10, "●", Color.parseColor("#888888"));
        calendarView.setMiddleTextOnDay(10, "¥1,200", Color.parseColor("#ff0000"));
        calendarView.setBottomTextOnDay(10, "¥10,000", Color.parseColor("#00ff00"));
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
