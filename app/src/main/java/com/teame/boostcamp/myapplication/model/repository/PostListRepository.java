package com.teame.boostcamp.myapplication.model.repository;

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
        return postListRemoteDataSource.getPostList().observeOn(AndroidSchedulers.mainThread());
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
                    Collections.sort(sortedList, new PostListRepository.ascToDateSort());
                    return sortedList;
                });
    }

    // Reply데이터 오름차순 정렬 Comparator
    class ascToDateSort implements Comparator<Reply> {
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
}

