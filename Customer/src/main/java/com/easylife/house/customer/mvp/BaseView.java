package com.easylife.house.customer.mvp;

import android.content.Context;


public interface BaseView {
    Context getContext();

    void showTip(String msg);

    void showLoading();

    void cancelLoading();
}
