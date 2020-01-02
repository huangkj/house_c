package com.easylife.house.customer.ui.pub.loginbyverifycode;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.ValidatorUtils;
import com.google.gson.Gson;

import java.util.Map;


public class LoginByVerifyCodePresenter extends BasePresenterImpl<LoginByVerifyCodeContract.View> implements LoginByVerifyCodeContract.Presenter, RequestManagerImpl {

    @Override
    public void getVerifyCode(String phone) {
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
        mView.showLoading();
        mDao.getVerifyCode(1, ServerDao.TYPE_VERIFYCODE_LOGIN, phone, this);
    }

    @Override
    public void loginByVerifyCode(String phone, String verifyCode, boolean isAgree) {
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
        if (TextUtils.isEmpty(verifyCode)) {
            mView.showTip("请输入验证码");
            return;
        }

        if (!isAgree) {
            mView.showTip("请先阅读用户服务协议");
            return;
        }

        mView.showLoading();
        mDao.login(2, ServerDao.TYPE_LOGIN_VARIFYCODE, phone, verifyCode, null, this);
    }

    @Override
    public void getUserInfo() {
        getNetData(this);
    }

    @Override
    public void loginByOther(String name, String tid, String headImg, String type) {
        mDao.loginByOther(3, name, tid, headImg, type, this);

    }

    private void getNetData(RequestManagerImpl requestManager) {
        LoginResult result = mDao.getLoginCache();
        if (result == null)
            return;
        String url = "customer/myInfoList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("token", result.token);
        mapData.put("userCode", result.userCode);
        mDao.manager.post(4, url, mapData, requestManager, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null) {
            mView.cancelLoading();
            switch (requestType) {
                case 4: //三方登录 获取用户信息成功，登录环信
//                    Customer customer = new Gson().fromJson(response, Customer.class);
//                    if (customer == null)
//                        return;
//                    mDao.saveCustomer(customer);
//                    mView.userInfoSuc();
                    Customer customer3 = new Gson().fromJson(response, Customer.class);
                    if (customer3 != null && !TextUtils.isEmpty(customer3.phone)) {
                        mView.loginThirdSucc();
                    } else {
                        //跳转绑定手机号
                        mView.startBindPhone( );
                    }
                    break;
                case 1:
                    mView.getVerifyCodeSucc();
                    break;
                case 2:
                    LoginResult result = new Gson().fromJson(response, LoginResult.class);
                    if (result != null) {
                        mDao.saveLoginCache(response);
                    }
                    mView.loginSucc();
                    break;
                case 3://第三方登录
                    if (!TextUtils.isEmpty(response)) {
                        mDao.saveLoginCache(response);
                        LoginResult thirdLoginresult = new Gson().fromJson(response, LoginResult.class);
                        Customer customer2 = new Customer();
                        customer2.userCode = thirdLoginresult.userCode;
                        customer2.token = thirdLoginresult.token;
                        mDao.saveCustomer(customer2);

                            //     获取用户信息
                            mDao.getUserInfo(4, this);
                    }
                    break;


            }
        }


    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code == null)
            return;
        if (mView != null) {
            mView.cancelLoading();
            switch (requestType) {
                case 1:
                    mView.getVerifyCodeFail(code.msg);
                    break;
                case 2:
                    mView.loginFail(code.msg);
                    break;
            }
        }
    }



}