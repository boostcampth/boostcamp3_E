package com.teame.boostcamp.myapplication.ui.period;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.teame.boostcamp.myapplication.util.CalendarUtil;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Calendar;
import java.util.List;

public class PeriodPresenter implements PeriodContract.Presenter {

    private PeriodContract.View view;
    private CalendarDay firstDay;
    private CalendarDay lastDay;

    public PeriodPresenter(PeriodContract.View view) {
        this.view = view;
    }

    @Override
    public void calcBetweenDays( List<CalendarDay> list) {
        String str="";
        firstDay=list.get(0);
        lastDay=list.get(list.size()-1);
        Calendar start=Calendar.getInstance();
        start.set(firstDay.getYear(),firstDay.getMonth(),firstDay.getDay());
        DLogUtil.e(""+start.get(Calendar.YEAR)+","+start.get(Calendar.MONTH)+","+start.get(Calendar.DATE));
        Calendar end=Calendar.getInstance();
        end.set(lastDay.getYear(),lastDay.getMonth(),lastDay.getDay());
        DLogUtil.e(""+end.get(Calendar.YEAR)+","+end.get(Calendar.MONTH)+","+end.get(Calendar.DATE));

        int between= CalendarUtil.daysBetween(start,end);
        DLogUtil.e(""+between);

        str+=firstDay.getYear()+"년 "+firstDay.getMonth()+"월 "+firstDay.getDay()+"일 ~ ";
        str+=lastDay.getYear()+"년 "+lastDay.getMonth()+"월 "+lastDay.getDay()+"일, "+between+ "박";

        view.setBetweenDate(str);
    }

    @Override
    public void buttonClick() {
        view.finishActivity(firstDay,lastDay);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
