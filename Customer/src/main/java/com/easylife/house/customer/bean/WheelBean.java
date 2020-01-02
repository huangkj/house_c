package com.easylife.house.customer.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

public class WheelBean implements IPickerViewData, Serializable {

    public WheelBean() {
    }

    public WheelBean(String text) {
        super();
        this.text = text;
    }

    public WheelBean(String text, String id) {
        super();
        this.text = text;
        this.id = id;
    }

    public String text;
    public String id;

    @Override
    public String getPickerViewText() {
        return text;
    }

    //    @Override
    public String getPickerViewId() {
        return id;
    }

    @Override
    public String toString() {
        return "WheelBean [text=" + text + ", id=" + id + "]";
    }

}
