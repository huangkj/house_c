package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.List;

/**
 * Created by zgm on 2017/3/27.
 */

public class ExportListAdapter extends BaseQuickAdapter<ExportListBean, BaseViewHolder> {


    private String[] tags;

    public ExportListAdapter(int layoutResId, List<ExportListBean> data) {
        super(layoutResId, data);
    }

    public void setOnPhoneClickLisenear(onPhoneClickLisenear onPhoneClickLisenear) {
        this.onPhoneClickLisenear = onPhoneClickLisenear;
    }

    public onPhoneClickLisenear onPhoneClickLisenear;

    public interface onPhoneClickLisenear {
        void phoneClick(String phone);

        void exportPage(String brokeCode, String phone);

        void imChat(String imUsername, String brokeCode, String brokerName, String phone);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ExportListBean exportListBean) {
        holder.setText(R.id.tv_username, exportListBean.name);
        holder.setText(R.id.tv_good, exportListBean.favorableRate + "%");
        holder.setText(R.id.tv_look, exportListBean.count + "次");
        holder.setRating(R.id.rb_star, exportListBean.level);
        FlowViewGroup flowViewGroup = (FlowViewGroup) holder.getView(R.id.export_flowgroup);
        if (!TextUtils.isEmpty(exportListBean.tag)) {
            tags = exportListBean.tag.split(",");
            for (int i = 0; i < tags.length; i++) {
                TextView text = (TextView) LayoutInflater.from(mContext).inflate(R.layout.export_item_flow_comment, flowViewGroup, false);
                text.setText(tags[i]);
                flowViewGroup.addView(text);
            }
        }

        holder.getView(R.id.team_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneClickLisenear.phoneClick(exportListBean.phone);
            }
        });

        holder.getView(R.id.rl_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneClickLisenear.exportPage(exportListBean.brokerCode, exportListBean.phone);
            }
        });

        CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.iv_head), exportListBean.headImg);

//        if (exportListBean.im == null || TextUtils.isEmpty(exportListBean.im)) {
//            holder.getView(R.id.iv_team_chat).setVisibility(View.GONE);
//            holder.getView(R.id.line).setVisibility(View.GONE);
//        } else {
//            holder.getView(R.id.iv_team_chat).setVisibility(View.VISIBLE);
//            holder.getView(R.id.line).setVisibility(View.VISIBLE);
//        }

        holder.getView(R.id.iv_team_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exportListBean.im == null || TextUtils.isEmpty(exportListBean.im)) {
                    ToastUtils.showShort("该经纪人暂不支持聊天功能");
                } else {
                    onPhoneClickLisenear.imChat(exportListBean.im, exportListBean.brokerCode, exportListBean.name, exportListBean.phone);
                }
            }
        });
    }
}
