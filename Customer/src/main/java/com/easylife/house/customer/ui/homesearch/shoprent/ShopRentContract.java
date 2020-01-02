package com.easylife.house.customer.ui.homesearch.shoprent;

import com.easylife.house.customer.bean.AreaDoubleBean;
import com.easylife.house.customer.bean.ShopRentRequestBean;
import com.easylife.house.customer.bean.ShopRentResponseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class ShopRentContract {

    interface View extends BaseView {
        void tradingListSuccess(List<AreaDoubleBean> areas);

        void shopRentListSuccess(List<ShopRentResponseBean> shopList);

        void showFail(String code);
    }

    interface Presenter extends BasePresenter<View> {
        void selectTradingByCityId(String cityId);

        void requestShopRentList(ShopRentRequestBean bean);
    }
}
