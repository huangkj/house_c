/*
package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.OperaterRecordBean;




public class OperaterRecordAdapter extends BaseRecycleAdapter<OperaterRecordBean> {

    public OperaterRecordAdapter(Activity activity) {
        super(activity, null);
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position, final OperaterRecordBean item) {

        // operation;// 1, "待审核",2, "运营审核通过",3, "财务审核通过" 4, "运营审核拒绝" 5, "财务审          核拒绝"
        holder.setText(R.id.tvDate, item.getCreateTimeStr());
        holder.setText(R.id.tvOperator, item.getSubmitPersonName());
        if (item.getOperation() == 1) {
            holder.setText(R.id.tvStatus, "待审核");
        } else if (item.getAuditStatus() == 2) {
            holder.setText(R.id.tvStatus, "运营审核通过");
        } else if (item.getAuditStatus() == 3) {
            holder.setText(R.id.tvStatus, "财务审核通过");
        } else if (item.getAuditStatus() == 4) {
            holder.setText(R.id.tvStatus, "运营审核拒绝");
        } else if (item.getAuditStatus() == 5) {
            holder.setText(R.id.tvStatus, "财务审核拒绝");
        }


//        holder.setText(R.id.tvStatus, item.getOperationText());
        if (TextUtils.isEmpty(item.getRemark())) {
            holder.setVisible(R.id.tvReason, View.INVISIBLE);
        } else {
            holder.setVisible(R.id.tvReason, View.VISIBLE);
        }
        holder.getView(R.id.tvReason).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.getRemark()) && itemListener != null)
                    itemListener.onClick(R.id.tvReason, position, item);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_record_check;
    }
}
*/
