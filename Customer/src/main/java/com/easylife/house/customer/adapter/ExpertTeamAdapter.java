package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.config.CacheManager;

import java.util.List;

/**
 * Created by zgm on 2017/3/22.
 * 详情页专家
 */

public class ExpertTeamAdapter extends BaseQuickAdapter<ExportListBean, BaseViewHolder> {


    public onPhoneClickLisenear onPhoneClickLisenear;

    public void setOnPhoneClickLisenear(onPhoneClickLisenear onPhoneClickLisenear){
        this.onPhoneClickLisenear = onPhoneClickLisenear;
    }



    public interface onPhoneClickLisenear{
        void phoneClick(String phone);

        void exportPage(String brokeCode,String headUrl,String phone);

        void imChat(String imUsername,String brokeCode,String brokerName, String phone);
    }
    public ExpertTeamAdapter(int layoutResId, List<ExportListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ExportListBean expertTeamBean) {

        holder.setText(R.id.team_good_value,expertTeamBean.favorableRate+"%");
        holder.setText(R.id.team_look_value,expertTeamBean.count+"次");
        holder.setText(R.id.team_name,expertTeamBean.name);
        CacheManager.initImageUserHeader(mContext,(ImageView) holder.getView(R.id.team_head),expertTeamBean.headImg);

        holder.getView(R.id.team_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneClickLisenear.phoneClick(expertTeamBean.phone);
            }
        });


        holder.getView(R.id.rl_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneClickLisenear.exportPage(expertTeamBean.brokerCode,expertTeamBean.headImg,expertTeamBean.phone);
            }
        });

//        if(expertTeamBean.im == null || TextUtils.isEmpty(expertTeamBean.im)){
//            holder.getView(R.id.iv_team_chat).setVisibility(View.GONE);
//            holder.getView(R.id.line).setVisibility(View.GONE);
//        }else {
//            holder.getView(R.id.iv_team_chat).setVisibility(View.VISIBLE);
//            holder.getView(R.id.line).setVisibility(View.VISIBLE);
//        }

        holder.getView(R.id.iv_team_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expertTeamBean.im == null || TextUtils.isEmpty(expertTeamBean.im)) {
                    ToastUtils.showShort("该经纪人暂不支持聊天功能");
                } else {

                    onPhoneClickLisenear.imChat(expertTeamBean.im, expertTeamBean.brokerCode, expertTeamBean.name, expertTeamBean.phone);
                }
            }
        });
    }
}
