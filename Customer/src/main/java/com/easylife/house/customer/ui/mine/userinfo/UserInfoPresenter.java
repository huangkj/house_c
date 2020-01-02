package com.easylife.house.customer.ui.mine.userinfo;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import java.util.Map;

public class UserInfoPresenter extends BasePresenterImpl<UserInfoContract.View> implements UserInfoContract.Presenter, RequestManagerImpl {

    @Override
    public void getCacheUserInfo() {
        mDao.getUserInfo(3, this);
    }

    @Override
    public void uploadUserHeader(String imgPath) {
        mDao.updateImgSingle(1, imgPath, imgPath, this);
    }

    @Override
    public void saveUserInfo(Customer customer) {
        String url = "customer/updateMyInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().token);
        mapData.put("headImg", customer.headimg);
        mapData.put("name", customer.username);
        mapData.put("sex", customer.sex);
        mapData.put("age", customer.age);
        mDao.manager.post(2, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    ImageResult.DataBean data = mDao.analyzeUploadSingleImageResult(response);
                    if (data == null || TextUtils.isEmpty(data.url)) {
                        mView.showTip("图片上传失败");
                        return;
                    }
                    mView.updateUserHeaderSucc(data.url);
                    break;
                case 2:
                    mView.saveSucc();
                    break;
                case 3:
                    Customer c = new Gson().fromJson(response, Customer.class);
                    mView.initUserInfo(c);
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            mView.saveFail();
    }
}
