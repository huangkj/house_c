package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.FiltrateHouseBean;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.easylife.house.customer.config.ItemSelectManager.HOUSE_TYPE_FLAG_TEXTSIZE;

public class FiltrateHouseAdapter extends BaseQuickAdapter<FiltrateHouseBean.ListBean, BaseViewHolder> {

    private ArrayList<FiltrateHouseBean.ListBean> compareList = new ArrayList<>();
    private OnAdapterListener mOnAdapterListener;

    public FiltrateHouseAdapter(@Nullable List<FiltrateHouseBean.ListBean> data) {
        super(R.layout.filtrate_house_item, data);
    }

    public interface OnAdapterListener {
        void onCompareCountChange(ArrayList<FiltrateHouseBean.ListBean> compareList);

        void onItemClick(FiltrateHouseBean.ListBean item);
    }

    public void setOnAdapterListener(OnAdapterListener l) {
        this.mOnAdapterListener = l;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FiltrateHouseBean.ListBean item) {
        setData(helper, item);


        helper.getView(R.id.tvCompare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isCompare = !item.isCompare;
                if (item.isCompare) {
                    compareList.add(item);
                } else {
                    compareList.remove(item);
                }
                notifyItemChanged(helper.getAdapterPosition());
                if (mOnAdapterListener != null) {
                    mOnAdapterListener.onCompareCountChange(compareList);
                }
            }
        });


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAdapterListener != null) {
                    mOnAdapterListener.onItemClick(item);
                }
            }
        });

    }


    private void setData(BaseViewHolder helper, FiltrateHouseBean.ListBean item) {
        helper.setText(R.id.tvCompare, item.isCompare ? "已加入" : "+ 加入对比");
        Glide.with(mContext).load(item.getUrl()).into(((ImageView) helper.getView(R.id.iv_house)));
        helper.setVisible(R.id.tv_transParent, item.getIsTransparent().equals("1"));
        helper.setText(R.id.tv_house_name, StringUtils.null2Length0(item.getDevName()));
        String houseType = item.getHouseType();
        if (!TextUtils.isEmpty(item.getArea()) && !TextUtils.isEmpty(houseType)) {
            String address = item.getArea() + " <font color='#DCDCDC'> 丨 </font> " + houseType;

            helper.setText(R.id.tv_address, Html.fromHtml(StringUtils.null2Length0(address)));
        } else {
            if (!TextUtils.isEmpty(item.getArea())) {
                helper.setText(R.id.tv_address, StringUtils.null2Length0(item.getArea()));
            } else {
                helper.setText(R.id.tv_address, StringUtils.null2Length0(houseType));
            }

        }


        if (!TextUtils.isEmpty(item.devSquareMetre) && !TextUtils.isEmpty(((TextView) helper.getView(R.id.tv_address)).getText())) {
            String devSquareMetre = "  <font color='#DCDCDC'> 丨 </font> 建面 " + item.devSquareMetre + "㎡";

            helper.setText(R.id.tv_area, (Html.fromHtml(devSquareMetre)));
        } else if (TextUtils.isEmpty(item.devSquareMetre)) {
            helper.setText(R.id.tv_area, "");
        } else {
            helper.setText(R.id.tv_area, ("建面 " + item.devSquareMetre + "㎡"));

        }


        if (TextUtils.isEmpty(item.averPrice) || "0".equals(item.averPrice)) {
            helper.setText(R.id.tv_price, "价格待定");
        } else {
            try {
                int avgPrice = Integer.parseInt(item.averPrice);
                if (avgPrice >= 1000000) {
                    helper.setText(R.id.tv_price, avgPrice / 10000 + "万元/㎡");
                } else {
                    helper.setText(R.id.tv_price, item.averPrice + "元/㎡");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        helper.setText(R.id.tv_room, TextUtils.isEmpty(item.devBedroom) ? " " : item.devBedroom);
        FlowViewGroup floviewgroup = (FlowViewGroup) helper.getView(R.id.floviewgroup);
        floviewgroup.removeAllViews();
        if (!TextUtils.isEmpty(item.getFeature())) {
            String[] features = item.getFeature().split(",");

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
