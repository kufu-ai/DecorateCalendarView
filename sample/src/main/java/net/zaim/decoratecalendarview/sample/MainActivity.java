package net.zaim.decoratecalendarview.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        calendarView.set(2014, 3 - 1);

        calendarView.setSymbolLabel(10, "●");
        calendarView.setPaymentLabel(10, "¥1,200");
        calendarView.setIncomeLabel(10, "¥10,000");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDayClick(Date day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Toast.makeText(this, format.format(day), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeDisplayMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Toast.makeText(this, format.format(date), Toast.LENGTH_SHORT).show();
    }
}
