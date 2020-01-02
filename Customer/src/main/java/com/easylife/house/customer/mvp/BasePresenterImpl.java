package com.easylife.house.customer.mvp;

import com.easylife.house.customer.dao.ServerDao;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    protected V mView;
    protected ServerDao mDao;

    @Override
    public void attachView(V view) {
        mView = view;
        mDao = new ServerDao(view.getContext());
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
