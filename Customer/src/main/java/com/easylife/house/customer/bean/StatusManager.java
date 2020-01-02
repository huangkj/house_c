package com.easylife.house.customer.bean;

/**
 * Created by Mars on 2017/6/6 14:07.
 * 描述：房源状态管理
 * 0 在售
 * 1 可售
 * 2 待售
 * 3 已售
 * 4 不可售
 * 5 已锁定
 */

public interface StatusManager {
    /**
     * 空白，不显示
     */
    int STATUS_NULL = -1;

    /**
     * 有效：在售
     */
    int STATUS_AVAILABLE = 0;
    /**
     * 可售
     */
    int STATUS_SOLD_CAN = 1;
    /**
     * 待售
     */
    int STATUS_SOLD_WAIT = 2;
    /**
     * 已售：不可操作
     */
    int STATUS_SOLD_HAD = 3;
    /**
     * 不可售
     */
    int STATUS_SOLD_FORBID = 4;
    /**
     * 已锁定
     */
    int STATUS_LOCKED = 5;

}
