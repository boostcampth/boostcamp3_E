package com.teame.boostcamp.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentMyListBinding;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.selectedgoods.SelectedGoodsActivity;
import com.teame.boostcamp.myapplication.util.Constant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MyListFragment extends BaseFragment<FragmentMyListBinding, MyListContract.Presenter> implements MyListContract.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_list;
    }

    @Override
    protected MyListContract.Presenter getPresenter() {
        MyListRepository repository = MyListRepository.getInstance();
        return new MyListPresenter(this, repository);
    }

    @Deprecated
    public MyListFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
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
        initView();
        presenter.onAttach();
    }

    private void initView() {
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
        GoodsListHeaderRecyclerAdapter adapter = new GoodsListHeaderRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        presenter.loadMyList(adapter);
        adapter.setOnItemClickListener((v, position) -> presenter.getMyListUid(position));
        adapter.setOnItemAlaramListener((v, position) -> {
            presenter.alarmButtonClick(position);
            // TODO: 알람 등록 리스너
        });
        adapter.setOnItemDeleteListener((v, position) -> presenter.deleteMyList(position));
        binding.rvMyList.setLayoutManager(linearLayoutManager);
        binding.rvMyList.setAdapter(adapter);
    }

    @Override
    public void showDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(position + "번째 리스트 알림설정")
                .setPositiveButton("확인", (dialog1, which) -> {
                    presenter.alarmButtonPosivive(position);
                })
                .show();
    }

    @Override
    public void finishLoad(int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (size == Constant.LOADING_NONE_ITEM) {
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_mylist)));
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
    }

    @Override
    public void observeWorkManager(String Tag) {
        WorkManager manager=WorkManager.getInstance();
        LiveData<List<WorkInfo>> liveData=manager.getWorkInfosByTagLiveData(Tag);
        if(liveData==null)
            return;
        else{
            liveData.observe(this, workInfos -> {

            });
        }
    }

    @Override
    public void adapterImageChange(int position, boolean change) {
        RecyclerView.ViewHolder holder = binding.rvMyList.findViewHolderForAdapterPosition(position);
        ImageView imageview=holder.itemView.findViewById(R.id.iv_alarm);
        imageview.setImageDrawable(getResources().getDrawable(R.drawable.btn_alarm_mute));
    }

    @Override
    public void showMyListItems(String headerKey) {
        SelectedGoodsActivity.startActivity(getContext(), headerKey);
    }
}
