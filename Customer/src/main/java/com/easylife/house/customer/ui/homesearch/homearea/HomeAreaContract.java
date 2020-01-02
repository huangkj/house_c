package com.easylife.house.customer.ui.homesearch.homearea;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zgm on 2017/3/20.
 */

public class HomeAreaContract {

    interface View extends BaseView{
        void showChooseTime(ArrayList<Map<String, String>> chooseList);
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * 选中时间
         */
        void chooseFlowChild(Map<Integer, Integer> chooseSet);

        /**
         * 监听editext是否有值
         * @param chooseSet
         * @param mFlowViewTime
         */
//        void lisenearETisEmpty(HashSet<Integer> chooseSet, FlowViewGroup mFlowViewTime);
    }

}
