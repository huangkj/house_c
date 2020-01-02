package com.easylife.house.customer.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BeanDesignCase;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.ui.pub.WebViewActivity;

/**
 * 设计案例
 */
public class DesignCaseListAdapter extends BaseQuickAdapter<BeanDesignCase, BaseViewHolder> {

    public DesignCaseListAdapter() {
        super(R.layout.item_fitment_design_case, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BeanDesignCase item) {

        helper.setOnClickListener(R.id.layContent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(mContext, "设计案例", Constants.URL_FITMENT_DESIGN_CASE);
            }
        });
    }
}
