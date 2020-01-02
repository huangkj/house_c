package com.easylife.house.customer.ui.mine.calculator.taxes;

import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class TaxesContract {
    interface View extends BaseView {
        /**
         * @param f1
         * @param f2
         * @param moneyAll      放款总额
         * @param moneyYinHua   印花税
         * @param moneyQiShui   契税
         * @param moneyQuanShu  权属登记费
         * @param moneyAllTaxes 税金总额
         */
        void initChart(float f1, float f2, String moneyAll, String moneyYinHua, String moneyQiShui, String moneyQuanShu, String moneyAllTaxes);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * @param isFirst   首次购房
         * @param area      房屋面积
         * @param priceUnit 房屋单价
         */
        void showResult(boolean isFirst, String area, String priceUnit);
    }
}
