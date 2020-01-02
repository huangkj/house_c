package com.easylife.house.customer.ui.pub.registeraccount;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

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

import java.util.Map;


public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter, RequestManagerImpl {


    private String phone;
    private String pass;




    @Override
    public void submit(String pass) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(pass)) {
            mView.showTip("请输入密码");
            return;
        }

        if (pass.length() < 6 || pass.length() > 16) {
            mView.showTip("请输入6-16位字符");
            return;
        }

//        if(!ValidatorUtils.isPassword(pass)){
//            mView.showTip("必须包含一个大写字母");
//            return;
//        }


    }

    @Override
    public void register(String name, String phone, String verifyCode, String pass, boolean isAgree) {


        if (TextUtils.isEmpty(name)) {
            mView.showTip("请输入姓名");
            return;
        }
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

        if (TextUtils.isEmpty(pass)) {
            mView.showTip("请输入6～16字符密码");
            return;
        }

        if (pass.length() < 6 || pass.length() > 16) {
            mView.showTip("密码不符合规则");
            return;
        }
        if (!isAgree) {
            mView.showTip("请先阅读用户服务协议");
            return;
        }

        this.phone = phone;
        this.pass = pass;
        register(1, name, phone, verifyCode, pass, this);
    }

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
        mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_REGISTER, phone, this);
    }


    private void register(int requestType, String username, String phone, String varifyCode, String password, RequestManagerImpl requestManager) {
        String url = "customer/reg";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("username", username);
        mapData.put("phone", phone);
        mapData.put("varifyCode", varifyCode);
        mapData.put("password", password);
        mDao.manager.post(requestType, url, mapData, requestManager, false);
    }

    public void getUserInfo() {
        LoginResult result = mDao.getLoginCache();
        if (result == null)
            return;
        mDao.getUserInfo(4, result.token, result.userCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1: //注册完毕  登录
                    mDao.pointRegister(phone);
                    mDao.login(3, ServerDao.TYPE_LOGIN_NORMAL, phone, null, pass, this);
                    break;
                case 2://获取验证码
                    mView.getVerifyCodeSucc();
                    break;

                case 3://登录接口成功
                    LoginResult result = new Gson().fromJson(response, LoginResult.class);
                    if (result != null) {
                        mDao.saveLoginCache(response);
                    }
                        mView.cancelLoading();
                        getUserInfo();
                    break;

                case 4://获取用户信息 登录成功
                    Customer customer = new Gson().fromJson(response, Customer.class);
                    if (customer == null)
                        return;
                    mDao.saveCustomer(customer);
                    mView.userInfoSuc();
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        Log.d(getClass().getSimpleName(), code.msg);
        if (code == null)
            return;
        if (mView != null) {
            mView.showTip(code.msg);
        }
    }



}
