package com.easylife.house.customer.ui.payment.refundinfoupdate;

import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.ParamRefundInfo;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class RefundInfoUpdateContract {

    interface View extends BaseView {
        void showUploadSuccess(List<ImageResult.DataBean> imageResultList);

        void showUploadFail();

        void showSubmitSuccess();

    }

    interface Presenter extends BasePresenter<View> {
        void uploadImage(List<ImageBean> imgList);

        void submit(String orderCode, ParamRefundInfo param);
    }
}
