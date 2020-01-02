package com.easylife.house.customer.ui.houses.housesdetail.bookinghouse;

import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;

/**
 * Created by zgm on 2017/3/29.
 */

public class BookingHousePresenter extends BasePresenterImpl<BookingHouseContract.View> implements BookingHouseContract.Presenter, RequestManagerImpl {

    @Override
    public void requestBookHouse(String devId, String userCode, String varifyCode, String token, String realName, String phone, String lookTime) {
        mDao.bookHouse(mDao.HOUSES_DETAIL_BASE, devId, userCode, varifyCode, token, realName, phone, lookTime, this);
    }

    @Override
    public void getVerifyCode(String phone) {
        mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_LOOK_HOUSE, phone, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case ServerDao.HOUSES_DETAIL_BASE:
                    mView.showSuccess();
                    break;
                case 2:
                    mView.getVerifyCodeSucc();
                    break;
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (null != code) {
            if (mView != null) mView.showFail(code);
        }

    }
}
