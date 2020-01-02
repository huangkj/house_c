package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportUserCommentBean;

import java.util.List;

/**
 * Created by zgm on 2017/4/5.
 * 用户评价列表
 */

public class UserCommentListAdapter extends BaseQuickAdapter<ExportUserCommentBean, BaseViewHolder> {


    public UserCommentListAdapter(int layoutResId, List<ExportUserCommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExportUserCommentBean exportUserCommentBean) {
        holder.setText(R.id.tv_comment_name, exportUserCommentBean.customerName);
        holder.setText(R.id.tv_comment_content, exportUserCommentBean.customerComent);
        ((RatingBar) holder.getView(R.id.export_content_star)).setRating(exportUserCommentBean.score);
        holder.setText(R.id.tv_comment_time, exportUserCommentBean.createTime);
        if (!TextUtils.isEmpty(exportUserCommentBean.brokerReply)) {
            holder.setVisible(R.id.tv_reply, true);
            holder.setText(R.id.tv_reply, "经济人回复:" + exportUserCommentBean.brokerReply);
        } else {
            holder.setVisible(R.id.tv_reply, false);
        }
    }
}
