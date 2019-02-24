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
import com.teame.boostcamp.myapplication.util.CalendarUtil;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PeriodActivity extends BaseMVPActivity<ActivityPeriodBinding,PeriodContract.Presenter> implements PeriodContract.View {

    private static final int REQUEST_CODE=1;
    private static final String PREF_ACTIVITY_FIRST_DATE="PREF_ACTIVITY_FIRST_DATE";
    private static final String PREF_ACTIVITY_LAST_DATE="PREF_ACTIVITY_LAST_DATE";

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

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy년 MM월 dd일");
        String first= SharedPreferenceUtil.getString(getApplicationContext(),PREF_ACTIVITY_FIRST_DATE);
        String last=SharedPreferenceUtil.getString(getApplicationContext(),PREF_ACTIVITY_LAST_DATE);
        Calendar firstDate = Calendar.getInstance();
        Calendar lastDate = Calendar.getInstance();

        try {
            firstDate.setTime(sdf.parse(first));
            lastDate.setTime(sdf.parse(last));
        }catch(Exception e){
            DLogUtil.e(e.toString());
        }

        initCalendar(firstDate,lastDate);
        initTextView(firstDate,lastDate);

        setSupportActionBar(binding.toolbarPeriod);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.toolbarPeriod.setNavigationIcon(R.drawable.btn_back);
        binding.toolbarPeriod.setNavigationOnClickListener(__->{
            finish();
        });
        setPresenter(new PeriodPresenter(this, new ResourceProvider(this)));
    }

    private void initCalendar(Calendar firstDate, Calendar lastDate){

        Calendar tomorrow=Calendar.getInstance();
        tomorrow.add(Calendar.YEAR,1);
        binding.mcvCalendar.state().edit()
                .setMinimumDate(CalendarDay.today())
                .setMaximumDate(CalendarDay.from(tomorrow.get(Calendar.YEAR),tomorrow.get(Calendar.MONTH),tomorrow.get(Calendar.DATE)))
                .isCacheCalendarPositionEnabled(true)
                .commit();

        binding.mcvCalendar.selectRange(CalendarDay.from(firstDate.get(Calendar.YEAR),firstDate.get(Calendar.MONTH)+1,firstDate.get(Calendar.DATE)),
                CalendarDay.from(lastDate.get(Calendar.YEAR),lastDate.get(Calendar.MONTH)+1,lastDate.get(Calendar.DATE)));
        binding.mcvCalendar.setOnRangeSelectedListener((__, dates) -> {
            presenter.calcBetweenDays(dates);
        });
    }

    private void initTextView(Calendar firstDate, Calendar lastDate){
        String str="";
        int between= CalendarUtil.daysBetween(firstDate,lastDate);
        str+=firstDate.get(Calendar.YEAR)+"년 "+Integer.toString(firstDate.get(Calendar.MONTH)+1)+"월 "+firstDate.get(Calendar.DATE)+"일 ~ ";
        str+=lastDate.get(Calendar.YEAR)+"년 "+Integer.toString(lastDate.get(Calendar.MONTH)+1)+"월 "+lastDate.get(Calendar.DATE)+"일, "+between+ "박";
        binding.tvDate.setText(str);
        binding.tvDate.setOnClickListener(__->{
            finish();
        });
    }


    @Override
    public void saveCalendar(CalendarDay start, CalendarDay end) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy년 MM월 dd일");

        Calendar first=Calendar.getInstance();
        first.set(start.getYear(),start.getMonth(),start.getDay());
        String firstString=sdf.format(first.getTime());

        Calendar last=Calendar.getInstance();
        last.set(end.getYear(),end.getMonth(),end.getDay());
        String lastString=sdf.format(last.getTime());

        SharedPreferenceUtil.putString(getApplicationContext(),PREF_ACTIVITY_FIRST_DATE,firstString);
        SharedPreferenceUtil.putString(getApplicationContext(),PREF_ACTIVITY_LAST_DATE,lastString);

        finish();
    }

    @Override
    public void setBetweenDate(String text) {
        binding.tvDate.setText(text);
    }
}
