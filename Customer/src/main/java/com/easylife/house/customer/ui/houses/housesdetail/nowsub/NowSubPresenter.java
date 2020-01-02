package com.easylife.house.customer.ui.houses.housesdetail.nowsub;

import android.text.TextUtils;

import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
import com.easylife.house.customer.bean.HouseSubServiceBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/4/7.
 */

public class NowSubPresenter extends BasePresenterImpl<NowSubContract.View> implements NowSubContract.Presenter, RequestManagerImpl {
    @Override
    public void getSub(HouseInfoSubBean houseInfoSubBean) {
        mDao.devHouseSub(1, houseInfoSubBean, this);
    }

    @Override
    public void getVerifyCode(String phone) {
        mDao.getVerifyCode(2, ServerDao.TYPE_VERIFYCODE_HOUSE, phone, this);
    }

    @Override
    public void getSubList(String userCode, String token, String projectId, String devId) {
        mDao.getHouseSubList(3, userCode, token, projectId, devId, this);
    }

    @Override
    public void getHousesSubNum(String devId, String projectId) {
        mDao.getHouseSubNum(4, devId, projectId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.showSuccess();
                    break;
                case 2:
                    mView.getVerifyCodeSucc();
                    break;
                case 3:
                    List<String> mySubList = new ArrayList<>();
                    HouseSubServiceBean houseSubServiceBean = new Gson().fromJson(response, HouseSubServiceBean.class);
                    if (houseSubServiceBean != null && !TextUtils.isEmpty(houseSubServiceBean.tag)) {
                        String[] split = houseSubServiceBean.tag.split(",");
                        for (String tag : split) {
                            mySubList.add(tag);
                        }
                    }
                    mView.showSubList(mySubList);
                    break;
                case 4:
                    HouseSubNumBean numBean = new Gson().fromJson(response, HouseSubNumBean.class);
                    mView.showHouseSubNum(numBean);
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
