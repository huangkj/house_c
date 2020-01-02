package com.easylife.house.customer.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PageTitleBean;

import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 */

public class PagerTitleAdapter extends BaseQuickAdapter<PageTitleBean, BaseViewHolder> {

    public PagerTitleAdapter(int layoutResId, List<PageTitleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PageTitleBean titleBean) {
        holder.setText(R.id.tv_pager_item,titleBean.title);
        if(titleBean.isChoose){
            ((TextView)holder.getView(R.id.tv_pager_item)).setTextSize(16);
            holder.setTextColor(R.id.tv_pager_item,mContext.getResources().getColor(R.color.gradient_end));
        }else {
            ((TextView)holder.getView(R.id.tv_pager_item)).setTextSize(12);
            holder.setTextColor(R.id.tv_pager_item,mContext.getResources().getColor(R.color.white));
        }
    }
}
