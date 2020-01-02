package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HouseSearchBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.DoubleFomat;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 主力户型
 */

public class HousesTypeDetailOtherTypeAdapter extends BaseQuickAdapter<HouseSearchBean.EstateDevBuildHousesBean, BaseViewHolder> {

    public HousesTypeDetailOtherTypeAdapter(int layoutResId, List<HouseSearchBean.EstateDevBuildHousesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HouseSearchBean.EstateDevBuildHousesBean housesTypeBean) {

        try {
            float price = 0;
            switch (housesTypeBean.salesStatus) {
                case "0":
                    holder.setText(R.id.tv_type_status, "在售");
                    break;
                case "1":
                    holder.setText(R.id.tv_type_status, "可售");
                    break;
                case "2":
                    holder.setText(R.id.tv_type_status, "待售");
                    break;
                case "3":
                    holder.setText(R.id.tv_type_status, "已售完");
                    break;
                case "4":
                    holder.setText(R.id.tv_type_status, "不可售");
                    break;
            }

            if (null == housesTypeBean.avgprice || TextUtils.isEmpty(housesTypeBean.avgprice) || 0 == Float.parseFloat(housesTypeBean.avgprice)) {
                holder.setText(R.id.tv_sale_star, "价格待定");
            } else {
                if (!TextUtils.isEmpty(housesTypeBean.avgprice)) {
                    price = Float.parseFloat(housesTypeBean.avgprice);
                    price = price / 10000;
                }
                holder.setText(R.id.tv_sale_star, Math.round(price * (Float.parseFloat(housesTypeBean.fArea))) + "万起");
            }
            holder.setText(R.id.tv_type_room, housesTypeBean.structure.substring(0, 6));//主力户型  限制到"卫"  ，显示为 X室X厅X卫


            if (!TextUtils.isEmpty(housesTypeBean.fArea)) {
                if (housesTypeBean.fArea.contains(".")) {
                    String format2 = DoubleFomat.format2(housesTypeBean.fArea);
                    holder.setText(R.id.tv_area, format2 + "㎡");
                } else {
                    holder.setText(R.id.tv_area, housesTypeBean.fArea + "㎡");
                }
            }

            if (housesTypeBean.houseImg.size() != 0) {
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_type), housesTypeBean.houseImg.get(0).url);
            } else {
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_type), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
