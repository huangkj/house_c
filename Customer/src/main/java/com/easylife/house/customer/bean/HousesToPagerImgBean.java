package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zgm on 2017/3/23.
 * 大图浏览相册
 */

public class HousesToPagerImgBean implements Serializable{
    public List<HousesTopImgBean.ImgBean> imgBeanList;
    public List<HousesTopImgBean> allBeanList;
    public List<PageTitleBean> titleList;
    public int pagetImgSize;
    public int currentPosition;
    public Map<Integer, Integer> currentMap;
}
