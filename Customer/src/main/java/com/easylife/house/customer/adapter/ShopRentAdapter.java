package com.easylife.house.customer.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ShopRentResponseBean;

import java.util.List;

/**
 * - @Description:  商铺租赁
 * - @Author:  hkj
 * - @Time:  2018/9/3 17:14
 */
public class ShopRentAdapter extends BaseQuickAdapter<ShopRentResponseBean, BaseViewHolder> {
    public ShopRentAdapter(int layoutResId, List<ShopRentResponseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ShopRentResponseBean data) {
        Glide.with(mContext).load(data.getImageUrl()).into(((ImageView) baseViewHolder.getView(R.id.ivLogoShopRent)));
        baseViewHolder.setText(R.id.tvTitleShopRent, data.getTitle());
        baseViewHolder.setText(R.id.tvDescShopRent, data.getArea() + "㎡·" + data.getDevName());
        baseViewHolder.setText(R.id.tvPriceShopRent, data.getRent() + "元/月");
    }
}
