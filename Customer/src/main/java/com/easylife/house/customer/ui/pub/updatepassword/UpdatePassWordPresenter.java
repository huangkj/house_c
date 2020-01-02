package com.easylife.house.customer.ui.pub.updatepassword;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.ValidatorUtils;

import java.util.Map;


public class UpdatePassWordPresenter extends BasePresenterImpl<UpdatePassWordContract.View> implements UpdatePassWordContract.Presenter, RequestManagerImpl {

    @Override
    public void updatePassWord(String pass, String passNew, String passNewAgain) {
        if (mView == null)
            return;
        if (TextUtils.isEmpty(pass)) {
            mView.showTip("请输入当前密码");
            return;
        }
        if (TextUtils.isEmpty(passNew)) {
            mView.showTip("请输入新密码");
            return;
        }

        if (pass.length() < 6 || pass.length() > 16) {
            mView.showTip("请输入6-16位字符");
            return;
        }
//        if (!ValidatorUtils.isPassword(passNew)) {
//            mView.showTip("请输入有效密码");
//            return;
//        }
        if (TextUtils.isEmpty(passNewAgain)) {
            mView.showTip("请再次输入新密码");
            return;
        }
        if (!passNew.equals(passNewAgain)) {
            mView.showTip("两次输入的密码不一致，请重新输入");
            return;
        }


        /**
         "userCode": "1234567890",
         "password1":"1234567890",
         "token": "0987654321",
         "originalPassword": "0",//原密码
         "password2":"1234567890"
         */
        String url = "customer/change/updatePassword";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache().token);
        mapData.put("phone", mDao.getCustomer().phone);
        mapData.put("originalPassword", pass);
        mapData.put("password1", passNew);
        mapData.put("password2", passNewAgain);
        mDao.manager.post(1, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null) mView.updateSuccess();
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.updateFail(code.msg);
    }
}
