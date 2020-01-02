package com.easylife.house.customer.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.SalingBean;
import com.easylife.house.customer.bean.SalingPersonItem;

import java.util.List;

/**
 * Created by zgm on 2017/4/17.
 */

public class ExpandableItemSaleAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TEXT_LIST = 1;
    public static final int TYPE_PERSON = 2;

    public ChildItemClickListenear childItemClickListenear;

    public interface ChildItemClickListenear {
        void click(MultiItemEntity item);
    }

    public void setChildItemClickListenear(ChildItemClickListenear childItemClickListenear) {
        this.childItemClickListenear = childItemClickListenear;
    }

    public ExpandableItemSaleAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TEXT_LIST, R.layout.house_res_list_item);
        addItemType(TYPE_PERSON, R.layout.house_res_list_person);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TEXT_LIST:
                final SalingBean salingBean = (SalingBean) item;
                holder.setText(R.id.tv_room, salingBean.salingTitle);
                holder.setText(R.id.tv_simple, salingBean.simple);
                holder.setText(R.id.tv_area, salingBean.farea);
                holder.setText(R.id.tv_price, salingBean.price);
                holder.setText(R.id.tv_can_sale, salingBean.count + "套可售");
                if (salingBean.isExpanded()) {
                    ((ImageView) holder.getView(R.id.iv_expand)).setImageResource(R.mipmap.houses_res_open);
                } else {
                    ((ImageView) holder.getView(R.id.iv_expand)).setImageResource(R.mipmap.houses_res_close);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (salingBean.isExpanded()) {
                            collapse(pos);
                            ((ImageView) holder.getView(R.id.iv_expand)).setImageResource(R.mipmap.houses_res_close);
                        } else {
                            expand(pos);
                            ((ImageView) holder.getView(R.id.iv_expand)).setImageResource(R.mipmap.houses_res_open);
                        }
                    }
                });
                break;
            case TYPE_PERSON:
                SalingPersonItem personItem = (SalingPersonItem) item;
                holder.setText(R.id.tv_room_person, personItem.title);
                holder.setText(R.id.tv_area_person, personItem.area);
                holder.setText(R.id.tv_price_person, personItem.price + "万元/套");
                int personPos = holder.getAdapterPosition();
                if (personPos == personItem.sum) {
                    holder.setVisible(R.id.btn_more, true);
                    //TODO 跳转房源更多列表  二期
                } else {
                    holder.setVisible(R.id.btn_more, false);
                }

                holder.getView(R.id.rl_person).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        childItemClickListenear.click(item);
                    }
                });
                break;
        }
    }
}
