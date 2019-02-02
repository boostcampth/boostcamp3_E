package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.model.repository.ShoppingListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class CreateListPresenter implements CreateListContract.Presenter {

    private CreateListContract.View view;
    private ShoppingListRepository shoppingListRepository;
    private CompositeDisposable disposable;

    public CreateListPresenter(CreateListContract.View view, ShoppingListRepository shoppingListRepository) {
        this.view = view;
        this.shoppingListRepository = shoppingListRepository;
        disposable = new CompositeDisposable();

        // 테스트 셈플 서버에서 가져오고 -> 바로 userId의 myList에 저장 (로그확인)
        disposable.add(shoppingListRepository.getItemList()
                .subscribe(
                        list -> {
                            DLogUtil.d(list.toString());
                            shoppingListRepository.saveMyChoiceList(list);
                        },
                        e -> DLogUtil.d(e.getMessage())
                )
        );

    }


    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        disposable.dispose();
    }

}
