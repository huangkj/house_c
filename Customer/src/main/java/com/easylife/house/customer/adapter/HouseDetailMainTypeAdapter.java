package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailMainTypeBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/3/21.
 */

public class HouseDetailMainTypeAdapter extends BaseQuickAdapter<HousesDetailMainTypeBean, BaseViewHolder> {

    public HouseDetailMainTypeAdapter(int layoutResId, List<HousesDetailMainTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HousesDetailMainTypeBean housesDetailMainTypeBean) {
        try {

            if(housesDetailMainTypeBean.hourseImg != null && housesDetailMainTypeBean.hourseImg.size() != 0){
                CacheManager.initImageClientList(mContext,(ImageView) holder.getView(R.id.iv_type),housesDetailMainTypeBean.hourseImg.get(0).url);
            }else {
                CacheManager.initImageClientList(mContext,(ImageView) holder.getView(R.id.iv_type),null);
            }

            holder.setText(R.id.tv_type_room,housesDetailMainTypeBean.id);
            if("0".equals(housesDetailMainTypeBean.salesStatus)){
                holder.setText(R.id.tv_type_status,"在售");
            }else if("1".equals(housesDetailMainTypeBean.salesStatus)){
                holder.setText(R.id.tv_type_status,"停售");
            }
            holder.setText(R.id.tv_area,housesDetailMainTypeBean.fArea+"m²");
            int price = 0;
            if(!TextUtils.isEmpty(housesDetailMainTypeBean.mTotalPrice)){
                price =Integer.parseInt(housesDetailMainTypeBean.mTotalPrice)/10000;
            }
            holder.setText(R.id.tv_sale_star,price+"万起");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
