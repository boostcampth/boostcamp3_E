package com.teame.boostcamp.myapplication.ui.period;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.teame.boostcamp.myapplication.util.CalendarUtil;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PeriodPresenter implements PeriodContract.Presenter {

    private PeriodContract.View view;
    private CalendarDay firstDay;
    private CalendarDay lastDay;
    private ResourceProvider provider;
    private static final String PREF_ACTIVITY_FIRST_DATE="PREF_ACTIVITY_FIRST_DATE";
    private static final String PREF_ACTIVITY_LAST_DATE="PREF_ACTIVITY_LAST_DATE";

    public PeriodPresenter(PeriodContract.View view, ResourceProvider provider) {
        this.view = view;
        this.provider=provider;
    }

    @Override
    public void calcBetweenDays( List<CalendarDay> list) {
        String str="";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy년 MM월 dd일");

        firstDay=list.get(0);
        lastDay=list.get(list.size()-1);
        Calendar start=Calendar.getInstance();
        start.set(firstDay.getYear(),firstDay.getMonth(),firstDay.getDay());
        start.add(Calendar.MONTH,-1);
        Calendar end=Calendar.getInstance();
        end.set(lastDay.getYear(),lastDay.getMonth(),lastDay.getDay());
        end.add(Calendar.MONTH,-1);

        DLogUtil.e(start.toString());
        DLogUtil.e(end.toString());

        int between= CalendarUtil.daysBetween(start,end);

        String startString=sdf.format(start.getTime());
        String endString=sdf.format(end.getTime());

        str+=startString+" ~ "+ endString+", "+between+"박";

        SharedPreferenceUtil.putString(provider.getApplicationContext(),PREF_ACTIVITY_FIRST_DATE,startString);
        SharedPreferenceUtil.putString(provider.getApplicationContext(),PREF_ACTIVITY_LAST_DATE,endString);

        view.setBetweenDate(str);
    }

    @Override
    public void buttonClick() {
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
