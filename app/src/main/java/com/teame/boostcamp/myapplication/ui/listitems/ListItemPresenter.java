package com.teame.boostcamp.myapplication.ui.listitems;

import com.teame.boostcamp.myapplication.adapter.ItemListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ListItemPresenter implements ListItemContract.Presenter {

    private ListItemContract.View view;
    private MyListRepository myListRepository;
    private CompositeDisposable disposable;
    private HashMap<Integer, Item> checkedList = new HashMap<>();
    private ItemListRecyclerAdapter adapter;

    ListItemPresenter(ListItemContract.View view, MyListRepository myListRepository) {
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
    public void loadListData(ItemListRecyclerAdapter adapter,String headerUid) {
        this.adapter = adapter;
        disposable.add(myListRepository.getMyListItems(headerUid)
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
            Item item = adapter.getItem(position);
            checkedList.put(position, item);
        } else {
            checkedList.remove(position);
        }
    }

    @Override
    public void getDetailItemUid(int position) {
        Item item = adapter.getItem(position);

        DLogUtil.d("position : " +position);
        DLogUtil.d(item.toString());

        String itemUid = item.getKey();
        view.showDetailItem(itemUid);
    }

    /**
     * 선택한 리스트 아이템을 넘겨줌
     */
    @Override
    public void getCheckedList() {
        List<Item> saveList = new ArrayList<>(checkedList.values());
        view.saveCheckedList(saveList);
    }
}
