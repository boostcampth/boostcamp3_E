package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class CreateListPresenter implements CreateListContract.Presenter {

    private CreateListContract.View view;
    private GoodsListRepository shoppingListRepository;
    private CompositeDisposable disposable;
    private HashMap<Integer, Goods> checkedList = new HashMap<>();
    private GoodsListRecyclerAdapter adapter;

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
        if(disposable.isDisposed()){
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
    public void loadListData(GoodsListRecyclerAdapter adapter,String nation, String city) {
        this.adapter = adapter;
        disposable.add(shoppingListRepository.getItemList(nation,city)
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                        },
                        e -> DLogUtil.d(e.getMessage())
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
    public void addItem(String itemName) {
        Goods item = new Goods();
        item.setName(itemName);
        int hitPosition = adapter.searchItem(item);

        if (hitPosition == -1) {
            adapter.addItem(item);
        }

        view.showAddedGoods(hitPosition);
    }

    /**
     * 선택한 리스트 아이템을 넘겨줌
     */
    @Override
    public void decideShoppingList() {
        List<Goods> saveList = new ArrayList<>(checkedList.values());
        if(saveList.size()<=0){
            view.emptyCheckGoods();
        }else{
            view.goNextStep(saveList);
        }
    }
}
