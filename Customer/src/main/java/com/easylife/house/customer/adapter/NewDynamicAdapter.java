package com.easylife.house.customer.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by zgm on 2017/3/27.
 */

public class NewDynamicAdapter extends BaseQuickAdapter<HousesDynamicBean, BaseViewHolder> {


    public NewDynamicAdapter(int layoutResId, List<HousesDynamicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesDynamicBean housesDynamicBean) {
        try {
            holder.setText(R.id.tv_dynamic_title_list,housesDynamicBean.title);
            holder.setText(R.id.tv_dynamic_content_list,housesDynamicBean.content);
            holder.setText(R.id.tv_dynamic_time_list, CustomerUtils.dateTransSdfMinutes(housesDynamicBean.createTime));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
