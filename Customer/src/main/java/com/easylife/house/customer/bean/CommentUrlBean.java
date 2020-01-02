package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/3/24.
 */

public class CommentUrlBean implements Serializable{
    public int position;
    public List<CommentListBean.ReviewsBean.ReviewimgBean> beanList = new ArrayList<>();
}
