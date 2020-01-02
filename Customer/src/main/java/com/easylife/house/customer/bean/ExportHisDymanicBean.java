package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by zgm on 2017/4/5.
 * TA的动态
 */

public class ExportHisDymanicBean {

    /**
     * contentUrl : [{"url":"http://om4yv9x56.bkt.clouddn.com/Fg3_jSe8qWE4nefWcKumX28oCXfB"},{"url":"http://om4yv9x56.bkt.clouddn.com/Fg4JGA0FAlM4Hfs2SHAl1x-XGS_I"}]
     * createTime : 1643604371000
     * id : 62
     * title : 测试
     * content : 把携家带口
     */

    public long createTime;
    public int id;
    public String title;
    public String content;
    public List<ContentUrlBean> contentUrl;

    public static class ContentUrlBean {
        /**
         * url : http://om4yv9x56.bkt.clouddn.com/Fg3_jSe8qWE4nefWcKumX28oCXfB
         */

        public String url;
    }
}
