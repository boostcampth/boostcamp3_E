package com.teame.boostcamp.myapplication.model.repository;

import android.net.Uri;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.remote.PostListRemoteDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PostListRepository implements PostListDataSource {

    private static PostListRepository INSTANCE;
    private PostListRemoteDataSource postListRemoteDataSource;

    public static PostListRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostListRepository();
        }
        return INSTANCE;
    }

    private PostListRepository() {
        this.postListRemoteDataSource = PostListRemoteDataSource.getInstance();
    }


    @Override
    public Single<List<Post>> getPostList() {
        return postListRemoteDataSource.getPostList()
                .map(unsortedList -> {
                    List<Post> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new PostListRepository.PostAscToDateSort());
                    return sortedList;
                });
    }

    @Override
    public Single<Post> writePost(String content, List<Uri> uriList, GoodsListHeader header) {
        return postListRemoteDataSource.writePost(content, uriList, header);
    }

    @Override
    public Single<Reply> writePostReply(String postUid, String content) {
        return postListRemoteDataSource.writePostReply(postUid, content);
    }

    @Override
    public Single<List<Reply>> loadPostReplyList(String postUid) {
        return postListRemoteDataSource.loadPostReplyList(postUid)
                .map(unsortedList -> {
                    List<Reply> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new PostListRepository.ReplyAscToDateSort());
                    return sortedList;
                });
    }

    @Override
    public Single<Boolean> deleteReply(String postUid,String replyUid) {
        return postListRemoteDataSource.deleteReply(postUid,replyUid).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Post> adjustLike(String postUid) {
        return postListRemoteDataSource.adjustLike(postUid).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> deletePost(String postUid, List<String> imagePathList) {
        return postListRemoteDataSource.deletePost(postUid, imagePathList).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<Uri>> loadModifyImages(List<String> pathList) {
        return postListRemoteDataSource.loadModifyImages(pathList).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Post> modifyPost(Post oldPost, String content, List<Uri> uriList) {
        return postListRemoteDataSource.modifyPost(oldPost, content, uriList).observeOn(AndroidSchedulers.mainThread());
    }


    // Reply데이터 오름차순 정렬 Comparator
    class ReplyAscToDateSort implements Comparator<Reply> {
        @Override
        public int compare(Reply r1, Reply r2) {
            Date r1Date = r1.getWriteDate();
            Date r2Date = r2.getWriteDate();
            int compare = r1Date.compareTo(r2Date);
            if (compare < 0) {
                return -1;
            }
            return 1;
        }
    }

    class PostAscToDateSort implements Comparator<Post> {
        @Override
        public int compare(Post p1, Post p2) {
            Date r1Date = p1.getCreatedDate();
            Date r2Date = p2.getCreatedDate();
            int compare = r1Date.compareTo(r2Date);
            if (compare < 0) {
                return 1;
            }
            return -1;
        }
    }
}

