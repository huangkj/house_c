package com.easylife.house.customer.bean.py;

/**
 * Created by Mars on 2017/3/2 13:10.
 * 描述：
 */

public interface ISelect {
    String getText();

    String getId();

    boolean isSelect();

    void select(boolean selected);
}
