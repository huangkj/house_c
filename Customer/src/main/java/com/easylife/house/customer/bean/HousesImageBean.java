package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 */

public class HousesImageBean {
    public int type;
    public String title;
    public List<ImagePager> imageData;

    public class ImagePager{
        public String url;
    }
}
