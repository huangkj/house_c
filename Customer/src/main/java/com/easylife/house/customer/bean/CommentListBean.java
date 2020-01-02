package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 */

public class CommentListBean implements Serializable{

    /**
     * createTime : 2017-02-23 10:10:10
     * reviewType : 2
     * id : 2
     * userType : 1
     * userName : xioaoooa
     * userId : 1
     * content : fsfdsafdsafdsad
     */

    public List<Strscores> strscores;
    public List<ReviewsBean> reviews;
    public String AVGScore;
    public String count;

    public class Strscores implements Serializable{
        public String scoreDistrict;
        public String scoreEnvi;
        public String scorePrice;
        public String scoreMating;
        public String scoreTraffic;
    }

    public class ReviewsBean implements Serializable{
        public long createTime;
//        public String createTime;
        public String aVGscores;
        public String reviewType;
        public int id;
        public String userType;
        public String userName;
        public String userId;
        public String content;
        public String headImg;

        public List<ReviewimgBean> reviewimg;

        public class ReviewimgBean implements Serializable{
            public String name;
            public String id;
            public String url;
        }
    }
}
