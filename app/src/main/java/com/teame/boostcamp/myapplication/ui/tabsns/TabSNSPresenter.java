package com.teame.boostcamp.myapplication.ui.tabsns;

import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class TabSNSPresenter implements TabSNSContract.Presenter {
    private TabSNSContract.View view;
    private PostListRepository postListRepository;
    private CompositeDisposable disposable;
    private PostListAdapter adapter;

    public TabSNSPresenter(TabSNSContract.View view, PostListRepository postListRepository) {
        this.view = view;
        this.postListRepository = postListRepository;
        disposable = new CompositeDisposable();
    }

    @Override
    public void onAttach() {

    }
    @Override
    public void onDetach() {
        this.view = null;
        disposable.dispose();
    }

    @Override
    public void loadPostData(PostListAdapter adapter) {
        this.adapter = adapter;
        disposable.add(postListRepository.getPostList()
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                        },
                        e -> DLogUtil.d(e.getMessage())
                )
        );
    }
}
