package com.easylife.house.customer.ui.message.messagehouses;

import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class MessageHousesContract {
    interface View extends BaseView {
        void initData(List<MsgDevSub> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(int page);
    }
}
