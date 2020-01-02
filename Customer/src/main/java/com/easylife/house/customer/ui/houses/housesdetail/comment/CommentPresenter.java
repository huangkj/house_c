package com.easylife.house.customer.ui.houses.housesdetail.comment;

import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class CommentPresenter extends BasePresenterImpl<CommentContract.View> implements CommentContract.Presenter, RequestManagerImpl {


    @Override
    public void uploadImage(List<ImageBean> imgList) {
        mDao.updateImg(mDao.HOUSES_DETAIL_TOP_IMG, imgList, this);
    }

    @Override
    public void commitComment(UserCommentBean userCommentBean) {
        mDao.commitComment(mDao.HOUSES_DETAIL_HOUSES_COMMENT, userCommentBean, this);
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
                case ServerDao.HOUSES_DETAIL_HOUSES_COMMENT:
                    mView.showCommitComment();
                    break;
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.showUploadFail();
    }
}
