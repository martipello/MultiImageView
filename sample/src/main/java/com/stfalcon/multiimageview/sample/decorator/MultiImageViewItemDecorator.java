package com.stfalcon.multiimageview.sample.decorator;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultiImageViewItemDecorator extends RecyclerView.ItemDecoration {

    private final int spacing;

    public MultiImageViewItemDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);

        if (position % 2 != 0) {
            outRect.right = spacing;
        }

        outRect.left = spacing;
        outRect.bottom = spacing;

    }
}
