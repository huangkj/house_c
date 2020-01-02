package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.MyPrizeBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyPrizeAdapter extends BaseQuickAdapter<MyPrizeBean, BaseViewHolder> {
    public MyPrizeAdapter(int layoutResId, @Nullable List<MyPrizeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyPrizeBean item) {
        Glide.with(mContext).load(item.getImg()).into(((ImageView) helper.getView(R.id.ivPrize)));
        helper.setText(R.id.tvTitlePrize, item.getName());
        helper.setText(R.id.tvDatePrize, "中奖时间：" + TimeUtils.millis2String(item.getWinTime(), new SimpleDateFormat("yyyy年MM月dd日")));
    }
}
