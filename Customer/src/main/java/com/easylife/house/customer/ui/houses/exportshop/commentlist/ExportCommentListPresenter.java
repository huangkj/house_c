package com.easylife.house.customer.ui.houses.exportshop.commentlist;

import com.easylife.house.customer.bean.CommentSubmit;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 * 专家店铺评论列表
 */

public class ExportCommentListPresenter extends BasePresenterImpl<ExportCommentListContract.View> implements ExportCommentListContract.Presenter, RequestManagerImpl {

    @Override
    public void requestUserComment(String brokeCode, String pageSize) {
        mDao.getExportComment(1, brokeCode, pageSize, this);
    }

    @Override
    public void requestUserCommentGood(String brokeCode) {
        mDao.getExportCommentGood(2, brokeCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    List<ExportUserCommentBean> commentBeanList = new Gson().fromJson(response, new TypeToken<List<ExportUserCommentBean>>() {
                    }.getType());
                    mView.showUserCommentList(commentBeanList);
                    break;
                case 2:
                    CommentSubmit commentSubmit = new Gson().fromJson(response, CommentSubmit.class);
                    mView.showCommentGood(commentSubmit);
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
