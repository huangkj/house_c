package com.easylife.house.customer.ui.homesearch.wherebuy;

import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.SearchHistoryBean;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/20.
 */

public class WhereBuyContract {

    interface View extends BaseView{

        void showSearchHistory(List<SearchHistoryBean> historyList);
        void initCity(List<City> data);

    }

    interface Presenter extends BasePresenter<View>{
        void getCityList();
        void getSearchHistory(LocalDao localDao);
        void saveSearchHistory(LocalDao localDao, SearchHistoryBean historyBean);
    }
}
