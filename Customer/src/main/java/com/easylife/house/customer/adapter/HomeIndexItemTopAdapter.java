package com.easylife.house.customer.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemBean;

import java.util.List;

/**
 * Created by zgm on 2017/9/4/004.
 * 首页顶部栏 adapter
 */

public class HomeIndexItemTopAdapter extends BaseQuickAdapter<ItemBean, BaseViewHolder> {

    public HomeIndexItemTopAdapter(int layoutResId, List<ItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ItemBean itemBean) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
       LinearLayout ll_item = holder.getView(R.id.ll_item);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width/4, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_item.setLayoutParams(params);

        holder.setImageResource(R.id.image,itemBean.imgUrl);
        holder.setText(R.id.name,itemBean.name);
    }
}
