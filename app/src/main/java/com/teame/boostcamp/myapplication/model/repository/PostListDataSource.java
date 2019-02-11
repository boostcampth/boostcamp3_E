package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;

import java.util.List;

import io.reactivex.Single;

public interface PostListDataSource {
    Single<List<Post>> getPostList();
    Single<Reply> writePostReply(String postUid, String content);
    Single<List<Reply>> loadPostReplyList(String postUid);
}
