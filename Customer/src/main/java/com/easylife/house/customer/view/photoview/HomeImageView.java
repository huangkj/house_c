package com.easylife.house.customer.view.photoview;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * Created by zgm on 2017/3/17.
 */

public class HomeImageView {

    /**
     *创建自定义宽高图片
     * @param context
     * @param width
     * @param height
     * @return
     */
    public ImageView createImageView(Context context,int width,int height){
        ImageView view = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
        return view;
    }
}
