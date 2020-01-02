package com.easylife.house.customer.ui.houses.exportshop.commentlist;

import com.easylife.house.customer.bean.CommentSubmit;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 * 专家店铺评论列表
 */

public class ExportCommentListContract {

    interface View extends BaseView{
        void showUserCommentList(List<ExportUserCommentBean> commentBeanList);
        void showCommentGood(CommentSubmit commentSubmit);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void requestUserComment(String brokeCode,String pageSize);
        void requestUserCommentGood(String brokeCode);
    }
}
