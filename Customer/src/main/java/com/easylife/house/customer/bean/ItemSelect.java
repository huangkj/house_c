package com.easylife.house.customer.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * 通用的选择列表项
 */
public class ItemSelect implements IPickerViewData, Serializable, IItemSelect {
    public String text;
    public String id;


    public ItemSelect(String text, String id) {
        this.id = id;
        this.text = text;
    }

    public ItemSelect(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ItemSelect{" +
                "text='" + text + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public String getPickerViewText() {
        return text;
    }

    public String getPickerViewId() {
        return id;
    }
}
