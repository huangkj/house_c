package com.easylife.house.customer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Mars on 2017/4/13 17:48.
 * 描述：
 */

public abstract class BaseSimpleAdapter<T, K> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseSimpleAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, T t) {
        init(baseViewHolder, t);
    }

    abstract void init(BaseViewHolder baseViewHolder, T t);
}
