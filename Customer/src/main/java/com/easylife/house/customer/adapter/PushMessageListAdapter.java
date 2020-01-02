package com.easylife.house.customer.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.view.TimeUtils;


/**
 * 内部消息列表数据
 */
public class PushMessageListAdapter extends BaseQuickAdapter<PushMsgBean, BaseViewHolder> {
    public PushMessageListAdapter() {
        super(R.layout.rcv_news_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, PushMsgBean item) {
        helper.setText(R.id.tv_time, TimeUtils.stampToDateWithDay(item.time));
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_content, item.msgType == 7 ? Html.fromHtml(item.text + "<font color=\"#5BBD6D\">去查看</font>") : item.text);
        helper.addOnClickListener(R.id.ll_system);
    }
}
