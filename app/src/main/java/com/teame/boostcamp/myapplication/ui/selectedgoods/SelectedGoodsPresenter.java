package com.teame.boostcamp.myapplication.ui.selectedgoods;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.SelectedGoodsRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SelectedGoodsPresenter implements SelectedGoodsContract.Presenter {

    private SelectedGoodsContract.View view;
    private MyListRepository myListRepository;
    private CompositeDisposable disposable;
    private HashMap<Integer, Goods> checkedList = new HashMap<>();
    private SelectedGoodsRecyclerAdapter adapter;

    SelectedGoodsPresenter(SelectedGoodsContract.View view, MyListRepository myListRepository) {
        this.view = view;
        this.myListRepository = myListRepository;
        disposable = new CompositeDisposable();
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 쇼핑리스트를 고르는데 필요한 정보를 가져옴
     */
    @Override
    public void loadListData(SelectedGoodsRecyclerAdapter adapter, String headerUid) {
        this.adapter = adapter;
        disposable.add(myListRepository.getMyListItems(headerUid)
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            view.finishLoad(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.d(e.getMessage());
                        }
                )
        );
    }

    /**
     * 선택한 아이템의 체크여부에 따라 HashMap에 추가하거나 제거함
     */
    @Override
    public void selectItem(int position, boolean isCheck) {
        if (isCheck) {
            Goods item = adapter.getItem(position);
            checkedList.put(position, item);
        } else {
            checkedList.remove(position);
        }
    }

    @Override
    public void getDetailItemUid(int position) {
        Goods item = adapter.getItem(position);
        view.showDetailItem(item);
    }

    /**
     * 선택한 리스트 아이템을 넘겨줌
     */
    @Override
    public void getCheckedList() {
        List<Goods> saveList = new ArrayList<>(checkedList.values());
        view.saveCheckedList(saveList);
    }
}
