package com.easylife.house.customer.ui.payment.refundinfoupdate;

import android.content.Context;
import android.text.TextUtils;

import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.ParamRefundInfo;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import java.util.List;


public class RefundInfoUpdatePresenter extends BasePresenterImpl<RefundInfoUpdateContract.View> implements RefundInfoUpdateContract.Presenter, RequestManagerImpl {
    @Override
    public void uploadImage(List<ImageBean> imgList) {
        mDao.updateImg(mDao.HOUSES_DETAIL_TOP_IMG, imgList, this);
    }

    @Override
    public void submit(String orderCode, ParamRefundInfo param) {
        mDao.applyRefundTwo(1, param, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case ServerDao.HOUSES_DETAIL_TOP_IMG:
                    ImageResult resultList = new Gson().fromJson(response, ImageResult.class);
                    if (resultList == null) {
                        mView.showUploadSuccess(null);
                    } else {
                        mView.showUploadSuccess(resultList.data);
                    }
                    break;
                case 1:
                    // 提交请求
                    mView.showSubmitSuccess();
                    break;
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            switch (requestType) {
                case ServerDao.HOUSES_DETAIL_TOP_IMG:
                    mView.showUploadFail();
                    break;
                case 2:
                    if (code != null && !TextUtils.isEmpty(code.msg))
                        mView.showTip(code.msg);
                    break;
            }
    }
}
