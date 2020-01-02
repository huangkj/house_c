package com.easylife.house.customer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.DetailSalingBean;

import java.util.List;

/**
 * Created by zgm on 2017/4/17.
 * 详情页在售房源
 */

public class HouseDetailSalingAdapter extends BaseQuickAdapter<DetailSalingBean, BaseViewHolder> {


    public HouseDetailSalingAdapter(int layoutResId, List<DetailSalingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DetailSalingBean salingBean) {

        if(salingBean != null){
            holder.setText(R.id.tv_saling_title,salingBean.salingTitle+salingBean.farea+"㎡(建面)");
            holder.setText(R.id.tv_saling_price,salingBean.price+"万元/套");
            holder.setText(R.id.tv_saling_discount,"已优惠"+salingBean.discount+"万元");
            holder.setText(R.id.tv_can_sale,salingBean.count+"套可售");
        }

    }
}
