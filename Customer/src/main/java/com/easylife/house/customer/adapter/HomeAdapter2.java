package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.bean.HotDevBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

import static com.easylife.house.customer.config.ItemSelectManager.HOUSE_TYPE_FLAG_TEXTSIZE;

/**
 * Created by zgm on 2017/3/16.
 */

public class HomeAdapter2 extends BaseQuickAdapter<HotDevBean, BaseViewHolder> {


    public HomeAdapter2(@Nullable List<HotDevBean> data) {
        super(R.layout.search_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HotDevBean item) {
        if (item.effectId != null && item.effectId.size() != 0) {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), item.effectId.get(0).getThumbnailImage());
        } else {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), null);
        }

        if (TextUtils.isEmpty(item.isTransparent) || "0".equals(item.isTransparent)) {
            holder.setVisible(R.id.tv_transParent, false);
        } else {
            holder.setVisible(R.id.tv_transParent, true);
        }

        String propertyType = "";
        if (!TextUtils.isEmpty(item.propertyType)) {
            String[] split = item.propertyType.split(",");
            propertyType = split[0];
        }

        holder.setText(R.id.tv_house_name, StringUtils.null2Length0(item.devName));
        if (!TextUtils.isEmpty(item.addressDistrict) && !TextUtils.isEmpty(propertyType)) {
            String address = item.addressDistrict + " <font color='#DCDCDC'> 丨 </font> " + propertyType;

            holder.setText(R.id.tv_address, Html.fromHtml(StringUtils.null2Length0(address)));
        } else {
            if (!TextUtils.isEmpty(item.addressDistrict)) {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(item.addressDistrict));
            } else {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(propertyType));
            }


        }
//        holder.setText(R.id.tv_house_name, StringUtils.null2Length0(item.devName));
//        holder.setText(R.id.tv_address, StringUtils.null2Length0(item.addressDistrict) + "     " + StringUtils.null2Length0(item.propertyType));
//        if (type == 1) {
//            //近期开盘
//            holder.setText(R.id.tv_area, "开盘时间： " + TimeUtils.millis2String(Long.parseLong(item.openTime + "000")
//                    , new SimpleDateFormat("yyyy-MM-dd")));

//        } else {

//        holder.setText(R.id.tv_area, (TextUtils.isEmpty(item.devSquareMetre) ? " " : item.devSquareMetre + "㎡"));
//        }


        if (!TextUtils.isEmpty(item.devSquareMetre) && !TextUtils.isEmpty(((TextView) holder.getView(R.id.tv_address)).getText())) {
            String devSquareMetre = "  <font color='#DCDCDC'> 丨 </font> 建面 " + item.devSquareMetre + "㎡";

            holder.setText(R.id.tv_area, (Html.fromHtml(devSquareMetre)));
        } else if (TextUtils.isEmpty(item.devSquareMetre)) {
            holder.setText(R.id.tv_area, "");
        } else {
            holder.setText(R.id.tv_area, ("建面 " + item.devSquareMetre + "㎡"));

        }
//        }
        if (TextUtils.isEmpty(item.averPrice) || "0".equals(item.averPrice)) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(item.averPrice);
                if (avgPrice >= 1000000) {
                    holder.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    holder.setText(R.id.tv_price, item.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
/*

        if (TextUtils.isEmpty(item.averPrice) || "0".equals(item.averPrice)) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(item.averPrice);
                if (avgPrice >= 1000000) {
                    holder.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    holder.setText(R.id.tv_price, item.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/

        holder.setText(R.id.tv_room, TextUtils.isEmpty(item.devBedroom) ? " " : item.devBedroom);
        FlowViewGroup floviewgroup = (FlowViewGroup) holder.getView(R.id.floviewgroup);
        floviewgroup.removeAllViews();
        if (!TextUtils.isEmpty(item.feature)) {
            String[] features = item.feature.split(",");
            int length = features.length;
            if (length > 3) {
                length = 3;
            }

            for (int i = 0; i < length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(mContext).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setTextSize(HOUSE_TYPE_FLAG_TEXTSIZE);
                tvFeature.setText(features[i]);
                floviewgroup.addView(tvFeature);
            }
        }
    }
}
