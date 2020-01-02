package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/3/30 19:22.
 * 描述：楼盘动态
 */

public class Dynamics implements Serializable {
    public Integer id;

    public String title;

    public String content;

    public String issue;

    public String createTime;

    public Integer estateProjectId;

}
