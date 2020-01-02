package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.bean.BeanFitmentCase;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.AddressEditActivity;

import java.util.List;

/**
 * 收货地址列表
 */
public class FitmentCaseListAdapter extends BaseQuickAdapter<BeanFitmentCase, BaseViewHolder> {

    public FitmentCaseListAdapter( ) {
        super(R.layout.item_fitment_case, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, final BeanFitmentCase item) {
    }
}
