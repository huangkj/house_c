package com.easylife.house.customer.ui.mine.calculator.group;

import android.text.TextUtils;

import com.easylife.house.customer.bean.LoanResult;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.LoanUtil;


public class GroupPresenter extends BasePresenterImpl<GroupContract.View> implements GroupContract.Presenter {

    @Override
    public void showResult(boolean isAccrual, String money, String year, double rate,
                           String moneyBusiness, String yearBusiness, double rateBusiness) {
        if (mView == null) {
            return;
        }
        if (TextUtils.isEmpty(money)) {
            mView.showTip("请输入贷款金额");
            return;
        }
        if (TextUtils.isEmpty(year)) {
            mView.showTip("请选择按揭年数");
            return;
        }
        if (rate == 0) {
            mView.showTip("请选择利率");
            return;
        }
        LoanResult result = LoanUtil.getLoanGroupResult(isAccrual, money, year, rate + "", moneyBusiness, yearBusiness, rateBusiness + "");
        if (result == null) {
            mView.showTip("计算错误，请检查参数");
            return;
        }
        mView.initChart(result.rateAccrual, result.rateCapital, result.moneyCapitalStr + "万元",
                result.moneyAccrualStr + "万元", result.paymentMonthStr + "元/月", result.paymentOtherStr + "元");
    }
}
