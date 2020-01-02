package com.easylife.house.customer.base;

import android.view.View;

/**
 * Created by zgm on 2017/8/30/030.
 */

public interface BaseNewView<T> {

    /**
     * 设置presenter对象
     *
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 初始化views
     *
     * @param view
     */
    void initViews(View view);
}
