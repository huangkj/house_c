package com.easylife.house.customer.ui.mine.calculator.business;

import android.text.TextUtils;

import com.easylife.house.customer.bean.LoanResult;
import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.LoanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class BusinessPresenter extends BasePresenterImpl<BusinessContract.View> implements BusinessContract.Presenter, RequestManagerImpl {
    @Override
    public void getRate() {
        mDao.selectRate(1, "1", "0", this);
    }

    @Override
    public void showResult(boolean isAccrual, String money, String year, double rate) {
        if (mView == null)
            return;
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
        LoanResult result = LoanUtil.getLoanResult(isAccrual, money, year, rate + "");
        if (result == null) {
            mView.showTip("计算错误，请检查参数");
            return;
        }
        mView.initChart(result.rateAccrual, result.rateCapital, result.moneyCapitalStr + "万元",
                result.moneyAccrualStr + "万元", result.paymentMonthStr + "元/月", result.paymentOtherStr + "元");
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ResultRate> list = new Gson().fromJson(response, new TypeToken<List<ResultRate>>() {
        }.getType());
        if (mView != null) mView.initNetData(list);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
