package com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment;

import com.easylife.house.customer.bean.ExportCommentBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/3/27.
 */

public class ExportCommentContract {

    interface View extends BaseView{
        void showCommitSuccess();

        void showCommitFail(String msg);

        void showShopBaseData(ExportShopBaseBean baseBean);
    }

    interface Presenter extends BasePresenter<View>{
        void commitComment(ExportCommentBean commentBean);

        /**
         * 请求基础信息
         * @param brokeCode
         */
        void requestShopBase(String brokeCode);
    }
}
