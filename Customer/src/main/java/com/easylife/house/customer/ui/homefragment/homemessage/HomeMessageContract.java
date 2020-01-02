package com.easylife.house.customer.ui.homefragment.homemessage;

import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class HomeMessageContract {
    interface View extends BaseView {
        void initDataHeadLine(List<MsgHeadLine> data);

        void initDataDevSub(List<MsgDevSub> data);

        void showTip();
    }

    interface Presenter extends BasePresenter<View> {
        void getDataHeadLine();

        void getDataDevSub();
    }
}
