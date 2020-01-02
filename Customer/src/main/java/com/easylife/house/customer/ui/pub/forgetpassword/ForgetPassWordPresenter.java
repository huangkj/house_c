package com.easylife.house.customer.ui.pub.forgetpassword;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.ValidatorUtils;

import java.util.Map;

public class ForgetPassWordPresenter extends BasePresenterImpl<ForgetPassWordContract.View> implements ForgetPassWordContract.Presenter, RequestManagerImpl {

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
        mDao.getVerifyCode(1, ServerDao.TYPE_VERIFYCODE_UPDATE_PASSWORD, phone, this);
    }

    @Override
    public void submit(String phone, String verifyCode, String pass, String passAgain) {
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
        if (TextUtils.isEmpty(pass)) {
            mView.showTip("请输入密码");
            return;
        }

        if (pass.length() < 6 || pass.length() > 16) {
            mView.showTip("请输入6-16位字符");
            return;
        }
//        if (!ValidatorUtils.isPassword(pass)) {
//            mView.showTip("请输入有效密码");
//            return;
//        }
        if (TextUtils.isEmpty(passAgain)) {
            mView.showTip("请输入验证码");
            return;
        }
        if (!pass.equals(passAgain)) {
            mView.showTip("两次输入的密码不一致，请重新输入");
            return;
        }

        mView.updatePsw(phone, verifyCode, pass);

    }

    @Override
    public void update(String phone, String verifyCode, String pass) {
        updatePassWord(2, phone, verifyCode, pass, this);
    }

    /**
     * 修改密码。忘记密码
     *
     * @param requestType
     * @param phone
     * @param varifyCode
     * @param password
     * @param requestManager
     */
    private void updatePassWord(int requestType, String phone, String varifyCode, String password, RequestManagerImpl requestManager) {
        String url = "customer/change/password";
        Map<String, Object> mapData = new ArrayMap<>();
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
                    mView.getVerifyCodeSucc();
                    break;
                case 2:
                    mView.submitSucc();
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
                    mView.getVerifyCodeFail(code.msg);
                    break;
                case 2:
                    mView.submitFail(code.msg);
                    break;
            }
    }
}
