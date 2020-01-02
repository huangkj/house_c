package com.easylife.house.customer.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 * 所售楼盘
 */

public class ExportShopHouseAdapter extends BaseQuickAdapter<ExportSaleHousesBean, BaseViewHolder> {


    public ExportShopHouseAdapter(int layoutResId, List<ExportSaleHousesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExportSaleHousesBean exportSaleHousesBean) {
        CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_home),exportSaleHousesBean.imgUrl);
        holder.setText(R.id.home_name,exportSaleHousesBean.name);
        holder.setText(R.id.tv_home_position,exportSaleHousesBean.address);
        holder.setText(R.id.tv_home_time,"签约时间:"+exportSaleHousesBean.Time);
        holder.setText(R.id.tv_price,exportSaleHousesBean.mTotalPrice+"万   "+exportSaleHousesBean.averPrice+"元/㎡");
    }
}
