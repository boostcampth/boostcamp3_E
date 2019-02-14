package com.teame.boostcamp.myapplication.ui;


import com.google.common.util.concurrent.ListenableFuture;
import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.workmanager.AlarmWork;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import io.reactivex.disposables.CompositeDisposable;

public class MyListPresenter implements MyListContract.Presenter {

    private MyListRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsListHeaderRecyclerAdapter adapter;
    private MyListContract.View view;
    private WorkManager workManager;
    private static String KEY_SELECTED="KEY_SELECTED";
    private boolean isAlarm=false;

    public MyListPresenter(MyListContract.View view, MyListRepository repository) {
        this.view = view;
        this.repository = repository;
        workManager=WorkManager.getInstance();
    }


    @Override
    public void loadMyList(GoodsListHeaderRecyclerAdapter adapter) {
        this.adapter = adapter;
        isAlarm=true;
        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            view.finishLoad(list.size());
                            adapter.initItems(list);
                            for(int i=0; i<list.size(); i++){
                                String headerKey=list.get(i).getKey();
                                view.observeWorkManager(headerKey);
                            }
                        },
                        e -> {
                            view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.e(e.getMessage());
                        })
        );
    }

    @Override
    public void alarmButtonClick(int position) {
        if(isAlarm)
            view.showDialog(position);
        else {
            //TODO: 알람 취소
        }
    }

    @Override
    public void alarmButtonPosivive(int position) {
        GoodsListHeader header=adapter.getItem(position);
        String headerKey=header.getKey();
        disposable.add(repository.getMyListItems(headerKey)
                    .subscribe(list->{
                        DLogUtil.e(list.toString());
                        String[] goodsItems=new String[list.size()];
                        for(int i=0; i<list.size(); i++){
                            goodsItems[i]=list.get(i).getName();
                        }
                        //TODO:ROOM 으로 선택된 아이템 저장
                        OneTimeWorkRequest.Builder alarmBuilder=new OneTimeWorkRequest.Builder(AlarmWork.class);
                        Data.Builder input=new Data.Builder();
                        input.putStringArray(KEY_SELECTED,goodsItems);
                        alarmBuilder.setInputData(input.build())
                                .addTag(headerKey)
                                .setInitialDelay(5, TimeUnit.SECONDS);
                        view.adapterImageChange(position,isAlarm);
                        workManager.beginUniqueWork(headerKey, ExistingWorkPolicy.KEEP,alarmBuilder.build()).enqueue();
                    }));
    }


    @Override
    public void getMyListUid(int position) {
        GoodsListHeader header = adapter.getItem(position);

        DLogUtil.d("position : " + position);
        DLogUtil.d(header.toString());

        String headerKey = header.getKey();
        view.showMyListItems(headerKey);
    }

    @Override
    public void deleteMyList(int position) {
        GoodsListHeader header = adapter.getItem(position);
        disposable.add(repository.deleteMyList(header.getKey())
                .subscribe(b -> adapter.removeItem(position)
                        , e -> DLogUtil.e(e.getMessage()))
        );
    }

    @Override
    public void onAttach() {
        DLogUtil.d("onAttach");
    }

    @Override
    public void onDetach() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
