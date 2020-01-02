package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zgm on 2017/4/17.
 */

public class MultipleSalingItem implements MultiItemEntity {

    public static final int TEXT_LIST = 1;
    public static final int TYPE_PERSON = 2;
    public int itemType;
    public int spanSize;
    public String content;

    public MultipleSalingItem(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
