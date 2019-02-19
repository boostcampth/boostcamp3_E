package com.teame.boostcamp.myapplication.model.repository.local.preference;

import java.util.HashMap;

public interface CheckListPreferencesHelper {
    void saveMyListCheck(HashMap<String, Boolean> list);

    HashMap<String, Boolean> getMyListCheck();

}
