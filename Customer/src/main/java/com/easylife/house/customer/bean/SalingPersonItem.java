package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easylife.house.customer.adapter.ExpandableItemSaleAdapter;

/**
 * Created by zgm on 2017/4/17.
 */

public class SalingPersonItem implements MultiItemEntity {
    public String title;
    public String area;
    public String price;
    public String id;
    public int sum;

    @Override
    public int getItemType() {
        return ExpandableItemSaleAdapter.TYPE_PERSON;
    }
}
