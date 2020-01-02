package com.easylife.house.customer.mvp;


public interface  BasePresenter <V extends BaseView>{
    void attachView(V view);

    void detachView();
}
