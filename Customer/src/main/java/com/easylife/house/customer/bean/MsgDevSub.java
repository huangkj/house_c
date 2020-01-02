package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/4/1 17:05.
 * 描述：楼盘订阅
 */

public class MsgDevSub implements Serializable {
    /**
     * "createTime": 1491449076,
     * "logo": "1313131313.ico",
     * "id": "58e5b4f40cf2684deecbe0a8",
     * "text": "222222",
     * "title": "111",
     * "userId": "1",
     * "logoUrl": "http://img4.imgtn.bdimg.com/it/u=2437762035,2994278153&fm=23&gp=0.jpg",
     * "url": "http://baidu.com"
     */

    public String createTime;
    public String logo;
    public String id;
    public String devId;
    public String userId;
    public String text;
    public String title;
    public String logoUrl;
    public String url;
}
