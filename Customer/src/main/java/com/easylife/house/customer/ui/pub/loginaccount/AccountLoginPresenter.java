package com.easylife.house.customer.ui.pub.loginaccount;


import android.text.TextUtils;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.ValidatorUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class AccountLoginPresenter extends BasePresenterImpl<AccountLoginContract.View> implements AccountLoginContract.Presenter, RequestManagerImpl {


    private String easemobUserName;
    private String easemobPassword;

    @Override
    public void loginByNormal(String phone, String pass) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(phone)) {
            mView.showTip("请输入手机号");
            return;
        }
        if (!ValidatorUtils.isMobile(phone)) {
            mView.showTip("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            mView.showTip("请输入密码");
            return;
        }
        mDao.login(1, ServerDao.TYPE_LOGIN_NORMAL, phone, null, pass, this);
    }

    @Override
    public void getUserInfo() {
        LoginResult result = mDao.getLoginCache();
        if (result == null)
            return;
        mDao.getUserInfo(0, result.token, result.userCode, this);
    }

    @Override
    public void loginByOther(String name, String tid, String headImg, String type) {
        mDao.loginByOther(2, name, tid, headImg, type, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1://登录成功
                final LoginResult result = new Gson().fromJson(response, LoginResult.class);
                if (result != null) {
                    mDao.saveLoginCache(response);
                }
                mView.loginSucc();
                    mDao.getUserInfo(0, result.token, result.userCode, AccountLoginPresenter.this);
                break;
            case 0://获取用户信息
                Customer customer = new Gson().fromJson(response, Customer.class);
                if (customer == null)
                    return;
                mDao.saveCustomer(customer);
                mView.userInfoSuc();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));

                break;
            case 2://三方登录成功
                if (!TextUtils.isEmpty(response)) {
                    mDao.saveLoginCache(response);
                    LoginResult thirdLoginresult = new Gson().fromJson(response, LoginResult.class);
                    Customer customer2 = new Customer();
                    customer2.userCode = thirdLoginresult.userCode;
                    customer2.token = thirdLoginresult.token;
                    mDao.saveCustomer(customer2);

                        //     获取用户信息
                        mDao.getUserInfo(3, this);
                }

                break;
            case 3://三方登录获取用户信息
                Customer customer3 = new Gson().fromJson(response, Customer.class);
                if (customer3 != null && !TextUtils.isEmpty(customer3.phone)) {
                    mView.loginThirdSucc();
                } else {
                    //跳转绑定手机号
                    mView.startBindPhone(easemobUserName, easemobPassword);
                }
                break;


        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        mView.showTip(code.msg);
    }


}
