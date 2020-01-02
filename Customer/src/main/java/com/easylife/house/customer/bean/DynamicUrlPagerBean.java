package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class DynamicUrlPagerBean implements Serializable{
    public int position;
    public List<String> urlList = new ArrayList<>();
}
