package com.easylife.house.customer.bean;

/**
 * Created by zgm on 2017/4/11.
 */

public class HouseSubNumBean {
    /**
     * count1 : 151
     * count2 : 150
     * count0 : 151
     * //0是变价   //1是开盘  //2是动态
     */


    /**
     * 开盘
     */
    private int count1;
    /**
     * 动态
     */
    private int count2;
    /**
     * 变价
     */
    private int count0;

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public int getCount0() {
        return count0;
    }

    public void setCount0(int count0) {
        this.count0 = count0;
    }
}
