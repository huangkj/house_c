package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class IntegralBean implements MultiItemEntity {


    public String fakeString = "fff";

    public int type = 1;
    /**
     * behavior_name : 注册
     * state : 0
     * time : 1538381799000
     * behavior : 0
     * balance_point : 20
     * point : 10
     * m_id : 518
     */

    private String behavior_name;


    private String xh;
    private String date;
    private int state;
    private long time;
    private int behavior;
    private int balance_point;
    private int point;
    private int m_id;

    @Override
    public int getItemType() {
        return type;
    }

    public String getBehavior_name() {
        return behavior_name;
    }

    public void setBehavior_name(String behavior_name) {
        this.behavior_name = behavior_name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBehavior() {
        return behavior;
    }

    public void setBehavior(int behavior) {
        this.behavior = behavior;
    }

    public int getBalance_point() {
        return balance_point;
    }

    public void setBalance_point(int balance_point) {
        this.balance_point = balance_point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
