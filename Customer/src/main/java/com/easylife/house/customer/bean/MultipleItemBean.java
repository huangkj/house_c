package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/3/16.
 */

public class MultipleItemBean implements MultiItemEntity {
    public static final int IMG_10 = 10;
    public static final int IMG_EMPTY = -1;
    private int itemType;
    public List<HomeBean> beanList;

    public MultipleItemBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

}
