package com.teame.boostcamp.myapplication.util;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {

    public static int daysBetween(Calendar day1, Calendar day2){

        GregorianCalendar dayOne = new GregorianCalendar(day1.get(Calendar.YEAR),day1.get(Calendar.MONTH),day1.get(Calendar.DAY_OF_MONTH));
        GregorianCalendar dayTwo = new GregorianCalendar(day2.get(Calendar.YEAR),day2.get(Calendar.MONTH),day2.get(Calendar.DAY_OF_MONTH));

        DateTime startDay=new DateTime(dayOne);
        startDay.minusMonths(1);
        DateTime endDay=new DateTime(dayTwo);
        endDay.minusMonths(1);

        int diff=Days.daysBetween(startDay,endDay).getDays();

        return diff;
    }
}
