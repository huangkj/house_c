package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by zgm on 2017/3/29.
 * 网上预约
 */

public class NetAppointmentAdapter extends BaseQuickAdapter<NetAppointmentBean, BaseViewHolder> {

    public ItemChildOnclickListenear itemChildOnClickListenear;
    public interface ItemChildOnclickListenear{
        void onClickPhone(String phoneNum);

        void onCancleClick(String devId);

        void onShopClick(String brokeCode);

        void showQRdialog(NetAppointmentBean netAppointmentBean);
    }
    public void setItemChildOnclickListenear(ItemChildOnclickListenear itemChildOnClickListenear){
        this.itemChildOnClickListenear = itemChildOnClickListenear;
    }

    public NetAppointmentAdapter(int layoutResId, List<NetAppointmentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NetAppointmentBean netAppointmentBean) {

        if(netAppointmentBean !=null && netAppointmentBean.dev != null){
            if(netAppointmentBean.dev.effectId != null && netAppointmentBean.dev.effectId.size() != 0){
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_house),netAppointmentBean.dev.effectId.get(0).thumbnailImage);
            }else {
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_house),null);
            }
            if(netAppointmentBean.dev != null){
                holder.setText(R.id.tv_house_name,netAppointmentBean.dev.devName);
                if(!TextUtils.isEmpty(netAppointmentBean.dev.devSquareMetre)){
                    holder.setText(R.id.tv_area,netAppointmentBean.dev.devSquareMetre+"㎡");
                }

                if(!TextUtils.isEmpty(netAppointmentBean.dev.averPrice)){
                    holder.setText(R.id.tv_price,netAppointmentBean.dev.averPrice+"元/㎡");
                }

                holder.setText(R.id.tv_room,netAppointmentBean.dev.devBedroom);
                holder.setText(R.id.tv_address,netAppointmentBean.dev.addressDistrict+"-"+netAppointmentBean.dev.addressTown+"   "+netAppointmentBean.dev.propertyType);
                if(!TextUtils.isEmpty(netAppointmentBean.look.lookTime)){
                    holder.setText(R.id.tv_name_value, CustomerUtils.dateTransSdfMinutes(Long.parseLong(netAppointmentBean.look.lookTime)));
                }
                holder.setText(R.id.tv_look_local_value, netAppointmentBean.dev.saleAddressDistrict+netAppointmentBean.dev.saleAddressTown+netAppointmentBean.dev.saleAddressDetail);
            }

            if(netAppointmentBean.look != null){
                switch (netAppointmentBean.look.status){
                    case 0:
                        holder.setVisible(R.id.tv_certificate,true);
                        holder.setText(R.id.tv_book,"预约成功");
                        holder.setBackgroundRes(R.id.iv_time,R.mipmap.clock);
                        holder.setBackgroundRes(R.id.tv_book,R.drawable.comment_flow_text_bg);
                        holder.setTextColor(R.id.tv_book,mContext.getResources().getColor(R.color.gradient_end));
                        holder.getView(R.id.rl_export).setVisibility(View.VISIBLE);
                        holder.getView(R.id.rl_no).setVisibility(View.GONE);
                        if(netAppointmentBean.broker != null){
                            CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head),netAppointmentBean.broker.headImg);
                        }else {
                            CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head),null);
                        }
                        holder.setText(R.id.team_name,netAppointmentBean.broker.name);
                        if(TextUtils.isEmpty(netAppointmentBean.broker.favorableRate)){
                            holder.setText(R.id.team_good_value,"100%");
                        }else {
                            holder.setText(R.id.team_good_value,netAppointmentBean.broker.favorableRate+"%");
                        }
                        holder.setText(R.id.team_look_value,netAppointmentBean.broker.count+"次");
                        holder.getView(R.id.team_phone).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemChildOnClickListenear.onClickPhone(netAppointmentBean.broker.phone);
                            }
                        });

                        holder.getView(R.id.tv_certificate).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemChildOnClickListenear.showQRdialog(netAppointmentBean);
                            }
                        });
                        break;
                    case 1:
                        holder.setVisible(R.id.tv_certificate,false);
                        holder.setText(R.id.tv_book,"正在预约");
                        holder.setBackgroundRes(R.id.iv_time,R.mipmap.clock);
                        holder.setBackgroundRes(R.id.tv_book,R.drawable.comment_flow_text_bg_blue);
                        holder.setTextColor(R.id.tv_book,mContext.getResources().getColor(R.color._73144226));
                        holder.getView(R.id.rl_export).setVisibility(View.GONE);
                        holder.getView(R.id.rl_no).setVisibility(View.VISIBLE);

                        holder.getView(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemChildOnClickListenear.onCancleClick(netAppointmentBean.dev.id);
                            }
                        });
                        break;
                    case 2:
                        holder.setVisible(R.id.tv_certificate,false);
                        holder.setText(R.id.tv_book,"已取消");
                        holder.setBackgroundRes(R.id.iv_time,R.mipmap.clock);
                        holder.setBackgroundRes(R.id.tv_book,R.drawable.comment_flow_text_bg);
                        holder.setTextColor(R.id.tv_book,mContext.getResources().getColor(R.color.gradient_end));
                        holder.getView(R.id.rl_export).setVisibility(View.GONE);
                        holder.getView(R.id.rl_no).setVisibility(View.GONE);
                        break;
                    case 7:
                        holder.setVisible(R.id.tv_certificate,false);
                        holder.setText(R.id.tv_book,"失效");
                        holder.setBackgroundRes(R.id.iv_time,R.mipmap.clock);
                        holder.setBackgroundRes(R.id.tv_book,R.drawable.comment_flow_text_bg_blue);
                        holder.setTextColor(R.id.tv_book,mContext.getResources().getColor(R.color._73144226));
                        holder.getView(R.id.rl_export).setVisibility(View.GONE);
                        holder.getView(R.id.rl_no).setVisibility(View.GONE);
                        break;
                }
            }


            holder.getView(R.id.rl_export_shop).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(netAppointmentBean.broker != null)
                    itemChildOnClickListenear.onShopClick(netAppointmentBean.broker.brokerCode);
                }
            });

        }




    }
}
