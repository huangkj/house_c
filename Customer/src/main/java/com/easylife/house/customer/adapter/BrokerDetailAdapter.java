package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BrokerDetailBean;
import com.easylife.house.customer.util.MoneyUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class BrokerDetailAdapter extends BaseQuickAdapter<BrokerDetailBean.BrokerOdersBean, BaseViewHolder> {
    public BrokerDetailAdapter(int layoutResId, @Nullable List<BrokerDetailBean.BrokerOdersBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrokerDetailBean.BrokerOdersBean item) {
        helper.setText(R.id.tvHouseName, item.getDevName() + " " + item.getHouseNum());
        helper.setText(R.id.tvDateApplyBrokerageItem, TimeUtils.millis2String(item.getCreatTime(), new SimpleDateFormat("yyyy/MM/dd")));
        helper.setText(R.id.tvUserNameBrokerageItem, item.getCustomerName() + " (" + item.getCustomerPhone() + ")");
        helper.setText(R.id.tvShouldBrokerageItem, "申请结佣： ¥" + MoneyUtils.moneyFormat3(item.getShouldAmount()));
    }
}
