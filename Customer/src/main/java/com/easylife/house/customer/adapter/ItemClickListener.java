package com.easylife.house.customer.adapter;

/**
 * Created by Mars on 2017/6/29 20:20.
 * 描述：外抛单击接口
 */

public interface ItemClickListener<T> {
    public void itemClick(int viewId, int actionType, T data);
}
