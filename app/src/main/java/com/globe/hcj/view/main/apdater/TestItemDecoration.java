package com.globe.hcj.view.main.apdater;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by baeminsu on 13/10/2018.
 */

public class TestItemDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;

    public TestItemDecoration(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    public TestItemDecoration(Context context, @DimenRes int itemOffsetId) {
        this.itemOffset = context.getResources().getDimensionPixelOffset(itemOffsetId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        outRect.left = itemOffset;
        outRect.right = itemOffset;
        outRect.bottom = itemOffset;
        outRect.top = itemOffset;
    }
}