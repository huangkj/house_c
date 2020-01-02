package com.easylife.house.customer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class NewDynamicApdater extends BaseQuickAdapter<HousesDynamicBean, BaseViewHolder> {

    public NewDynamicApdater(int layoutResId, List<HousesDynamicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesDynamicBean housesDynamicBean) {
        holder.setText(R.id.tv_dynamic_title,housesDynamicBean.title);
        holder.setText(R.id.tv_dynamic_content,housesDynamicBean.content);
        holder.setText(R.id.tv_dynamic_time, CustomerUtils.dateTransSdfMinutes(housesDynamicBean.createTime));
    }
}
