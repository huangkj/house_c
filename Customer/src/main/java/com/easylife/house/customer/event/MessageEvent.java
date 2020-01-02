package com.easylife.house.customer.event;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;


/**
 * 创建者：zgm
 * 类描述： 通用事件
 */
public class MessageEvent {
    private String TAG = "MessageEvent";
    public static final int UPDATE_SEARCH_DATA = 110;
    public static final int UPDATE_HOUSES_TYPE_DETAIL = 111;
    public static final int HOUSES_DETAIL_PAGER = 120;
    public static final int HOUSES_INDEXT_COLLECTION = 130;//楼盘详情页面收藏 首页刷新
    public static final int HOUSES_INDEXT_COLLECTION_LOGIN = 131;//楼盘详情页面收藏 首页刷新
    public static final int UPDATE_MESSAGE_HOUSE = 140;
    public static final int UPDATE_MESSAGE_HEADLINE = 190;
    public static final int LOGIN_STATE_CHANGE = 150;
    public static final int LOOK_HOUSE_CHANGE = 160;
    public static final int LOOK_HOUSE_CHANGE_SEE = 170;
    public static final int HOUSE_DETAIL_COMMENT = 180;//楼盘评价成功刷新
    public static final int IM_SAVEUSERINFO = 190;//获取IM个人信息
    public static final int IM_SAVEUSERINFO_NAME = 191;//IM号被顶，清空我的页面个人姓名
    public static final int UPDATE_HOUSE_DETAIL_COMMENT = 192;//更新楼盘评价接口
    public static final int UPDATE_ORDER_STATUS = 193;//更新订单状态，更新订单列表分页位置，更新订单列表分页数据
    public static final int RESTART_LOCATION = 194;//刷新定位信息
    public static final int SEARCH_HOUSE_FINISH = 195;//搜索楼盘关闭原楼盘列表页
    public static final int REFRESH_BANK_CARD = 196;//通知刷新银行卡列表
    public static final int REFRESH_ID_CARD_NUM = 197;//身份证绑定成功，通知刷新账户设置页面
    public static final int SET_TRADE_PASSWORD_SUCC = 198;//设置交易密码成功
    public static final int RESTART_ACTIVITY_GAME = 199;//设置交易密码成功
    public static final int RESTART_HOUSE_COLLECT_STATE = 202;//楼盘收藏状态刷新
    public static final int CHECK_TRADE_PASSWORD = 203; // 验证交易密码成功

    public static final int FORCE_LOGIN_OUT = 300; // 单点登录触发，强制下线通知
    public static final int PUSH_MSG = 310; // 消息列表通知
    public static final int PUSH_MSG_REFRESH = 320; // 消息列表刷新通知

    public int MsgType;
    public String msg;
    public Object obj;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean flag;
    public SwipeRefreshLayout swipeRefreshLayout;

    public int getMsgI() {
        return msgI;
    }

    public void setMsgI(int msgI) {
        this.msgI = msgI;
    }


    public int msgI;

    public MessageEvent() {
    }

    public MessageEvent(int msgType) {
        this.MsgType = msgType;
    }

    public MessageEvent(int msgType, Object obj) {
        Log.d(TAG, "msgType : " + msgType);
        this.MsgType = msgType;
        this.obj = obj;
    }

    public MessageEvent(int msgType, SwipeRefreshLayout swipeRefreshLayout) {
        this.MsgType = msgType;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
}
