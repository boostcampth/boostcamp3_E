package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Reply;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.Subject;

public interface ItemDetailDataSource {

    Single<List<Reply>> getReplyList();

    Subject<Reply> writeReply(String itemId, String content, int ratio);
}
