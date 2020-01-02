package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BankCardListBean;

import java.util.List;

public class MyBankCardAdapter extends BaseQuickAdapter<BankCardListBean, BaseViewHolder> {
    public MyBankCardAdapter(int layoutResId, @Nullable List<BankCardListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCardListBean item) {
        helper.setVisible(R.id.tvComplete, !item.isComplete());
        helper.addOnClickListener(R.id.llMyBankCard);

        int position = helper.getAdapterPosition();
        if (position % 2 == 0) {
            helper.setBackgroundRes(R.id.llMyBankCard, R.drawable.my_bank_card_item_bg);
        } else {
            helper.setBackgroundRes(R.id.llMyBankCard, R.drawable.my_bank_card_item_red_bg);
        }

        helper.setText(R.id.tvBankNameMyBankCard, item.getBelongToBank());
        String last4Num = item.getBankCardNum().substring(item.getBankCardNum().length() - 4);
        helper.setText(R.id.tvBankCardNumberMyBankCard, last4Num);
    }
}
