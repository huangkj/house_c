package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/4/5.
 * 成交房源
 */

public class ExportCompleteHomeAdapter extends BaseQuickAdapter<ExportCompeleteHomeBean, BaseViewHolder> {

    public ExportCompleteHomeAdapter(int layoutResId, List<ExportCompeleteHomeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExportCompeleteHomeBean exportShopSaleHousesBean) {

        if(!TextUtils.isEmpty(exportShopSaleHousesBean.imgUrl)){
            CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_home),exportShopSaleHousesBean.imgUrl);
        }
        holder.setText(R.id.home_name,exportShopSaleHousesBean.houseName +"   "+exportShopSaleHousesBean.buildName);
        holder.setText(R.id.tv_home_position,exportShopSaleHousesBean.toward + "  "+exportShopSaleHousesBean.buildType+"  "+exportShopSaleHousesBean.fArea+"㎡");
        holder.setText(R.id.tv_home_time,"签约时间:缺少字段");
        holder.setText(R.id.tv_price,exportShopSaleHousesBean.mTotalPrice+"万   "+exportShopSaleHousesBean.mUnitPrice+"元/㎡");
    }
}
