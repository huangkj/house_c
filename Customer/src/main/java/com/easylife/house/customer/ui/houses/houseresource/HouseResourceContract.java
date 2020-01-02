package com.easylife.house.customer.ui.houses.houseresource;

import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseResource;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class HouseResourceContract {
    interface View extends BaseView {
        void initDetail(HouseResource detail);

        void showFavorableDev(DevFavorable devFavorable);

        void showFavorableVip(List<FavorableVip> favorableVips);

        void showHousesDetail(HousesDetailBaseBean baseBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(String id);

        /**
         * 获取楼盘优惠
         *
         * @param devId
         */
        void getDevFavorable(String devId);

        /**
         * 获取会员优惠
         *
         * @param devId
         */
        void getVipFavorable(String devId);

        /**
         * 楼盘详情基础数据
         *
         * @param devId
         */
        void requestHousesDetail(String devId);

    }
}
