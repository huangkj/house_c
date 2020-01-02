package com.easylife.house.customer.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.SelectBrokerOrderBean;
import com.easylife.house.customer.util.CommonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class SelectOrderAdapter extends BaseQuickAdapter<SelectBrokerOrderBean, BaseViewHolder> {
    private OnCheckedChangedListener mOnCheckedChangedListener;

    public SelectOrderAdapter(int layoutResId, @Nullable List<SelectBrokerOrderBean> data) {
        super(layoutResId, data);
    }

    public interface OnCheckedChangedListener {
        void onCheckedChanged(SelectBrokerOrderBean item);
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener l) {
        mOnCheckedChangedListener = l;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final SelectBrokerOrderBean item) {
        final TextView tvSelectOrder = (TextView) helper.getView(R.id.cbSelectOrder);

        /*
         *
         *         <!--tvUsernameOrder  tvHouseInfo  tvDateOrderItem  tvShouldBrokerage-->

         * */

        helper.setText(R.id.tvUsernameOrder, item.getCustomerName());
        helper.setText(R.id.tvHouseInfo, item.getDevName() + "(" + item.getHouseNum() + ")");
        helper.setText(R.id.tvDateOrderItem, TimeUtils.millis2String(item.getCreatTime(), new SimpleDateFormat("yyyy/MM/dd")));
        helper.setText(R.id.tvShouldBrokerage, MoneyUtils.moneyFormat3(item.getShouldAmount()));
        helper.setText(R.id.tvPhoneNumberOrder, CommonUtils.hidePhoneNumber(item.getCustomerPhone()));


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getConfirmState() != 2) {
                    new MaterialDialog.Builder(mContext).title("提示").content("未进行交易及佣金确认，请联系好生活经服经理").
                            canceledOnTouchOutside(true).cancelable(true).positiveText("知道了").show();
                    return;
                }
                item.isSelect = !item.isSelect;
                notifyDataSetChanged();
                if (mOnCheckedChangedListener != null) {
                    mOnCheckedChangedListener.onCheckedChanged(item);
                }
            }
        });

        //  订单是否已确定
        if (item.getConfirmState() == 2) {
            helper.itemView.setBackgroundResource(R.drawable.select_order_item_bg);
        } else {
            helper.itemView.setBackgroundResource(R.drawable.select_order_item_false_bg);
        }


        tvSelectOrder.setSelected(item.isSelect);

    }


    /**
     * 全选
     *
     * @param isAll
     */
    public void selectAll(boolean isAll) {
        for (SelectBrokerOrderBean bean :
                getData()) {
            if (bean.confirmState == 2)
                bean.isSelect = isAll;
        }
        notifyDataSetChanged();
    }

    public int getSelectSize() {
        List<SelectBrokerOrderBean> data = getData();
        int size = 0;
        for (SelectBrokerOrderBean bean :
                data) {
            if (bean.isSelect) {
                size++;
            }
        }
        return size;
    }
}
