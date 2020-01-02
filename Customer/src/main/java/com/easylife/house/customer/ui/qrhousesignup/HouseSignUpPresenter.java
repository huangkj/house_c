package com.easylife.house.customer.ui.qrhousesignup;

import com.easylife.house.customer.bean.CommitSignInfoBean;
import com.easylife.house.customer.bean.HouseSignBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

/**
 * Created by zgm on 2017/4/21.
 */

public class HouseSignUpPresenter extends BasePresenterImpl<HouseSignUpContract.View> implements HouseSignUpContract.Presenter, RequestManagerImpl {

    @Override
    public void getVerifyCode(String phone) {
        mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_REGISTER, phone, this);
    }

    @Override
    public void requestSignInfo(String userCode, String devId, String token, String brokerUserCode) {
        mDao.requestSignInfo(1, userCode, devId, token, brokerUserCode, this);
    }

    @Override
    public void commitSignInfo(CommitSignInfoBean commitSignInfoBean) {
        mDao.commitSignInfo(3, commitSignInfoBean, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    HouseSignBean houseSignBean = new Gson().fromJson(response, HouseSignBean.class);
                    mView.showSign(houseSignBean);
                    break;
                case 2:
                    mView.getVerifyCodeSucc();
                    break;
                case 3:
                    mView.showCommitSuc("报名成功");
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            if (code != null) {
                mView.showFail(code.msg);
            } else {
                mView.showFail("订阅失败");
            }
    }
}
