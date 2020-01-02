package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.FlowViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by zgm on 2017/3/31.
 * 搜索列表adapter
 */

public class HouseLikeAdapter extends BaseQuickAdapter<HousesLikeBean, BaseViewHolder> {


    private int type;

    public HouseLikeAdapter(int layoutResId, List<HousesLikeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesLikeBean housesLikeBean) {


        if (housesLikeBean.effectId != null && housesLikeBean.effectId.size() != 0) {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), housesLikeBean.effectId.get(0).thumbnailImage);
        } else {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), null);
        }

        if (TextUtils.isEmpty(housesLikeBean.isTransparent) || "0".equals(housesLikeBean.isTransparent)) {
            holder.setVisible(R.id.tv_transParent, false);
        } else {
            holder.setVisible(R.id.tv_transParent, true);
        }


//        holder.setText(R.id.tv_house_name, StringUtils.null2Length0(housesLikeBean.devName));
//        holder.setText(R.id.tv_address, StringUtils.null2Length0(housesLikeBean.addressDistrict) + "     " + StringUtils.null2Length0(housesLikeBean.propertyType));

        String propertyType = "";
        if (!TextUtils.isEmpty(housesLikeBean.propertyType)) {
            String[] split = housesLikeBean.propertyType.split(",");
            propertyType = split[0];
        }

        holder.setText(R.id.tv_house_name, StringUtils.null2Length0(housesLikeBean.devName));
        if (!TextUtils.isEmpty(housesLikeBean.addressDistrict) && !TextUtils.isEmpty(propertyType)) {
            holder.setText(R.id.tv_address, StringUtils.null2Length0(housesLikeBean.addressDistrict) + " 丨 " + propertyType);
        } else {
            if (!TextUtils.isEmpty(housesLikeBean.addressDistrict)) {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(housesLikeBean.addressDistrict));
            } else {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(propertyType));
            }
        }

//            if (type == 1) {
//            //近期开盘
//            holder.setText(R.id.tv_area, "开盘时间： " + TimeUtils.millis2String(Long.parseLong(housesLikeBean.openTime + "000")
//                    , new SimpleDateFormat("yyyy-MM-dd")));
//        } else {
//            holder.setText(R.id.tv_area, (TextUtils.isEmpty(housesLikeBean.devSquareMetre) ? " " : housesLikeBean.devSquareMetre + "㎡"));
//        }
        if (!TextUtils.isEmpty(housesLikeBean.devSquareMetre) && !TextUtils.isEmpty(((TextView) holder.getView(R.id.tv_address)).getText())) {
            holder.setText(R.id.tv_area, (" 丨 建面 " + housesLikeBean.devSquareMetre + "㎡"));
        } else if (TextUtils.isEmpty(housesLikeBean.devSquareMetre)) {
            holder.setText(R.id.tv_area, "");
        } else {
            holder.setText(R.id.tv_area, ("建面 " + housesLikeBean.devSquareMetre + "㎡"));

        }
        if (TextUtils.isEmpty(housesLikeBean.averPrice) || "0".equals(housesLikeBean.averPrice)) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(housesLikeBean.averPrice);
                if (avgPrice >= 1000000) {
                    holder.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    holder.setText(R.id.tv_price, housesLikeBean.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                LogOut.d("averPrice", e.toString());
                e.printStackTrace();
            }
        }

        holder.setText(R.id.tv_room, TextUtils.isEmpty(housesLikeBean.devBedroom) ? " " : housesLikeBean.devBedroom);
        FlowViewGroup floviewgroup = (FlowViewGroup) holder.getView(R.id.floviewgroup);
        floviewgroup.removeAllViews();
        if (!TextUtils.isEmpty(housesLikeBean.feature)) {
            String[] features = housesLikeBean.feature.split(",");
            int length = features.length;
            if (length > 3) {
                length = 3;
            }
            if (!TextUtils.isEmpty(housesLikeBean.feature)) {
                for (int i = 0; i < length; i++) {
                    TextView tvFeature = (TextView) LayoutInflater.from(mContext).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                    tvFeature.setTextSize(8);
                    tvFeature.setText(features[i]);
                    floviewgroup.addView(tvFeature);
                }
            }
        }

    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
