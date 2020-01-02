package com.easylife.house.customer.ui.c2b.mycommission;

import android.content.Context;

import com.easylife.house.customer.bean.Commission;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;


public class MyCommissionContract {
    interface View extends BaseView {
        /**
         * 展示佣金详情
         *
         * @param commission
         */
        void showDetail(Commission commission);

        /**
         * 提现
         *
         * @param commission
         */
        void withdraw(Commission commission);

        /**
         * 显示弹窗菜单
         */
        void showMenu();
    }

    interface Presenter extends BasePresenter<View> {

        void getNetData();
    }
}
