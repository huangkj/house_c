package com.easylife.house.customer.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.view.TimeUtils;

/**
 * 首页的消息列表
 */
public class MessageListAdapter extends BaseQuickAdapter<PushMsgBean, BaseViewHolder> {
    public MessageListAdapter() {
        super(R.layout.message_title_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, PushMsgBean item) {
        switch (item.msgType) {
            case 0:
                // 头条
                helper.setImageResource(R.id.ivIcon, R.mipmap.icon_push_dheadline);
                helper.setText(R.id.tvTitle, item.title);
                break;
            case 1:
                // 楼盘订阅
                helper.setImageResource(R.id.ivIcon, R.mipmap.icon_push_dev);
                helper.setText(R.id.tvTitle, item.title);
                break;
            case 7:
                // 跟进消息
                helper.setImageResource(R.id.ivIcon, R.mipmap.icon_push_deal);
                helper.setText(R.id.tvTitle, item.title);
                break;
            default:
                helper.setImageResource(R.id.ivIcon, R.mipmap.icon_push_sys_tip);
                helper.setText(R.id.tvTitle, TextUtils.isEmpty(item.title) ? "系统通知" : item.title);
                break;
        }
        helper.setText(R.id.tvContent, item.text);
        helper.setText(R.id.tvTime, TimeUtils.stampToDate(item.time, "HH:mm"));
    }
}
