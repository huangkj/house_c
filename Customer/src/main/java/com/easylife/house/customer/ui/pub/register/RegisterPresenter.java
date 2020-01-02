package com.easylife.house.customer.ui.pub.register;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.widget.CheckBox;
import android.widget.EditText;

import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.ValidatorUtils;

import java.util.Map;


public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter, RequestManagerImpl {


    @Override
    public void submitName(String name, CheckBox cbAgree) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(name)) {
            mView.showTip("请输入用户名");
            return;
        }
        if (!cbAgree.isChecked()) {
            mView.showTip("请先阅读用户服务协议");
            return;
        }
        mView.showNextView(1);
    }

    @Override
    public void submitPhone(String phone) {
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
        getVerifyCode(phone);
    }

    @Override
    public void submitVerifyCode(String verifyCode) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(verifyCode)) {
            mView.showTip("请输入验证码");
            return;
        }
        mView.showNextView(3);
    }


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


        mView.valiSuc(pass);
    }

    @Override
    public void register(String name, String phone, String verifyCode, String pass) {
        register(1, name, phone, verifyCode, pass, this);
    }

    @Override
    public void exchangePassWordState(EditText edPass, boolean isShowing) {
        if (isShowing) {
            edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

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

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.submitSucc();
                    break;
                case 2:
                    if (mView.getViewIndex() == 1) {
                        mView.showNextView(2);
                    }
                    mView.getVerifyCodeSucc();
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code == null)
            return;
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.submitFail(code.msg);
                    break;
                case 2:
                    mView.getVerifyCodeFail(code.msg);
                    break;
            }
    }
}
