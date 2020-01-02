package com.easylife.house.customer.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by zgm on 2017/3/23.
 */

public class PagerAllImageBean extends SectionEntity<HousesTopImgBean.ImgBean> {
    public int num;
    public PagerAllImageBean(boolean isHeader, String header,int num) {
        super(isHeader, header);
        this.num = num;
    }

    public PagerAllImageBean(HousesTopImgBean.ImgBean imgBean) {
        super(imgBean);
    }
}
