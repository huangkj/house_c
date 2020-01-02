package com.easylife.house.customer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PayDetail;
import com.easylife.house.customer.util.DoubleFomat;

import java.util.List;

/**
 * Created by Mars on 2017/7/4 14:51.
 * 描述：支付明细列表
 */

public class PayDetailAdapter extends BaseQuickAdapter<PayDetail, BaseViewHolder> {
    public PayDetailAdapter(int layoutResId, List<PayDetail> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PayDetail payDetail) {
        baseViewHolder.setText(R.id.tvPayDetailNo, payDetail.serialNumber);
        baseViewHolder.setText(R.id.tvPaid, "￥" + DoubleFomat.format2(payDetail.money));
        baseViewHolder.setText(R.id.tvPayDate, payDetail.createTime);
        baseViewHolder.setText(R.id.tvPayStatus, payDetail.msg);
    }
}
