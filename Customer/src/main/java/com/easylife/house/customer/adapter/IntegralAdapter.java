package com.easylife.house.customer.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.IntegralBean;

import java.text.SimpleDateFormat;
import java.util.List;

public class IntegralAdapter extends BaseMultiItemQuickAdapter<IntegralBean, BaseViewHolder> {
    public final static int TYPE_DATA = 1;
    public final static int TYPE_STICKY_HEAD = 2;
    public final static int TYPE_SMALL_STICKY_HEAD_WITH_DATA = 3;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public IntegralAdapter(List<IntegralBean> data) {
        super(data);
        addItemType(TYPE_DATA, R.layout.integral_item);
        addItemType(TYPE_STICKY_HEAD, R.layout.integral_head);
        addItemType(TYPE_SMALL_STICKY_HEAD_WITH_DATA, R.layout.integral_head);
    }


    @Override
    protected void convert(BaseViewHolder helper, IntegralBean item) {
        int adapterPosition = helper.getAdapterPosition();
        switch (helper.getItemViewType()) {


            case TYPE_STICKY_HEAD:
//                helper.setText(R.id.tvDateIntegralHead, "TYPE_STICKY_HEAD   " + adapterPosition);
                helper.setText(R.id.tvDateIntegralHead, item.getDate());
                break;

            case TYPE_DATA:
                helper.setText(R.id.tvSignIntegral, item.getBehavior_name());

                switch (item.getState()) {
                    //增加
                    case 0:
                        helper.setText(R.id.tvChangeIntegral, "+" + item.getPoint());
                        break;
                    default:
                        helper.setText(R.id.tvChangeIntegral, "-" + item.getPoint());
                        break;
                    //减少
                }


                helper.setText(R.id.tvSaveIntegral, "余额:" + item.getBalance_point());
                String time = TimeUtils.millis2String(item.getTime(), new SimpleDateFormat("yyyy-MM-dd"));
                helper.setText(R.id.tvMonthIntegral, time.substring(8));
//                helper.setText(R.id., "11");
                break;
            case TYPE_SMALL_STICKY_HEAD_WITH_DATA:
                helper.setText(R.id.tvDateIntegralHead, item.getDate());
//                helper.setText(R.id.tvDateIntegralHead, "TYPE_SMALL_STICKY_HEAD_WITH_DATA   " + adapterPosition);
                break;


        }
    }
}
