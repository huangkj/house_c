package com.easylife.house.customer.ui.homesearch.search;

import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/30.
 */

public class SearchContract {

    interface View extends BaseView{
        void showSuccessData(List<HousesDetailBaseBean> baseBeanList);

        void showFail(String code);
    }

    interface Presenter extends BasePresenter<View>{
        void requestSearchData(SearchRequestBean requestBean,String type,String isTransparent,String pageSize);
    }
}
