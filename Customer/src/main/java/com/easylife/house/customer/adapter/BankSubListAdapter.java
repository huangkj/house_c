package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BeanBankSub;

import java.util.List;

/**
 * 开户行支行列表
 */
public class BankSubListAdapter extends BaseQuickAdapter<BeanBankSub, BaseViewHolder> {
    public BankSubListAdapter(@Nullable List<BeanBankSub> data) {
        super(R.layout.item_bank_sub, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BeanBankSub item) {
        helper.setText(R.id.tvName, item.bankBranchName);
    }
}
