package com.teame.boostcamp.myapplication.model.repository.local.preference;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.util.List;

public interface CartPreferenceHelper {

    void saveGoodsCartList(List<Goods> list);

    List<Goods>  getGoodsCartList();

    void saveListHeader(GoodsListHeader list);

    GoodsListHeader getListHeader();

    void clearPreferenceData();

}
