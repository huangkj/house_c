package com.easylife.house.customer.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/4/5.
 * TA的动态
 */

public class ExportHisIdcAdapter extends BaseQuickAdapter<ExportHisIdcBean, BaseViewHolder> {


    public CommentImgOnclickListenear commentImgOnclickListenear;

    public void setCommentImgOnclickListenear(CommentImgOnclickListenear commentImgOnclickListenear) {
        this.commentImgOnclickListenear = commentImgOnclickListenear;
    }

    public interface CommentImgOnclickListenear {
        void setCommentOnclick(int position, ExportHisIdcBean exportHisDymanicBean);
    }

    public ExportHisIdcAdapter(int layoutResId, List<ExportHisIdcBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ExportHisIdcBean idcBean) {
        holder.setText(R.id.tv_dynamic,idcBean.name);
        CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_dynamic),idcBean.url);
        holder.getView(R.id.iv_dynamic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentImgOnclickListenear.setCommentOnclick(0,idcBean);
            }
        });
    }
}
