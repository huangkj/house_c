package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easylife.house.customer.adapter.ExpandableItemSaleAdapter;

/**
 * Created by zgm on 2017/4/17.
 */

public class SalingBean extends AbstractExpandableItem<SalingPersonItem> implements MultiItemEntity {
    public String imgUrl;
    public String salingTitle;
    public String price;
    public String farea;
    public String discount;
    public String count;

    public String simple;

    @Override
    public int getItemType() {
        return ExpandableItemSaleAdapter.TEXT_LIST;
    }


    @Override
    public int getLevel() {
        return 0;
    }
}
