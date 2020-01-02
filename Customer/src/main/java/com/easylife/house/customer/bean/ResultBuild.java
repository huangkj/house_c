package com.easylife.house.customer.bean;

/**
 * Created by Mars on 2017/6/15 15:56.
 * 描述：房源销控页面楼栋列表
 */

public class ResultBuild implements IItemSelect {
    public String buildId;
    public String buildName;

    @Override
    public String getText() {
        return buildName;
    }

    @Override
    public String getId() {
        return buildId;
    }
}
