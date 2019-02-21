package com.teame.boostcamp.myapplication.ui.period;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.Calendar;
import java.util.List;

public interface PeriodContract {
    interface View extends BaseView<Presenter> {
        void setBetweenDate(String text);
        void finishActivity(CalendarDay start, CalendarDay end);
    }

    interface Presenter extends BasePresenter {
        void calcBetweenDays(List<CalendarDay> list);
        void buttonClick();
    }
}
