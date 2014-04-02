DecorateCalendarView
====================

Simple customizable calendar view for Android.

![Screenshot](screenshot.png)


Usage
-----

### Implementation

Include `DecorateCalendarView` in your layout XML.

```xml
<net.zaim.decoratecalendarview.DecorateCalendarView
    android:id="@+id/my_calendar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

In the `onCreate` of your activity or the `onCreateView` of your fragment, set the view with valid date.

```java
DecorateCalendarView calendarView = (DecorateCalendarView) findViewById(R.id.my_calendar);
calendarView.setOnDecorateCalendarListener(this);
calendarView.set(2014, 4 - 1);
```

### Customize

The default display is as below:

* Biginning week of day: Sunday
* Holiday highlight: Date text

If you want to customize, please call `DecorateCalendarView.setBiginningDayOfWeek(int)` and `DecorateCalendarView.setHolidayHighlightType(String)`.

```java
DecorateCalendarView calendarView = (DecorateCalendarView) findViewById(R.id.my_calendar);
// Set event listener from calendar view
calendarView.setOnDecorateCalendarListener(this);
// Custom calendar view
calendarView.setmBiginningDayOfWeek(Calendar.MONDAY);
calendarView.setHolidayHighlightType(DecorateCalendarView.HOLIDAY_HIGHLIGHT_TYPE_BACKGROUND);
// Draw calendar view
calendarView.set(new Date());
//calendarView.set(2014, 3 - 1);
// Decorate cell of day
calendarView.setTopTextOnDay(10, "top", Color.parseColor("#00ff00"));
calendarView.setMiddleTextOnDay(10, "middle", Color.parseColor("#ff0000"));
calendarView.setBottomTextOnDay(10, "bottom", Color.parseColor("#0000ff"));
```

Download
-----

Download via gradle.

```groovy
repositories {
    mavenCentral()
    maven { url 'http://zaiminc.github.com/DecorateCalendarView/repository' }
}
dependecies {
    compile 'net.zaim.android:decoratecalendarview:0.1.0`
}
```

License
-----


    Copyright 2014 Zaim, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
