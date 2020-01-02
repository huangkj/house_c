package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.DiscountListBean;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseQuickAdapter<DiscountListBean, BaseViewHolder> {



    public DiscountAdapter(@Nullable List<DiscountListBean> data) {
        super(R.layout.youhuixinxi_item, data);
//        int screenWidth = ScreenUtils.getScreenWidth();
//        itemWidth = (screenWidth - SizeUtils.dp2px(55)) / 2;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscountListBean item) {

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout itemView = (RelativeLayout) helper.itemView;
//        itemView.setLayoutParams(layoutParams);

        helper.setText(R.id.tvYouhuiTitle, item.getPrivilege());
        helper.setText(R.id.tvYouhuiDesc, "适用于:  " + item.getScope());
        helper.setText(R.id.tvGetCount, item.getCount() + "人已领取");

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }
}
