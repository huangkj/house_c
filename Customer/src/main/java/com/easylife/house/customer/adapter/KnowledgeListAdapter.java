package com.easylife.house.customer.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BeanDesignCase;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.ui.pub.WebViewActivity;

/**
 * 装修知识
 */
public class KnowledgeListAdapter extends BaseQuickAdapter<BeanDesignCase, BaseViewHolder> {

    public KnowledgeListAdapter() {
        super(R.layout.item_fitment_knowledge, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BeanDesignCase item) {
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.setGone(R.id.imgReadImage, false);
        }

        helper.setOnClickListener(R.id.layContent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(mContext, "装修知识", Constants.URL_FITMENT_KNOWLEDGE);
            }
        });
    }
}
