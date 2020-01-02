package com.easylife.house.customer.ui.homefragment.homemine;

import android.util.Log;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeMinePresenter extends BasePresenterImpl<HomeMineContract.View> implements HomeMineContract.Presenter, RequestManagerImpl {

    @Override
    public boolean isLogin() {
        return mDao.isLogin();
    }

    @Override
    public void getUserInfoCache() {
        if (mView != null) mView.initUserInfo(mDao.getCustomer());
    }

    @Override
    public void getUserInfo() {
        getUserInfoCache();// 退出登录后用来更新用户信息
        mDao.getUserInfo(0, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        Customer customer = new Gson().fromJson(response, Customer.class);

        if (customer == null)
            return;
        mDao.saveCustomer(customer);
        if (mView != null) mView.initUserInfo(customer);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code == null)
            return;
        if (mView != null) mView.showTip(code.msg);
    }
}
