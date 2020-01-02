package com.easylife.house.customer.ui.homesearch.opentime;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/3/20.
 */

public class OpenTimeContract {

    interface View extends BaseView{
        void showChooseTime(String date);
    }

    interface Presenter extends BasePresenter<View>{
        void chooseOpenTime(String choosePosition);
    }

}
