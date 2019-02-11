package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Reply;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface GoodsDetailDataSource {

    Single<List<Reply>> getReplyList(String itemUid);

    Single<Reply> writeReply(String itemUid, String content, int ratio);

    Single<Boolean> deleteReply(String itemUid,String replyUid);
}
