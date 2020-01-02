package com.easylife.house.customer.ui.message.messageheadlines;

import android.text.TextUtils;

import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MessageHeadLinesPresenter extends BasePresenterImpl<MessageHeadLinesContract.View> implements MessageHeadLinesContract.Presenter, RequestManagerImpl {

    private boolean hasSaveCache;

    @Override
    public void getNetData(int page) {
        mDao.headMessage(1, page, this);
    }

    @Override
    public void getCacheData() {
        String cache = mDao.localDao.getCacheStr(LocalDao.FOLDER_CACHE_CUSTOMER, "MsgHeadLine");
        if (!TextUtils.isEmpty(cache)) {
            List<MsgHeadLine> homeList = new Gson().fromJson(cache, new TypeToken<List<MsgHeadLine>>() {
            }.getType());
            if (mView != null) mView.initData(homeList);
        }
    }

    @Override
    public void requestCount(String id) {
        mDao.headMessageCount(100, id, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (requestType == 1) {
            List<MsgHeadLine> list = new Gson().fromJson(response, new TypeToken<List<MsgHeadLine>>() {
            }.getType());
            if (!hasSaveCache && list != null && list.size() != 0) {
                mDao.localDao.saveString(LocalDao.FOLDER_CACHE_CUSTOMER, "MsgHeadLine", response);
                hasSaveCache = true;
            }
            if (mView != null)
                mView.initData(list);
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}
