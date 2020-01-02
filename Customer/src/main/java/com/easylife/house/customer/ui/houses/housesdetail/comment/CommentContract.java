package com.easylife.house.customer.ui.houses.housesdetail.comment;

import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class CommentContract {

    interface View extends BaseView{
        void showUploadSuccess(List<ImageResult.DataBean> imageResultList);

        void showUploadFail();

        void showCommitComment();

    }

    interface Presenter extends BasePresenter<View>{
        void uploadImage(List<ImageBean> imgList);

        void commitComment(UserCommentBean userCommentBean);
    }
}
