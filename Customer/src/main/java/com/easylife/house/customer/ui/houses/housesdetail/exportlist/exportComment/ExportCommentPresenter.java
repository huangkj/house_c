package com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment;

import com.easylife.house.customer.bean.ExportCommentBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

/**
 * Created by zgm on 2017/3/27.
 */

public class ExportCommentPresenter extends BasePresenterImpl<ExportCommentContract.View> implements ExportCommentContract.Presenter, RequestManagerImpl {
    @Override
    public void commitComment(ExportCommentBean commentBean) {
        mDao.commitExportComment(2, commentBean, this);
    }

    /**
     * 请求专家店铺基础信息
     *
     * @param brokeCode
     */
    @Override
    public void requestShopBase(String brokeCode) {
        mDao.getExportBase(1, brokeCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                ExportShopBaseBean baseBean = new Gson().fromJson(response, ExportShopBaseBean.class);
                mView.showShopBaseData(baseBean);
                break;
            case 2:
                mView.showCommitSuccess();
                break;
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code != null) {
            mView.showCommitFail(code.msg);
        } else {
            mView.showCommitFail("评价失败");
        }
    }
}
