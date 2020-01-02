package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.RecommendRateBean;
import com.easylife.house.customer.util.DateUtil;

import java.util.List;

/**
 * 推荐详情中的推荐进度列表
 */
public class RecommendRateListAdapter extends BaseQuickAdapter<RecommendRateBean, BaseViewHolder> {

    public RecommendRateListAdapter(int layoutResId, @Nullable List<RecommendRateBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final RecommendRateBean item) {

        /**
         * createTime : 1538214117518
         * followType : 1
         * id : 5baf48e5e4b053e8e1e0aba6
         * check : 成功
         * followTypeName : 报备
         */

        helper.setText(R.id.tvType, item.followTypeName);
        helper.setText(R.id.tvStatus, item.check);
        helper.setText(R.id.tvDate, DateUtil.phpToDate(item.createTime, "yyyy/MM/dd HH:mm"));

        switch (item.check) {
            case "成功":
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_status_green));
                break;
            case "拒绝":
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_status_red));
                break;
            case "审核中":
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_status_orange));
                break;
            case "未审核":
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_status_blue));
                break;
            default:
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_status_gray));
                break;
        }


        if (helper.getAdapterPosition() == getData().size() - 1) {
            helper.setGone(R.id.line, false);
        }
    }
}
