package com.easylife.house.customer.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hkj on 2017/12/4.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int edgeSpacing;
    private int verticalSpacing;
    private int horizonSpacing;
    private int headviewCount = 1;

    public GridSpacingItemDecoration(int spanCount, int edgeSpacing, int verticalSpacing, int horizonSpacing, int headviewCount) {
        this.spanCount = spanCount;
        this.edgeSpacing = edgeSpacing;
        this.verticalSpacing = verticalSpacing;
        this.horizonSpacing = horizonSpacing;
        this.headviewCount = headviewCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


        int position = parent.getChildAdapterPosition(view); // item position
        if (position == 0 && headviewCount > 0) { //pass headview
            return;
        }
        if (position == parent.getAdapter().getItemCount() - 1) { //pass footview
            return;
        }
        position = position - headviewCount;


        if (position % spanCount == 1) {
            //每行第一个
            outRect.left = edgeSpacing;
            outRect.right = horizonSpacing / 2;
        } else if (position % spanCount == 0) {
            //每行最后一个
            outRect.right = edgeSpacing;
            outRect.left = horizonSpacing / 2;
        } else {
            //中间的
            outRect.right = horizonSpacing / 2;
            outRect.left = horizonSpacing / 2;
        }
        outRect.bottom = verticalSpacing;
    }
}