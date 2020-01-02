package com.easylife.house.customer.adapter;

import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;

import java.util.List;

/**
 * Created by Mars on 2017/3/28 10:11.
 * 描述：
 */

public class CloudItemAdapter extends BaseQuickAdapter<CloudItem, BaseViewHolder> {

    public CloudItemAdapter(int layoutResId, List<CloudItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CloudItem poiItem) {
        TextView name = baseViewHolder.getView(R.id.tvName);
        name.setText(poiItem.getTitle());
        TextView distance = baseViewHolder.getView(R.id.tvDistance);
        distance.setText("距离" + poiItem.getDistance() + "米");
    }
}
