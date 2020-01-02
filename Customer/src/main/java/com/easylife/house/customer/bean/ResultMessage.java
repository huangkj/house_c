package com.easylife.house.customer.bean;


import java.util.List;

/**
 * 描述：消息列表
 */

public class ResultMessage {
    public List<PushMsgBean> pushMessageOuterList;// 首页外层列表
    public List<PushMsgBean> pushMessageList; // 首页消息列表点击跳转的子消息列表
}
