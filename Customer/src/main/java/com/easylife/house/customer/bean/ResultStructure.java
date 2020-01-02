package com.easylife.house.customer.bean;

/**
 * Created by Mars on 2017/6/15 15:56.
 * 描述：房源销控页面户型列表
 */

public class ResultStructure implements IItemSelect {
    public String createTime;
    public String structure;

    @Override
    public String getText() {
        return structure;
    }

    @Override
    public String getId() {
        return null;
    }
}
