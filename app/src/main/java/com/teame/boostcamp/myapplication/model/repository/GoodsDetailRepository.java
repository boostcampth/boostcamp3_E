package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.remote.GoodsDetailRemoteDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GoodsDetailRepository implements GoodsDetailDataSource {

    private static GoodsDetailRepository INSTANCE;
    private GoodsDetailRemoteDataSource itemDetailRemoteDataSource;

    private GoodsDetailRepository() {
        this.itemDetailRemoteDataSource = GoodsDetailRemoteDataSource.getInstance();
    }

    public static GoodsDetailRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoodsDetailRepository();
        }
        return INSTANCE;
    }

    @Override
    public Single<List<Reply>> getReplyList(String itemUid) {
        return itemDetailRemoteDataSource.getReplyList(itemUid)
                .map(unsortedList -> {
                    List<Reply> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new descToDateSort());
                    return sortedList;
                });
    }

    @Override
    public Single<Reply> writeReply(String itemUid, String content, int ratio) {
        return itemDetailRemoteDataSource.writeReply(itemUid,content,ratio);
    }

    @Override
    public Single<Boolean> deleteReply(String itemUid,String replyUid) {
        return itemDetailRemoteDataSource.deleteReply(itemUid,replyUid).observeOn(AndroidSchedulers.mainThread());
    }

    // Reply데이터 내림차순 정렬 Comparator
    class descToDateSort implements Comparator<Reply> {
        @Override
        public int compare(Reply r1, Reply r2) {
            Date r1Date = r1.getWriteDate();
            Date r2Date = r2.getWriteDate();
            int compare = r1Date.compareTo(r2Date);
            if (compare < 0) {
                return 1;
            }
            return -1;
        }
    }
}
