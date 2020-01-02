package com.easylife.house.customer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.util.DateUtil;

import java.util.List;

/**
 * Created by Mars on 2017/4/1 17:04.
 * 描述：消息头条
 */

public class HeadLinesAdapter extends BaseQuickAdapter<MsgHeadLine, BaseViewHolder> {

    public HeadLinesAdapter(int layoutResId, List<MsgHeadLine> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MsgHeadLine messageBean) {
        baseViewHolder.setText(R.id.tvHeadLineTitle, messageBean.title);
        baseViewHolder.setText(R.id.tvHeadLineDate,
                DateUtil.phpToDate(messageBean.createTime, "yyyy-MM-dd HH:mm:ss"));
    }
}
