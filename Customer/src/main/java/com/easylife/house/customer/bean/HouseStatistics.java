package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/6/15 16:04.
 * 描述：房源统计数据
 */

public class HouseStatistics implements Serializable {
    /**
     * "forsale": 0,待售
     * "notforsale": 216,不可售
     * "lock": 0,已锁定
     * "availablefoesale": 0,可售
     * "inthesaleof": 0在售
     * "soldout": 0,已售
     */

    public String forsale;
    public String notforsale;
    public String lock;
    public String availablefoesale;
    public String soldout;
    public String inthesaleof;
}
