package com.easylife.house.customer.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

import static com.easylife.house.customer.config.ItemSelectManager.HOUSE_TYPE_FLAG_TEXTSIZE;


/**
 * Created by zgm on 2017/3/31.
 * 搜索列表adapter
 */

public class SearchHouseV3Adapter extends BaseQuickAdapter<HomeSearchResponseBean, BaseViewHolder> {


    private int type;

    public SearchHouseV3Adapter(int layoutResId, List<HomeSearchResponseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeSearchResponseBean HomeSearchResponseBean) {


        if (HomeSearchResponseBean.effectId != null && HomeSearchResponseBean.effectId.size() != 0) {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), HomeSearchResponseBean.effectId.get(0).getThumbnailImage());
        } else {
            CacheManager.initSearchHouseList(mContext, (ImageView) holder.getView(R.id.iv_house), null);
        }

        if (TextUtils.isEmpty(HomeSearchResponseBean.isTransparent) || "0".equals(HomeSearchResponseBean.isTransparent)) {
            holder.setVisible(R.id.tv_transParent, false);
        } else {
            holder.setVisible(R.id.tv_transParent, true);
        }


        String propertyType = "";
        if (!TextUtils.isEmpty(HomeSearchResponseBean.propertyType)) {
            String[] split = HomeSearchResponseBean.propertyType.split(",");
            propertyType = split[0];
        }


        holder.setText(R.id.tv_house_name, StringUtils.null2Length0(HomeSearchResponseBean.devName));
        if (!TextUtils.isEmpty(HomeSearchResponseBean.addressDistrict) && !TextUtils.isEmpty(propertyType)) {
            String address = HomeSearchResponseBean.addressDistrict + " <font color='#DCDCDC'> 丨 </font> " + propertyType;

            holder.setText(R.id.tv_address, Html.fromHtml(StringUtils.null2Length0(address)));
        } else {
            if (!TextUtils.isEmpty(HomeSearchResponseBean.addressDistrict)) {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(HomeSearchResponseBean.addressDistrict));
            } else {
                holder.setText(R.id.tv_address, StringUtils.null2Length0(propertyType));
            }


        }
//        if (type == 1) {
//            //近期开盘
//            holder.setText(R.id.tv_area, "开盘时间： " + TimeUtils.millis2String(Long.parseLong(HomeSearchResponseBean.openTime + "000")
//                    , new SimpleDateFormat("yyyy-MM-dd")));

//        } else {


        if (!TextUtils.isEmpty(HomeSearchResponseBean.devSquareMetre) && !TextUtils.isEmpty(((TextView) holder.getView(R.id.tv_address)).getText())) {
            String devSquareMetre = "  <font color='#DCDCDC'> 丨 </font> 建面 " + HomeSearchResponseBean.devSquareMetre + "㎡";

            holder.setText(R.id.tv_area, (Html.fromHtml(devSquareMetre)));
        } else if (TextUtils.isEmpty(HomeSearchResponseBean.devSquareMetre)) {
            holder.setText(R.id.tv_area, "");
        } else {
            holder.setText(R.id.tv_area, ("建面 " + HomeSearchResponseBean.devSquareMetre + "㎡"));

        }
//        }
        if (TextUtils.isEmpty(HomeSearchResponseBean.averPrice) || "0".equals(HomeSearchResponseBean.averPrice)) {
            holder.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(HomeSearchResponseBean.averPrice);
                if (avgPrice >= 1000000) {
                    holder.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    holder.setText(R.id.tv_price, HomeSearchResponseBean.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.setText(R.id.tv_room, TextUtils.isEmpty(HomeSearchResponseBean.devBedroom) ? " " : HomeSearchResponseBean.devBedroom);
        FlowViewGroup floviewgroup = (FlowViewGroup) holder.getView(R.id.floviewgroup);
        floviewgroup.removeAllViews();
        if (!TextUtils.isEmpty(HomeSearchResponseBean.feature)) {
            String[] features = HomeSearchResponseBean.feature.split(",");

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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
