package net.zaim.decoratecalendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

public class DecorateCalendarFragment extends Fragment {

    public static final String BUNDLE_KEY_DISPLAY_TIME = "display_time";
    public static final String BUNDLE_KEY_SELECTED_COLOR = "selected_color";

    private DecorateCalendarView.OnDecorateCalendarListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        DecorateCalendarView calendarView = (DecorateCalendarView) view.findViewById(R.id.decorate_calendar_view);
        calendarView.setPagerMode(true);
        if (mListener != null) calendarView.setOnDecorateCalendarListener(mListener);

        Bundle bundle = getArguments();
        long displayTime = bundle.getLong(BUNDLE_KEY_DISPLAY_TIME, 0);
        calendarView.set(new Date(displayTime));
        int color = bundle.getInt(BUNDLE_KEY_SELECTED_COLOR);
        calendarView.setCurrentDayColor(getResources().getColor(color));

        return view;
    }
}
