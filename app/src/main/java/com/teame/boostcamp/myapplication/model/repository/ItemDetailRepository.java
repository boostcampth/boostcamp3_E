package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.remote.ItemDetailRemoteDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.Subject;

public class ItemDetailRepository implements ItemDetailDataSource {

    private static ItemDetailRepository INSTANCE;
    private ItemDetailRemoteDataSource itemDetailRemoteDataSource;

    private ItemDetailRepository() {
        this.itemDetailRemoteDataSource = ItemDetailRemoteDataSource.getInstance();
    }

    public static ItemDetailRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemDetailRepository();
        }
        return INSTANCE;
    }

    @Override
    public Single<List<Reply>> getReplyList() {
        return itemDetailRemoteDataSource.getReplyList()
                .map(unsortedList -> {
                    List<Reply> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new descToDateSort());
                    return sortedList;
                });
    }

    @Override
    public Subject<Reply> writeReply(String itemId, String content, int ratio) {
        return itemDetailRemoteDataSource.writeReply(itemId,content,ratio);
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
