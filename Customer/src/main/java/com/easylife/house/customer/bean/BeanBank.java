package com.easylife.house.customer.bean;

import android.text.TextUtils;

import com.easylife.house.customer.util.PinYinUtil;

import java.io.Serializable;

public class BeanBank implements Serializable, PYBean {


    public String bankName;
    public String bankCode; // 银行简写
    public String pinyin;
    public String bankInitials;

    public BeanBank(String id, String name) {
        this.bankCode = id;
        this.bankName = name;
        pinyin = PinYinUtil.CN2Spell(name); // 根据姓名获取拼音
        if (TextUtils.isEmpty(bankInitials)) {
            bankInitials = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
            if (!bankInitials.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                bankInitials = "#";
            }
        }
    }

    @Override
    public String getId() {
        return bankCode;
    }

    @Override
    public String getPys() {
        if (TextUtils.isEmpty(bankInitials)) {
            bankInitials = getPinyin().substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
            if (!bankInitials.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                bankInitials = "#";
            }
        }
        return bankInitials;
    }

    public String getPinyin() {
        if (TextUtils.isEmpty(pinyin))
            pinyin = PinYinUtil.CN2Spell(bankName);
        return pinyin;
    }

    @Override
    public String getBeanName() {
        return bankName;
    }

    @Override
    public boolean getSelected() {
        return false;
    }
}
