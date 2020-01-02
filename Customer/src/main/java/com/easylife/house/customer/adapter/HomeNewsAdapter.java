package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HomeNewsBean;

import java.util.List;

public class HomeNewsAdapter extends BaseQuickAdapter<HomeNewsBean.ListBean, BaseViewHolder> {
    public HomeNewsAdapter(int layoutResId, List<HomeNewsBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeNewsBean.ListBean listBean) {
        int position = baseViewHolder.getAdapterPosition();

        if (listBean.getType() == 1) {
            //单一张图片
            baseViewHolder.setVisible(R.id.relContent, false);
            baseViewHolder.setVisible(R.id.ivSingleImg, true);
            Glide.with(mContext).load(listBean.getPictureAddress()).into(((ImageView) baseViewHolder.getView(R.id.ivSingleImg)));
        } else {
            baseViewHolder.setVisible(R.id.relContent, true);
            baseViewHolder.setVisible(R.id.ivSingleImg, false);
            baseViewHolder.setText(R.id.tvTopTitle, listBean.getTopTitle());
            baseViewHolder.setText(R.id.tvTitle, listBean.getTitleOrLink());
            if (TextUtils.isEmpty(listBean.getPictureAddress())) {
                Glide.with(mContext).load(listBean.getLocalPicture()).into(((ImageView) baseViewHolder.getView(R.id.ivImg)));
            } else {
                Glide.with(mContext).load(listBean.getPictureAddress()).into(((ImageView) baseViewHolder.getView(R.id.ivImg)));
            }
        }
    }
}
