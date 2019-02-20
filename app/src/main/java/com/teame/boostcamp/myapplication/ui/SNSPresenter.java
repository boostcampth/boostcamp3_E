package com.teame.boostcamp.myapplication.ui;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class SNSPresenter implements SNSContract.Presenter {
    private static final String PREF_SNS_SEARCH="PREF_SNS_SEARCH";

    private SNSContract.View view;
    private PostListRepository postListRepository;
    private CompositeDisposable disposable;
    private PostListAdapter adapter;
    private PostListAdapter searchAdapter;
    private SnsSearchRecyclerAdapter exAdapter;
    private ResourceProvider provider;

    public SNSPresenter(SNSContract.View view, ResourceProvider provider) {
        this.view = view;
        this.postListRepository = PostListRepository.getInstance();
        this.provider = provider;
        disposable = new CompositeDisposable();

    }

    @Override
    public void onAttach() {

    }
    @Override
    public void onDetach() {
        this.view = null;
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void createList(SnsSearchRecyclerAdapter exAdapter) {
        this.exAdapter = exAdapter;
        String exJson= SharedPreferenceUtil.getString(provider.getApplicationContext(), PREF_SNS_SEARCH);
        Gson gson=new Gson();
        if(exJson!=null){
            this.exAdapter.initItems(gson.fromJson(exJson,new TypeToken<ArrayList<String>>(){}.getType()));
        }
    }
    @Override
    public void loadPostData(PostListAdapter adapter) {
        this.adapter = adapter;
        disposable.add(postListRepository.getPostList()
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                            view.stopRefreshIcon();
                        },
                        e -> DLogUtil.d(e.getMessage())
                )
        );
    }

    public void loadSearchPost(PostListAdapter searchAdapter, String tag){
        this.searchAdapter = searchAdapter;
        disposable.add(postListRepository.searchPostList(tag)
                .subscribe(
                        list -> {
                            DLogUtil.e(list.toString());
                            searchAdapter.initItems(list);
                            if(!exAdapter.getItemList().contains(tag)){
                                exAdapter.addItem(0, tag);
                            }
                            view.succeedSearch(searchAdapter);
                        },
                        e -> DLogUtil.d(e.getMessage())
                ));
    }

    @Override
    public void saveToJson() {
        Gson gson=new Gson();
        String toJson = gson.toJson(exAdapter.getItemList());
        SharedPreferenceUtil.putString(provider.getApplicationContext(), PREF_SNS_SEARCH, toJson);
    }

}
