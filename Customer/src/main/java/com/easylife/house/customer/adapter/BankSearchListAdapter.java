package com.easylife.house.customer.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BeanBank;

/**
 * 收货地址列表
 */
public class BankSearchListAdapter extends BaseQuickAdapter<BeanBank, BaseViewHolder> {

    public BankSearchListAdapter() {
        super(R.layout.item_banklist_search, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BeanBank item) {
        String name = item.getBeanName();
        if (!TextUtils.isEmpty(name)) {
            if (name.contains("\r"))
                name = name.replace("\r", "");
            if (name.contains("\n"))
                name = name.replace("\n", "");
        }
        helper.setText(R.id.tvTitle, name);
    }
}
