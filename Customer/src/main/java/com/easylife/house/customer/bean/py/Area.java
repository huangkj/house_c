package com.easylife.house.customer.bean.py;

import com.easylife.house.customer.bean.PYBean;
import com.easylife.house.customer.util.PinYinUtil;

import java.io.Serializable;

/**
 * Created by Mars on 2017/2/17 19:28.
 * 描述：区域板块
 */

public class Area implements Serializable, PYBean, ISelect {
    @Override
    public boolean getSelected() {
        return selected;
    }

    /**
     * "id": "",
     * "areaid": "110101",
     * "area": "东城区",
     * "areaid": ""
     */
    public String areaid;
    public String area;
    public boolean selected;
    public String Pys;
    public String pinyin;

    public Area() {
    }

    public Area(String id, String name) {
        this.areaid = id;
        this.area = name;
        pinyin = PinYinUtil.CN2FirstSpell(name); // 根据姓名获取拼音
        Pys = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!Pys.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            Pys = "#";
        }
    }

    @Override
    public String getText() {
        return area;
    }

    @Override
    public String getId() {
        return areaid;
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public void select(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String getPys() {
        return Pys;
    }

    public String getPinyin() {
        return pinyin;
    }

    @Override
    public String getBeanName() {
        return area;
    }
}
