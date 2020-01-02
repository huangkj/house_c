package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表
 */

public class HousesTypeAdapter extends BaseQuickAdapter<HousesTypeBean.HouseLayoutDataBean, BaseViewHolder> {

    public HousesTypeAdapter(int layoutResId, List<HousesTypeBean.HouseLayoutDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesTypeBean.HouseLayoutDataBean housesTypeBean) {

//        try {
        float price = 0;
            switch (housesTypeBean.salesStatus) {
                case "0":
                    holder.setText(R.id.tv_sale_status, "在售");

                    break;
                case "1":
                    holder.setText(R.id.tv_sale_status, "可售");
                    break;
                case "2":
                    holder.setText(R.id.tv_sale_status, "待售");
                    break;
                case "3":
                    holder.setText(R.id.tv_sale_status, "已售完");
                    break;
                case "4":
                    holder.setText(R.id.tv_sale_status, "不可售");
                    break;
            }

//            if(getData() != null && getData().size() == holder.getPosition()){
//                holder.setVisible(R.id.line,false);
//            }else {
//                holder.setVisible(R.id.line,true);
//            }


        if (TextUtils.isEmpty(housesTypeBean.isTransparent) || "0".equals(housesTypeBean.isTransparent)) {
            holder.setVisible(R.id.tv_transParent, false);
        } else {
            holder.setVisible(R.id.tv_transParent, true);
        }

        if (null == housesTypeBean.avgprice || TextUtils.isEmpty(housesTypeBean.avgprice) || 0 == (Float.parseFloat(housesTypeBean.avgprice))) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            if (!TextUtils.isEmpty(housesTypeBean.avgprice)) {
                price = Float.parseFloat(housesTypeBean.avgprice);
                price = price / 10000;
            }
            holder.setText(R.id.tv_price, Math.round(price * (Float.parseFloat(housesTypeBean.fArea))) + "万起");
        }

        FlowViewGroup flowViewGroup = holder.getView(R.id.type_flow);
        flowViewGroup.removeAllViews();
        String[] tagSplit = housesTypeBean.tag.split(",");
        int length = tagSplit.length;
        if (length > 3) {
            length = 3;
        }
//        for (int i = 0; i < tagSplit.length; i++) {
        for (int i = 0; i < length; i++) {
            TextView tvFeature = (TextView) LayoutInflater.from(mContext).inflate(R.layout.houses_detail_flow_item, flowViewGroup, false);
            tvFeature.setTextSize(8);
            tvFeature.setText(tagSplit[i]);
            flowViewGroup.addView(tvFeature);
        }


        holder.setText(R.id.tv_type_name, housesTypeBean.structure);
        if ("0".equals(housesTypeBean.saleNum)) {
            holder.setVisible(R.id.tv_room_count, false);
        } else {
            holder.setVisible(R.id.tv_room_count, true);
            holder.setText(R.id.tv_room_count, housesTypeBean.saleNum + "套房源");
        }

        holder.setText(R.id.tv_type, housesTypeBean.houseName + "户型");
        holder.setText(R.id.tv_area, "建面 " + housesTypeBean.fArea + "㎡");
        if (housesTypeBean.houseImg.size() != 0) {
            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_type), housesTypeBean.houseImg.get(0).url);
        } else {
            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_type), null);
        }

//        } catch (Exception e) {
//        }


    }
}
