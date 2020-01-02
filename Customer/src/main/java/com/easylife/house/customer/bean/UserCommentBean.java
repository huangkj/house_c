package com.easylife.house.customer.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/3/27.
 * 用户评价
 */

public class UserCommentBean {

    /**
     * userId : 2
     * userName : 张三李四王五赵六
     * userType : 0
     * content : 第二种测试数据不知
     * headImg : www.baidu.com
     * scorePrice : 4.5
     * scoreDistrict : 4.0
     * scoreEnvi : 5
     * scoreMating : 5
     * scoreTraffic : 5
     * projectId : 6
     * reviewType : 0
     * reviewimg : [{"url":"http://om4yv9x56.bkt.clouddn.com/FgI0mLfLiPSavRdh43Om73ESWDqK"}]
     */

    public String userId;
    public String userName;
    public String userType = "0";
    public String content;
    public String headImg;
    public String scorePrice = "0";
    public String scoreDistrict = "0";
    public String scoreEnvi = "0";
    public String scoreMating = "0";
    public String scoreTraffic = "0";
    public String projectId;
    public String AVGscores = "0";
    public String reviewType = "0";
    public List<ReviewimgBean> reviewimg = new ArrayList<>();
    
    public static class ReviewimgBean {
        /**
         * url : http://om4yv9x56.bkt.clouddn.com/FgI0mLfLiPSavRdh43Om73ESWDqK
         */

        public String url;
    }
}
