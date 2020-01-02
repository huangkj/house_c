package com.easylife.house.customer.ui.houses.housesdetail.getdiscount;

import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

/**
 * Created by zgm on 2017/3/29.
 */

public class GetDiscountPresenter extends BasePresenterImpl<GetDiscountContract.View> implements GetDiscountContract.Presenter, RequestManagerImpl {

    @Override
    public void getVerifyCode(String phone) {
        mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_VIP, phone, this);
    }

    @Override
    public void commitGetDis(String devId, String userCode, String token, String phone, String varifyCode, String naem, String ruleId) {
        mDao.commitGetDis(1, devId, userCode, token, phone, varifyCode, naem, ruleId, this);
    }

    @Override
    public void getDisCount(String devId) {
        mDao.getHousesTopClub(3, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
            case 1:
                mView.showSuccDis("获取成功");
                break;
            case 2:
                mView.getVerifyCodeSucc();
                break;
            case 3:
                GitDisCountBean gitDisCountBean = new Gson().fromJson(response, GitDisCountBean.class);
                mView.showDisCount(gitDisCountBean);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            if (code != null) {
                mView.showFail(code.msg);
            } else {
                mView.showFail("请求失败");
            }
    }
}
