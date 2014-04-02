package net.zaim.decoratecalendarview.tests;

import android.test.InstrumentationTestCase;

import net.zaim.decoratecalendarview.DecorateCalendarView;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isAssignableFrom;

public class DecorateCalendarViewTest extends InstrumentationTestCase {

    public void testSimpleCheckText() throws Exception {
        DecorateCalendarView view = new DecorateCalendarView(getInstrumentation().getTargetContext());
        assertTrue(isAssignableFrom(DecorateCalendarView.class).matches(view));
    }
}
