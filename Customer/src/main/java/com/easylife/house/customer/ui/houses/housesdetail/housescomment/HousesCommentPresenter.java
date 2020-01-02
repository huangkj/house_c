package com.easylife.house.customer.ui.houses.housesdetail.housescomment;

import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

/**
 * Created by zgm on 2017/3/24.
 * 楼盘评价
 */

public class HousesCommentPresenter extends BasePresenterImpl<HousesCommentContract.View> implements HousesCommentContract.Presenter, RequestManagerImpl {

    @Override
    public void loadMoreData(String projectId, String pageSize) {
        mDao.getComment(mDao.HOUSES_DETAIL_HOUSES_COMMENT, projectId, pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        CommentListBean commentListBean = new Gson().fromJson(response, CommentListBean.class);
        if (commentListBean != null)
            if (mView != null) mView.showMoreData(commentListBean);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code != null)
            if (mView != null) mView.showFail(code.msg);
    }
}
