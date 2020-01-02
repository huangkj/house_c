package com.easylife.house.customer.bean.py;

import com.easylife.house.customer.bean.PYBean;
import com.easylife.house.customer.util.PinYinUtil;

import java.io.Serializable;

/**
 * Created by Mars on 2017/2/17 19:28.
 * 描述：城市
 */

public class City implements Serializable, PYBean, ISelect {
    @Override
    public boolean getSelected() {
        return selected;
    }

    /**
     * "id": "",
     * "storesName": "110000",
     * "areaid": "北京市"
     */
    public String id;
    public String cityid;
    public String city;
    public boolean selected;
    public String Pys;
    public String pinyin;


    public City() {
    }

    public City(String id, String name) {
        this.cityid = id;
        this.city = name;
        pinyin = PinYinUtil.CN2FirstSpell(name); // 根据姓名获取拼音
        Pys = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!Pys.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            Pys = "#";
        }
    }

    @Override
    public String getText() {
        return city;
    }

    @Override
    public String getId() {
        return cityid;
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
        return city;
    }
}
