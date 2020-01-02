package com.easylife.house.customer.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.Record;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by Mars on 2017/10/18 15:51.
 * 描述：客户列表适配器
 */

public class RecordListAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {

    public String type;
    private ItemClickListener<Record> listener;

    public RecordListAdapter(int layoutResId, String type, List<Record> data) {
        super(layoutResId, data);
        this.type = type;
    }

    public void setItemClickListener(ItemClickListener<Record> listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Record record) {
        baseViewHolder.setText(R.id.tvCustomerName, record.name);
        baseViewHolder.setText(R.id.tvPhone, CustomerUtils.splitPhoneNum(record.phone));
        baseViewHolder.setText(R.id.tvDevName, record.devName);
        baseViewHolder.setText(R.id.tvRate, record.rate);
        // TODO 过期判断
//        baseViewHolder.setText(R.id.tvDate,record.tvDate);
//        baseViewHolder.setText(R.id.tvDateArrived,record.arrivedData);

        if (type == null) {
        } else {
            if (type.equals(Customer.TYPE_RECORD_RECOMMEND) || type.equals(Customer.TYPE_RECORD_ARRIVED)) {
                // 推荐，已到访-已过期   显示再报备   TODO 未判断过期
                baseViewHolder.setVisible(R.id.btnRecord, true);
                baseViewHolder.setText(R.id.btnRecord, "再报备");
            } else if (type.equals(Customer.TYPE_RECORD_PAID) || type.equals(Customer.TYPE_RECORD_SIGNED)) {
                // 已认购，已签约-可提现        显示结佣   TODO 未判断过期
                baseViewHolder.setVisible(R.id.btnRecord, true);
                baseViewHolder.setText(R.id.btnRecord, "结佣");
            } else if (type.equals(Customer.TYPE_RECORD_REFUND)) {
                //　退房                   不显示第三个按钮
                baseViewHolder.setVisible(R.id.btnRecord, false);
            }
        }

        baseViewHolder.setOnClickListener(R.id.layContent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.itemClick(R.id.layContent, 0, record);
            }
        });
        baseViewHolder.setOnClickListener(R.id.btnSms, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发短信
                if (listener != null)
                    listener.itemClick(R.id.btnSms, 1, record);
            }
        });
        baseViewHolder.setOnClickListener(R.id.btnCall, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打电话
                if (listener != null)
                    listener.itemClick(R.id.btnCall, 2, record);
            }
        });
        baseViewHolder.setOnClickListener(R.id.btnRecord, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 再报备 或者 结佣
                if (listener != null) {
                    // TODO 未判断过期
                    if (type.equals(Customer.TYPE_RECORD_RECOMMEND) || type.equals(Customer.TYPE_RECORD_ARRIVED)) {
                        // 推荐，已到访-已过期   显示再报备   TODO
                        listener.itemClick(R.id.btnRecord, 3, record);
                    } else if (type.equals(Customer.TYPE_RECORD_PAID) || type.equals(Customer.TYPE_RECORD_SIGNED)) {
                        // 已认购，已签约-可提现        显示结佣   TODO
                        listener.itemClick(R.id.btnRecord, 4, record);
                    }
                }
            }
        });
    }

}
