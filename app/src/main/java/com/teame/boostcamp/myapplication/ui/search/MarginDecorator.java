package com.teame.boostcamp.myapplication.ui.search;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginDecorator extends RecyclerView.ItemDecoration {
    private int margin;
    public MarginDecorator(int margin){
        this.margin=margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top=margin;
        outRect.bottom=margin;
        outRect.left=margin;
        outRect.right=margin;
    }
}
