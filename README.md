DecorateCalendarView
====================

Simple customizable calendar view for Android.

![Screenshot](screenshot.png)


Usage
-----

```xml
<net.zaim.decoratecalendarview.DecorateCalendarView
    android:id="@+id/my_calendar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

```java
DecorateCalendarView calendarView = (DecorateCalendarView) findViewById(R.id.my_calendar);
calendarView.setOnDecorateCalendarListener(this);
calendarView.set(2014, 3 - 1);
calendarView.setSymbolLabel(10, "●");
calendarView.setPaymentLabel(10, "¥1,200");
calendarView.setIncomeLabel(10, "¥10,000");
```

Download
-----

```groovy
repositories {
    mavenCentral()
    maven { url 'http://zaiminc.github.com/DecorateCalendarView/repository' }
}
dependecies {
    compile 'net.zaim:decoratecalendarview:(version)`
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
