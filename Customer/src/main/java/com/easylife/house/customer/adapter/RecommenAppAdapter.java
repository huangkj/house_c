package com.easylife.house.customer.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.RecommenApp;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：推荐应用
 */

public class RecommenAppAdapter extends BaseQuickAdapter<RecommenApp, BaseViewHolder> {

    public RecommenAppAdapter(int layoutResId, List<RecommenApp> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommenApp app) {
        baseViewHolder.setText(R.id.tvName, app.title);
        baseViewHolder.setText(R.id.tvDesc, app.content);
        CacheManager.initImageUserHeader(mContext, (ImageView) baseViewHolder.getView(R.id.imgCover), app.img);
    }
}
