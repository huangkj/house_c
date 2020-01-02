package com.easylife.house.customer.adapter;

import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ResultRate;

import java.util.List;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：选择利率
 */

public class RateSelectAdapter extends BaseQuickAdapter<ResultRate, BaseViewHolder> {

    public RateSelectAdapter(int layoutResId, List<ResultRate> data) {
        super(layoutResId, data);
    }

    public RateSelectAdapter(int layoutResId, List<ResultRate> data, String rateNum) {
        super(layoutResId, data);
        this.rateNum = rateNum;
    }

    private String rateNum;

    public void setRateNum(String rateNum) {
        this.rateNum = rateNum;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ResultRate itemSelect) {
        if (isEqual(itemSelect.rateNum, rateNum)) {
            baseViewHolder.setTextColor(R.id.name, mContext.getResources().getColor(R.color.colorPrimary));
            baseViewHolder.setVisible(R.id.check, true);
        } else {
            baseViewHolder.setVisible(R.id.check, false);
            baseViewHolder.setTextColor(R.id.name, mContext.getResources().getColor(R.color.text_normal));
        }
        TextView name = baseViewHolder.getView(R.id.name);
        name.setGravity(Gravity.LEFT);
        name.setText(itemSelect.rateName);
    }

    private boolean isEqual(String rateNumCurrent, String rateNum) {
        try {
            float curRate = Float.parseFloat(rateNumCurrent);
            float rate = Float.parseFloat(rateNum);
            return rate == curRate;
        } catch (Exception e) {
            return false;
        }
    }
}
