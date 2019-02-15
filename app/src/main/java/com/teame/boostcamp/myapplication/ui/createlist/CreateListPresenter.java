package com.teame.boostcamp.myapplication.ui.createlist;

import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;
import io.reactivex.disposables.CompositeDisposable;

public class CreateListPresenter implements CreateListContract.Presenter {

    private CreateListContract.View view;
    private GoodsListRepository shoppingListRepository;
    private CompositeDisposable disposable;
    private List<Goods> checkedList = new ArrayList<>();
    private GoodsListRecyclerAdapter adapter;
    private ObservableField<String> count = new ObservableField<>("0");

    CreateListPresenter(CreateListContract.View view, GoodsListRepository shoppingListRepository) {
        this.view = view;
        this.shoppingListRepository = shoppingListRepository;
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

    @Override
    public void getDetailItemUid(int position) {
        Goods item = adapter.getItem(position);
        view.showDetailItem(item);
    }

    /**
     * 쇼핑리스트를 고르는데 필요한 정보를 가져옴
     */
    @Override
    public void loadListData(GoodsListRecyclerAdapter adapter, String nation, String city) {
        this.adapter = adapter;
        disposable.add(shoppingListRepository.getItemList(nation, city)
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            view.finishLoad(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            DLogUtil.d(e.getMessage());
                            view.finishLoad(Constant.FAIL_LOAD);
                        }
                )
        );
    }

    /**
     * 선택한 아이템의 체크여부에 따라 HashMap에 추가하거나 제거함
     */
    @Override
    public void checkedItem(int position, boolean isCheck) {
        final Goods item = adapter.getItem(position);
        if (isCheck) {
        } else {
            int targetPosition = searchItem(item);
            if (targetPosition == -1)
                return;
        }
    }

    /**
     * 선택한 리스트 아이템을 넘겨줌
     */
    @Override
    public void decideShoppingList() {
        if (checkedList.size() <= 0) {
            view.emptyCheckGoods();
        } else {
            view.goNextStep(checkedList);
        }
    }

    /**
     * ItemName을 기준으로 해당 아이템 position 반환
     */
    public int searchItem(Goods target) {
        if (checkedList.contains(target)) {
            for (int i = 0; i < checkedList.size(); i++) {
                if (TextUtils.equals(checkedList.get(i).getName(), target.getName())) {
                    return i;
                }
            }
        }
        // 아이템이 없다면
        return -1;
    }

    public ObservableField<String> getCount() {
        return count;
    }
}
