package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：楼盘列表
 */

public class HouseCollectAdapter extends BaseQuickAdapter<HousesDetailBaseBean, BaseViewHolder> {

    public HouseCollectAdapter(int layoutResId, List<HousesDetailBaseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HousesDetailBaseBean app) {

        TextView tvName = baseViewHolder.getView(R.id.tvHouseName);
        tvName.setText(app.devName);

        ImageView imgCover = baseViewHolder.getView(R.id.imgCover);
        if (app.effectId != null && app.effectId.size() != 0) {
            CacheManager.initImageClientList(mContext, imgCover, app.effectId.get(0).url);
        }

        // 明房源
        baseViewHolder.setGone(R.id.tvCoverTag, "1".equals(app.isTransparent));

        String text = "";
        if (!TextUtils.isEmpty(app.addressDistrict))
            text += app.addressDistrict;
        if (!TextUtils.isEmpty(app.propertyType))
            text += "  |  " + app.propertyType;
        if (!TextUtils.isEmpty(app.devSquareMetre))
            text += "  |  建面" + app.devSquareMetre + "㎡";
        baseViewHolder.setText(R.id.tvLocation, text);

        FlowViewGroup flowViewGroup = baseViewHolder.getView(R.id.groupTags);
        flowViewGroup.removeAllViews();
        if (TextUtils.isEmpty(app.tag)) {
            flowViewGroup.removeAllViews();
        } else {
            String[] tagSplit = app.tag.split(",");
            if (tagSplit != null)
                // 最多显示3个，保证展示一行
                for (int i = 0; i < (tagSplit.length > 3 ? 3 : tagSplit.length); i++) {
                    TextView text1 = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag_orange, flowViewGroup, false);
                    text1.setText(tagSplit[i]);
                    flowViewGroup.addView(text1);
                }
        }

        TextView tvPrice = baseViewHolder.getView(R.id.tvPrice);
        tvPrice.setText(PriceUtil.formatPriceUnit(app.averPrice, "元/㎡"));


        baseViewHolder.setGone(R.id.line, baseViewHolder.getPosition() != getItemCount() - 1);
    }
}
