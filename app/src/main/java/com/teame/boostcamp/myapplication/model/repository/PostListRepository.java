package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.repository.remote.PostListRemoteDataSource;

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
}

