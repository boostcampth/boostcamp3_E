package com.teame.boostcamp.myapplication.model.repository;

import android.net.Uri;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;

import java.util.List;

import io.reactivex.Single;

public interface PostListDataSource {
    Single<List<Post>> getPostList();

    Single<Post> writePost(String content, List<Uri> uriList, GoodsListHeader header);

    Single<Reply> writePostReply(String postUid, String content);

    Single<List<Reply>> loadPostReplyList(String postUid);

    Single<Boolean> deleteReply(String postUid, String replyUid);

    Single<Post> adjustLike(String postUid);

    Single<Boolean> deletePost(String postUid, List<String> imagePathList);
}
