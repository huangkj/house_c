package com.easylife.house.customer.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.DevFavorable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2017/6/28 19:11.
 * 描述：优惠显示列表
 */

public class FavorableShowAdapter extends BaseQuickAdapter<DevFavorable.Favorable, BaseViewHolder> {
    public FavorableShowAdapter(int layoutResId, List<DevFavorable.Favorable> data) {
        super(layoutResId, data);
        favorables = new ArrayList<>();
    }

    private List<DevFavorable.Favorable> favorables;

    public List<DevFavorable.Favorable> getSelectedItems() {
        return favorables;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final DevFavorable.Favorable favorable) {
        baseViewHolder.setText(R.id.cbSelect,
                Html.fromHtml(
                        favorable.key +
                                "<font   color=\"#FF6800\">" +
                                favorable.value +
                                "折</font>"));
    }
}
