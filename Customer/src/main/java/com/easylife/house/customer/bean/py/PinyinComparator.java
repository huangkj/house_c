package com.easylife.house.customer.bean.py;

import com.easylife.house.customer.bean.PYBean;
import com.easylife.house.customer.util.LogOut;

import java.util.Comparator;

/**
 * 根据拼音排序
 *
 * @author heiyue
 * @Desc
 * @Email heiyue623@126.com
 * @Date 2015-9-27
 */
public class PinyinComparator implements Comparator<PYBean> {

    @Override
    public int compare(PYBean o1, PYBean o2) {
        String py1 = o1.getPinyin();
        String py2 = o2.getPinyin();
        // 判断是否为空""  
        if (isEmpty(py1) && isEmpty(py2))
            return 0;
        if (isEmpty(py1))
            return -1;
        if (isEmpty(py2))
            return 1;
        String str1 = "";
        String str2 = "";
        try {
            str1 = ((o1.getPinyin()).toUpperCase()).substring(0, 1);
            str2 = ((o2.getPinyin()).toUpperCase()).substring(0, 1);
        } catch (Exception e) {
            LogOut.d("Exception:", "某个str为\" \" 空");
        }
        return str1.compareTo(str2);
    }

    private boolean isEmpty(String str) {
        return "".equals(str.trim());
    }
}  