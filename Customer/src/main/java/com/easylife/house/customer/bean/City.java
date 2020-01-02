package com.easylife.house.customer.bean;

/**
 * Created by Mars on 2017/3/21 20:25.
 * 描述： 城市
 */

public class City implements IItemSelect {
    /**
     * "id": "",
     * "areaid": "110105",
     * "area": "朝阳区",
     * "cityid": ""
     */
    public String id;
    public String city;
    public String cityid;
    public String tableId;
    public String tableDevId;

    @Override
    public String getText() {
        return city;
    }

    @Override
    public String getId() {
        return cityid;
    }
}
