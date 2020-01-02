package com.easylife.house.customer.adapter;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;

import java.util.List;

/**
 * Created by Mars on 2017/3/28 10:11.
 * 描述：周边配套搜索结果展示列表
 */

public class PoiItemAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    public PoiItemAdapter(int layoutResId, List<PoiItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PoiItem poiItem) {
        baseViewHolder.setText(R.id.tvName, poiItem.getTitle());
        baseViewHolder.setText(R.id.tvDistance, poiItem.getDistance() + "米");
    }
}
