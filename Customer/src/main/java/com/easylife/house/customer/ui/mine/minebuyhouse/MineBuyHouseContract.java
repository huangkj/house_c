package com.easylife.house.customer.ui.mine.minebuyhouse;

import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class MineBuyHouseContract {
    interface View extends BaseView {
        void submitSucc();

        void initCity(List<City> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getCityList();

        void submit(String buyCity, String budget, String loan, String structure, String intentDev);
    }
}
