package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;


/**
 * Created by zgm on 2017/3/31.
 * 搜索列表adapter
 */

public class SearchHouseAdapter extends BaseQuickAdapter<HousesDetailBaseBean, BaseViewHolder> {


    public SearchHouseAdapter(int layoutResId, List<HousesDetailBaseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesDetailBaseBean housesDetailBaseBean) {


        if (housesDetailBaseBean.effectId != null && housesDetailBaseBean.effectId.size() != 0) {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), housesDetailBaseBean.effectId.get(0).thumbnailImage);
        } else {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), null);
        }

        if (TextUtils.isEmpty(housesDetailBaseBean.isTransparent) || "0".equals(housesDetailBaseBean.isTransparent)) {
            holder.setVisible(R.id.tv_transParent, false);
        } else {
            holder.setVisible(R.id.tv_transParent, true);
        }

        holder.setText(R.id.tv_house_name, housesDetailBaseBean.devName);
        holder.setText(R.id.tv_address, housesDetailBaseBean.addressDistrict + "     " + housesDetailBaseBean.propertyType);
        holder.setText(R.id.tv_area, (TextUtils.isEmpty(housesDetailBaseBean.devSquareMetre) ? " " : housesDetailBaseBean.devSquareMetre + "㎡"));
        if (TextUtils.isEmpty(housesDetailBaseBean.averPrice) || "0".equals(housesDetailBaseBean.averPrice)) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(housesDetailBaseBean.averPrice);
                if (avgPrice >= 1000000) {
                    holder.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    holder.setText(R.id.tv_price, housesDetailBaseBean.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.setText(R.id.tv_room, TextUtils.isEmpty(housesDetailBaseBean.devBedroom) ? " " : housesDetailBaseBean.devBedroom);

        FlowViewGroup floviewgroup = (FlowViewGroup) holder.getView(R.id.floviewgroup);
        floviewgroup.removeAllViews();
        if (!TextUtils.isEmpty(housesDetailBaseBean.feature)) {
            String[] features = housesDetailBaseBean.feature.split(",");
            for (int i = 0; i < features.length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(mContext).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setTextSize(8);
                tvFeature.setText(features[i]);
                floviewgroup.addView(tvFeature);
            }
        }
    }
}
