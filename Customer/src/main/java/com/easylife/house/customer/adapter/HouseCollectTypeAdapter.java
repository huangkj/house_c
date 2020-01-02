package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：户型列表
 */

public class HouseCollectTypeAdapter extends BaseQuickAdapter<HouseColletion, BaseViewHolder> {

    public HouseCollectTypeAdapter(int layoutResId, List<HouseColletion> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HouseColletion housesTypeBean) {
        holder.setText(R.id.tvDevName, housesTypeBean.devName);
        holder.setText(R.id.tvPriceUnit, housesTypeBean.avgprice + "元/㎡起");

        ImageView imgCover = holder.getView(R.id.imgCover);
        if (housesTypeBean.houseImg != null && housesTypeBean.houseImg.size() != 0) {
            CacheManager.initImageClientList(mContext, imgCover, housesTypeBean.houseImg.get(0).url);
        }

        holder.setText(R.id.tvHouseName, housesTypeBean.structure);
        String text = "";
        if (!TextUtils.isEmpty(housesTypeBean.fArea))
            text += "建面" + housesTypeBean.fArea + "㎡";
        if (!TextUtils.isEmpty(housesTypeBean.houseName))
            text += "  |  " + housesTypeBean.houseName;
        if (!TextUtils.isEmpty(housesTypeBean.houseCount))
            text += "  |  " + housesTypeBean.houseCount + "套房源";
        holder.setText(R.id.tvLocation, text);

        holder.setText(R.id.tvPriceTotal, PriceUtil.formatPriceAver(housesTypeBean.mTotalPrice));

        //0在售，1可售,2待售，3已售完，4不可售，5未知
        String status = "在售";
        switch (housesTypeBean.salesStatus) {
            case "0":
                status = "在售";
                break;
            case "1":
                status = "可售";
                break;
            case "2":
                status = "待售";
                break;
            case "3":
                status = "已售完";
                break;
            case "4":
                status = "不可售";
                break;
            case "5":
                status = "未知";
                break;
        }
        holder.setText(R.id.tvSaleStatus, status);

        FlowViewGroup flowViewGroup = holder.getView(R.id.groupTags);
        flowViewGroup.removeAllViews();
        if (TextUtils.isEmpty(housesTypeBean.tag)) {
            flowViewGroup.removeAllViews();
        } else {
            String[] tagSplit = housesTypeBean.tag.split(",");
            if (tagSplit != null)
                // 最多显示3个，保证展示一行
                for (int i = 0; i < (tagSplit.length > 3 ? 3 : tagSplit.length); i++) {
                    TextView text1 = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag_orange, flowViewGroup, false);
                    text1.setText(tagSplit[i]);
                    flowViewGroup.addView(text1);
                }
        }
    }
}
