package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AddressBean;
import com.easylife.house.customer.bean.RecommendBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.AddressEditActivity;
import com.easylife.house.customer.ui.mine.RecommendDetailActivity;
import com.easylife.house.customer.util.DateUtil;
import com.easylife.house.customer.util.IntentUtils;
import com.easylife.house.customer.util.ValidatorUtils;

import java.util.List;

/**
 * 我的推荐列表
 */
public class RecommendListAdapter extends BaseQuickAdapter<RecommendBean, BaseViewHolder> {
    private Activity activity;
    private ServerDao mDao;
    private ItemClickListener<String> listener;

    public RecommendListAdapter(int layoutResId, @Nullable List<RecommendBean> data) {
        super(layoutResId, data);
    }

    public RecommendListAdapter(Activity activity, ServerDao mDao, int layoutResId, @Nullable List<RecommendBean> data, ItemClickListener<String> listener) {
        super(layoutResId, data);
        this.activity = activity;
        this.mDao = mDao;
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final RecommendBean item) {
        /**
         * "devId": 1496,
         *             "followTime": 1537428105000,
         *             "followStatus": 0,
         *             "followType": 1,
         *             "assistant": "13121535150",
         *             "followStatusName": "已过期",
         *             "brokerCustomerId": 4848,
         *             "nameAndPhone": "笔墨(136****5432)",
         *             "followTypeName": "报备",
         *             "devName": "东方雅苑会员"
         */

        helper.setText(R.id.tvUserName, item.nameAndPhone);
        helper.setText(R.id.tvDevName, item.devName);
        helper.setText(R.id.tvRecordType, item.followTypeName);
        helper.setText(R.id.tvStatus, item.followTypeName);
        helper.setText(R.id.tvStatus, item.followStatusName);
        helper.setText(R.id.tvDate, DateUtil.phpToDate(item.followTime, "yyyy/MM/dd HH:mm"));
        helper.setGone(R.id.tvCall, ValidatorUtils.isMobile(item.assistant));

        /**
         * EXPIRED(0, "已过期"), REMAIN(1, "剩余天"), DEAL(2, "已成交"),NO(3,""),LOCK(4,"已锁定");
         */
        switch (item.followStatus) {
            case 0:
                //已过期
                helper.setGone(R.id.tvStatus, true);
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_past_red));
                helper.setBackgroundRes(R.id.tvStatus, R.drawable.shape_bg_recommend_past);
                break;
            case 1:
                //剩余天
                helper.setGone(R.id.tvStatus, true);
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.recommend_last_green));
                helper.setBackgroundRes(R.id.tvStatus, R.drawable.shape_bg_recommend_residue);
                break;
            case 2:
                //已成交
                helper.setGone(R.id.tvStatus, true);
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.tvStatus, R.drawable.shape_bg_recommend_deal);
                break;
            case 3:
                //不显示
                helper.setGone(R.id.tvStatus, false);
                break;
            case 4:
                //已锁定
                helper.setGone(R.id.tvStatus, true);
                helper.setTextColor(R.id.tvStatus, mContext.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.tvStatus, R.drawable.shape_bg_recommend_lock);
                break;
        }

        helper.setOnClickListener(R.id.layContent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendDetailActivity.startActivity(activity, item.brokerCustomerId, 1);
            }
        });
        helper.setOnClickListener(R.id.tvCall, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(R.id.tvCall, 0, item.assistant);
            }
        });
    }
}
