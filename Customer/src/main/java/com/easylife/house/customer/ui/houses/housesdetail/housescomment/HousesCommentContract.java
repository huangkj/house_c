package com.easylife.house.customer.ui.houses.housesdetail.housescomment;

import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/3/24.
 */

public class HousesCommentContract {

    interface View extends BaseView{
        void showMoreData(CommentListBean commentListBean);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void loadMoreData(String projectId,String pageSize);
    }
}
