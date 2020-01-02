package com.easylife.house.customer.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.SignListBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 类描述：
 * 创建者：zgm on 2017/11/22/022.
 */

public class SignAdapter extends BaseQuickAdapter<SignListBean.PointDetailsBean, BaseViewHolder> {


    public SignAdapter(@LayoutRes int layoutResId, @Nullable List<SignListBean.PointDetailsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SignListBean.PointDetailsBean item) {
        helper.setText(R.id.tv_integrail, "获得" + item.point + "积分");
//        if(item.time !null)
        helper.setText(R.id.tv_date, TimeUtils.millis2String(item.time, new SimpleDateFormat("yyyy-MM-dd")));
    }
}
