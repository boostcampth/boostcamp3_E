package com.teame.boostcamp.myapplication.ui.period;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityPeriodBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PeriodActivity extends BaseMVPActivity<ActivityPeriodBinding,PeriodContract.Presenter> implements PeriodContract.View {

    private static final int REQUEST_CODE=1;
    private static final String EXTRA_FIRSTDAY="EXTRA_FIRSTDAY";
    private static final String EXTRA_LASTDAY="EXTRA_LASTDAY";
    @Override
    protected PeriodContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_period;
    }

    public static void startActivity(Context context){
        Intent intent=new Intent(context,PeriodActivity.class);
        ((Activity)context).startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCalendar();
        initTextView();
        setSupportActionBar(binding.toolbarPeriod);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.toolbarPeriod.setNavigationIcon(R.drawable.btn_back);
        binding.toolbarPeriod.setNavigationOnClickListener(__->{
            finish();
        });
        setPresenter(new PeriodPresenter(this));
    }

    private void initCalendar(){
        Calendar minDate=Calendar.getInstance();
        Calendar maxDate=Calendar.getInstance();
        maxDate.setTime(minDate.getTime());
        maxDate.add(Calendar.YEAR,1);
        binding.mcvCalendar.state().edit()
                .setMinimumDate(CalendarDay.today())
                .setMaximumDate(CalendarDay.from(maxDate.get(Calendar.YEAR),maxDate.get(Calendar.MONTH)+1,maxDate.get(Calendar.DATE)))
                .isCacheCalendarPositionEnabled(true)
                .commit();
        minDate.add(Calendar.DATE,1);
        DLogUtil.e(minDate.toString());
        binding.mcvCalendar.selectRange(CalendarDay.today(),CalendarDay.from(minDate.get(Calendar.YEAR),minDate.get(Calendar.MONTH)+1,minDate.get(Calendar.DATE)));
        binding.mcvCalendar.setOnRangeSelectedListener((__, dates) -> {
            presenter.calcBetweenDays(dates);
        });
    }

    private void initTextView(){
        String str="";
        Calendar today=Calendar.getInstance();
        str+=today.get(Calendar.YEAR)+"년 "+Integer.toString(today.get(Calendar.MONTH)+1)+"월 "+today.get(Calendar.DATE)+"일 ~ ";
        today.add(Calendar.DATE,1);
        str+=today.get(Calendar.YEAR)+"년 "+Integer.toString(today.get(Calendar.MONTH)+1)+"월 "+today.get(Calendar.DATE)+"일, 1박";
        binding.tvDate.setText(str);
        binding.tvDate.setOnClickListener(__->{
            presenter.buttonClick();
        });
    }

    @Override
    public void finishActivity(CalendarDay start, CalendarDay end) {
        Intent intent=new Intent();
        intent.putExtra(EXTRA_FIRSTDAY,start);
        intent.putExtra(EXTRA_LASTDAY,end);
        setResult(REQUEST_CODE,intent);
        finish();
    }

    @Override
    public void setBetweenDate(String text) {
        binding.tvDate.setText(text);
    }
}
