package com.easylife.house.customer.ui.homesearch.housefilter;

import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HouseFilterContract {
    interface View extends BaseView {
        void initAreaData(List<ItemSelect> data);

        void initSubwayData(List<ItemSelect> data);

        void submitParams(SearchRequestBean searchRequestBean);

        void reset();

    }

    interface Presenter extends BasePresenter<View> {

        void getAreaData(String city, String cityId);

        void getSubwayData(String cityID);

        void submit(String city, String addressDistrict, String subway, Map<Integer,Integer> houseTypeSet, Map<Integer,Integer> budgetSet, Map<Integer,Integer> devRoomInfoSet, Map<Integer,Integer> houseAreaIdSet, String sort, String type);

    }
}
