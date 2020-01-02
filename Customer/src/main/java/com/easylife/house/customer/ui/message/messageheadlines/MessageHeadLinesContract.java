package com.easylife.house.customer.ui.message.messageheadlines;

import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class MessageHeadLinesContract {
    interface View extends BaseView {
        void initData(List<MsgHeadLine> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(int page);

        void getCacheData();

        void requestCount(String id);
    }
}
