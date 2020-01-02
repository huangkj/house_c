package com.easylife.house.customer.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/4/5.
 * TA的动态
 */

public class ExportHisDynamicAdapter extends BaseQuickAdapter<ExportHisDymanicBean, BaseViewHolder> {


    public CommentImgOnclickListenear commentImgOnclickListenear;

    public void setCommentImgOnclickListenear(CommentImgOnclickListenear commentImgOnclickListenear) {
        this.commentImgOnclickListenear = commentImgOnclickListenear;
    }

    public interface CommentImgOnclickListenear {
        void setCommentOnclick(int position,ExportHisDymanicBean exportHisDymanicBean);
    }

    public ExportHisDynamicAdapter(int layoutResId, List<ExportHisDymanicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ExportHisDymanicBean exportHisDymanicBean) {
        holder.setText(R.id.tv_dynamic,exportHisDymanicBean.title+":"+exportHisDymanicBean.content);
        if(exportHisDymanicBean.contentUrl != null){
            switch (exportHisDymanicBean.contentUrl.size()){
                case 0:
                    holder.getView(R.id.iv_dynamic).setVisibility(View.GONE);
                    holder.getView(R.id.iv_dynamic1).setVisibility(View.GONE);
                    break;
                case 1:
                    holder.getView(R.id.iv_dynamic).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_dynamic1).setVisibility(View.GONE);
                    CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_dynamic),exportHisDymanicBean.contentUrl.get(0).url);
                    break;
                case 2:
                    holder.getView(R.id.iv_dynamic).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_dynamic1).setVisibility(View.VISIBLE);
                    CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_dynamic),exportHisDymanicBean.contentUrl.get(0).url);
                    CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_dynamic1),exportHisDymanicBean.contentUrl.get(1).url);
                    break;
            }

            holder.getView(R.id.iv_dynamic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentImgOnclickListenear.setCommentOnclick(0,exportHisDymanicBean);
                }
            });

            holder.getView(R.id.iv_dynamic1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentImgOnclickListenear.setCommentOnclick(1,exportHisDymanicBean);
                }
            });
        }else {
            holder.getView(R.id.iv_dynamic).setVisibility(View.GONE);
            holder.getView(R.id.iv_dynamic1).setVisibility(View.GONE);
        }
    }
}
