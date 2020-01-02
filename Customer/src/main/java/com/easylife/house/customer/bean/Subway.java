package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/4/10 09:53.
 * 描述：城市地铁列表
 */

public class Subway implements Serializable, IItemSelect {
    /**
     * "rwName": "七号线",
     * "cityid": "110100",
     * "id": 1
     */
    public String id;
    public String rwName;
    public String cityid;

    @Override
    public String getText() {
        return rwName;
    }

    @Override
    public String getId() {
        return id;
    }
}
