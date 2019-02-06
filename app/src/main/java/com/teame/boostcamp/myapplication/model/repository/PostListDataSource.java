package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Post;

import java.util.List;

import io.reactivex.Single;

public interface PostListDataSource {
    Single<List<Post>> getPostList();
}
