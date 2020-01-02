package com.easylife.house.customer.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.OperaterRecordBean;

import java.util.List;

public class OperaterRecordAdapter2 extends BaseQuickAdapter<OperaterRecordBean, BaseViewHolder> {
    public OperaterRecordAdapter2(@Nullable List<OperaterRecordBean> data) {
        super(R.layout.operate_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OperaterRecordBean item) {

        int adapterPosition = helper.getAdapterPosition();

        helper.setGone(R.id.vBottom, !(adapterPosition == getData().size() - 1));
        helper.setText(R.id.tvDate, item.getCreateTime());
        helper.setText(R.id.tvName, item.getStateStr());
        helper.setText(R.id.tvStatus, item.getCheckStatus());
        helper.setVisible(R.id.tvReason, !TextUtils.isEmpty(item.getRemark()));
        helper.addOnClickListener(R.id.tvReason);


        if (adapterPosition == 0) {
            helper.setBackgroundRes(R.id.iv_shape, R.drawable.follow_progress_shape);
            helper.setTextColor(R.id.tvName, Color.parseColor("#FF6800"));
        } else {
            helper.setBackgroundRes(R.id.iv_shape, R.drawable.follow_progress_shape2);
            helper.setTextColor(R.id.tvName, Color.parseColor("#535353"));
        }

    }
}
