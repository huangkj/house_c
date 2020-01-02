package com.easylife.house.customer.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 项目名称：gaocaisheng3.0
 * 类描述：设置RecyclerView项目之间的间距
 * 创建人：lh
 * 创建时间：2017/2/9 11:31
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int orientation = -1;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, int orientation) {
        this.space = space;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildPosition(view) != 0) {
            if (orientation == LinearLayout.VERTICAL) {
                outRect.top = space;
            } else if (orientation == LinearLayout.HORIZONTAL) {
                outRect.left = space;
            } else {
                outRect.top = space;

            }
        }
    }
}
