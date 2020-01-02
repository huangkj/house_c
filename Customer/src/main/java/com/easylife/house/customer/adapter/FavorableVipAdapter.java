package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.FavorableVip;

import java.util.List;

/**
 * Created by Mars on 2017/6/29 20:00.
 * 描述：会员优惠列表，-认筹，单选
 */

public class FavorableVipAdapter extends BaseQuickAdapter<FavorableVip, BaseViewHolder> {
    public FavorableVipAdapter(Activity activity, int layoutResId, List<FavorableVip> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    private Activity activity;
    private ItemClickListener<FavorableVip> listener;

    public void setItemClickListener(ItemClickListener<FavorableVip> listener) {
        this.listener = listener;
    }

    private String idSelected;

    public String getIdSelected() {
        return idSelected;
    }

    public void setIdSelected(String id){
        idSelected = id;
    }

    public FavorableVip getSelectedFavorable() {
        if (TextUtils.isEmpty(idSelected))
            return null;
        for (FavorableVip vip : getData()) {
            if (idSelected.equals(vip.id)) {
                return vip;
            }
        }
        return null;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final FavorableVip favorableVip) {
        baseViewHolder.setText(R.id.cbSelect, favorableVip.privilege + "  适用于  " + favorableVip.scope);
        final TextView cb = baseViewHolder.getView(R.id.cbSelect);

        Drawable drawable = activity.getResources().getDrawable(favorableVip.id.equals(idSelected) ? R.mipmap.check : R.mipmap.check_n);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        cb.setCompoundDrawables(drawable, null, null, null);

        baseViewHolder.setOnClickListener(R.id.cbSelect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idSelected = favorableVip.id;
                notifyDataSetChanged();
                listener.itemClick(R.id.cbSelect, 0, favorableVip);
            }
        });
    }
}
