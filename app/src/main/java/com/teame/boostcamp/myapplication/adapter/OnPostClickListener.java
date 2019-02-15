package com.teame.boostcamp.myapplication.adapter;

import android.view.View;

public interface OnPostClickListener {
    void onReplyButtonClick(int i);

    void onLikeButtonClick(int i);

    void onListButtonClick(int i);

    void onMenuButtonClick(View view, int i);
}
