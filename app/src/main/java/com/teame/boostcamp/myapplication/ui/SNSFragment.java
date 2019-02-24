package com.teame.boostcamp.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSnsBinding;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.modifypost.ModifyPostActivity;
import com.teame.boostcamp.myapplication.ui.mypost.MyPostActivity;
import com.teame.boostcamp.myapplication.ui.othershoppinglist.OtherShoppingListActivity;
import com.teame.boostcamp.myapplication.ui.postreply.PostReplyActivity;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SNSFragment extends BaseFragment<FragmentSnsBinding, SNSContract.Presenter> implements SNSContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sns;
    }

    @Override
    protected SNSContract.Presenter getPresenter() {
        return new SNSPresenter(this, new ResourceProvider(getContext()));
    }

    @Override
    public void setPresenter(SNSContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Deprecated
    public SNSFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static SNSFragment newInstance() {
        SNSFragment fragment = new SNSFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.srlSns.setRefreshing(true);
        initView();
    }

    private void initView() {
        PostListAdapter adapter = new PostListAdapter();
        presenter.setAdapter(adapter);
        adapter.setOnReplyClickListener((__, position) -> PostReplyActivity.startActivity(getContext(), adapter.getItem(position).getKey()));
        adapter.setOnShowListClickListener((__, position) -> OtherShoppingListActivity.startActivity(getContext(), adapter.getItem(position).getUid(), adapter.getItem(position).getHeader().getKey(), adapter.getItem(position).getWriter()));
        adapter.setOnMenuClickListener((v, position) -> {
            PopupMenu menu = new PopupMenu(getContext(), v);
            getActivity().getMenuInflater().inflate(R.menu.menu_post, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                if (item.toString().equals("수정")) {
                    ModifyPostActivity.startActivity(getContext(), adapter.getItem(position));
                } else {
                    presenter.deletePost(adapter.getItem(position).getKey(), adapter.getItem(position).getImagePathList(), position);
                }
                return false;
            });
            menu.show();
        });
        adapter.setOnLikeClickListener((v, position) -> { presenter.adjustLike(adapter.getItem(position).getKey(), position); });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        SnsSearchRecyclerAdapter exAdapter = new SnsSearchRecyclerAdapter();
        exAdapter.setOnItemClickListener((v, position) -> binding.svSns.setQuery(exAdapter.itemList.get(position), true));
        exAdapter.setOnDeleteListener((v, position) -> {
            exAdapter.removeItem(position);
        });
        binding.ivSnsMypost.setOnClickListener(__ -> MyPostActivity.startActivity(getContext()));
        binding.rvSnsSearch.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false));

        binding.rvSnsSearch.setAdapter(exAdapter);
        presenter.createList(exAdapter);

        ImageView icon = binding.svSns.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(Color.BLACK);
        ImageView iconClose = binding.svSns.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconClose.setColorFilter(Color.BLACK);


        // 서치뷰 활성화(클릭되었을떄)
        binding.svSns.setOnSearchClickListener(v -> {
            binding.ivSnsBack.setVisibility(View.VISIBLE); // 뒤로가기버튼 보여줌
            binding.rvSnsSearch.setVisibility(View.VISIBLE); // 최근 검색 뷰 보여줌
            binding.ivSnsMypost.setVisibility(View.GONE);
            binding.ivBuyThis.setVisibility(View.GONE);
            binding.clNoPost.setVisibility(View.INVISIBLE);
        });
        // 시처뷰 클로즈 되었을떄
        binding.svSns.setOnCloseListener((() -> {
            binding.ivSnsBack.setVisibility(View.INVISIBLE);    // 다 안보여줌
            binding.rvSnsSearch.setVisibility(View.INVISIBLE);
            binding.rvSnsSearchPost.setVisibility(View.INVISIBLE);
            binding.clNoSearch.setVisibility(View.INVISIBLE);
            binding.ivSnsMypost.setVisibility(View.VISIBLE);
            binding.ivBuyThis.setVisibility(View.VISIBLE);
            if (adapter.itemList.size() == 0) {
                binding.clNoPost.setVisibility(View.VISIBLE);
            }

            return false;
        }));
        // 뒤로가기 버튼 눌렸을떄
        binding.ivSnsBack.setOnClickListener(v -> {
            binding.svSns.onActionViewCollapsed(); // 서치뷰 비활성화 및 onClose 호출
        });

        // 백 프레스드 위한 메서드
        binding.svSns.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                binding.svSns.setIconified(true);
            } else {
                binding.rvSnsSearchPost.setVisibility(View.INVISIBLE);
                binding.clNoSearch.setVisibility(View.INVISIBLE);
                binding.rvSnsSearch.setVisibility(View.VISIBLE);
            }
        });
        binding.clNoPost.setOnClickListener(__ -> AddPostActivity.startActivity(getContext()));
        binding.svSns.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PostListAdapter searchAdapter = new PostListAdapter();
                searchAdapter.setOnReplyClickListener((__, position) -> PostReplyActivity.startActivity(getContext(), searchAdapter.getItem(position).getKey()));
                searchAdapter.setOnShowListClickListener((__, position) -> OtherShoppingListActivity.startActivity(getContext(), searchAdapter.getItem(position).getUid(), searchAdapter.getItem(position).getHeader().getKey(), searchAdapter.getItem(position).getWriter()));
                searchAdapter.setOnMenuClickListener((v, position) -> {
                    PopupMenu menu = new PopupMenu(getContext(), v);
                    getActivity().getMenuInflater().inflate(R.menu.menu_post, menu.getMenu());
                    menu.setOnMenuItemClickListener(item -> {
                        if (item.toString().equals("수정")) {
                            ModifyPostActivity.startActivity(getContext(), searchAdapter.getItem(position));
                        } else {
                            presenter.deleteSearchPost(searchAdapter.getItem(position).getKey(), searchAdapter.getItem(position).getImagePathList(), position);
                        }
                        return false;
                    });
                    menu.show();
                });
                searchAdapter.setOnLikeClickListener((v, position) -> { presenter.searchAdjustLike(searchAdapter.getItem(position).getKey(), position); });

                presenter.loadSearchPost(searchAdapter, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (binding.rvSnsSearch.getVisibility() != View.VISIBLE) {
                    binding.rvSnsSearchPost.setVisibility(View.INVISIBLE);
                    binding.clNoSearch.setVisibility(View.INVISIBLE);
                    binding.rvSnsSearch.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        binding.svSns.setQueryHint("해시태그를 이용해 검색해보세요!");
        binding.rvSns.setLayoutManager(linearLayoutManager);
        binding.rvSns.setAdapter(adapter);
        binding.rvSnsSearch.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false));
        binding.rvSnsSearchPost.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false));
        binding.fabAddPost.setOnClickListener(__ -> AddPostActivity.startActivity(getContext()));
        presenter.loadPostData(adapter);
        binding.rvSns.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.fabAddPost.hide();
                } else {
                    binding.fabAddPost.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.srlSns.setOnRefreshListener(() -> initView());
    }

    @Override
    public void stopRefreshIcon(int size) {
        binding.srlSns.setRefreshing(false);
        if (size == 0) {
            binding.clNoPost.setVisibility(View.VISIBLE);
        } else {
            binding.clNoPost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        presenter.saveToJson();
        super.onPause();
    }

    @Override
    public void succeedSearch(PostListAdapter searchAdapter) {
        if (searchAdapter.itemList.size() == 0) {
            binding.clNoSearch.setVisibility(View.VISIBLE);
        } else {
            binding.clNoSearch.setVisibility(View.INVISIBLE);
        }
        binding.rvSnsSearchPost.setAdapter(searchAdapter);
        binding.rvSnsSearch.setVisibility(View.INVISIBLE);
        binding.rvSnsSearchPost.setVisibility(View.VISIBLE);
    }

    @Override
    public void succeedDelete(int size) {
        if(size == 0 ){
            binding.clNoPost.setVisibility(View.VISIBLE);
        }
        else{
            binding.clNoPost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void succeedSearchDelete(int size) {
        if(size == 0){
            binding.clNoSearch.setVisibility(View.VISIBLE);
        }
        else{
            binding.clNoSearch.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


}


