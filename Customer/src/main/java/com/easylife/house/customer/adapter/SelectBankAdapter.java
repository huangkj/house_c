package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.ui.mine.card.BankCardDetailActivity;
import com.easylife.house.customer.util.ToastUtils;

import java.util.List;

public class SelectBankAdapter extends BaseQuickAdapter<BankCardListBean, BaseViewHolder> {
    private OnCheckedListener mOnCheckedListener;

    private int selectPos = -1;
    private int preSelectPos = -1;
    private boolean isRefund;
    private Activity activity;

    public SelectBankAdapter(Activity activity, int layoutResId, boolean isRefund, @Nullable List<BankCardListBean> data) {
        super(layoutResId, data);
        this.isRefund = isRefund;
        this.activity = activity;
    }

    public interface OnCheckedListener {
        void onChecked(BankCardListBean item);
    }

    public void setOnCheckedListener(OnCheckedListener l) {
        mOnCheckedListener = l;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final BankCardListBean item) {
        final TextView cbSelectBank = (TextView) helper.getView(R.id.cbSelectBank);
        cbSelectBank.setSelected(item.isSelect());

        helper.setGone(R.id.line_h, helper.getPosition() != getItemCount() - 1);

        helper.setText(R.id.tvBankName, item.getBelongToBank());
        String last4Num = item.getBankCardNum().substring(item.getBankCardNum().length() - 4, item.getBankCardNum().length());
        helper.setText(R.id.tvBankCard, "尾号" + last4Num + "储蓄卡");

        // 非本人名下银行卡
        if (!item.isSelf()) {
            // 退款申请跳转的银行卡选择页面，此时需要判断是否是本人银行卡
            if (isRefund) {
                // 退款只能退到当前用户名下的银行卡,否则置灰不可选择
                helper.setGone(R.id.tvTip, true);
                helper.setText(R.id.tvTip, "非本人银行卡，暂不可选");
                helper.setTextColor(R.id.tvBankName, Color.parseColor("#99535353"));
                helper.setTextColor(R.id.tvBankCard, Color.parseColor("#99535353"));
            } else {
                if (item.isComplete()) {
                    helper.setGone(R.id.tvTip, false);
                } else {
                    helper.setGone(R.id.tvTip, true);
                    helper.setText(R.id.tvTip, "信息不全");
                    helper.setGone(R.id.tvComplete, true);
                    helper.setTextColor(R.id.tvBankName, Color.parseColor("#ff535353"));
                    helper.setTextColor(R.id.tvBankCard, Color.parseColor("#ff535353"));
                }
            }
        } else {
            if (item.isComplete()) {
                helper.setGone(R.id.tvTip, false);
            } else {
                //  显示补充信息
                helper.setGone(R.id.tvTip, true);
                helper.setText(R.id.tvTip, "信息不全");
                helper.setGone(R.id.tvComplete, true);
                helper.setTextColor(R.id.tvBankName, Color.parseColor("#ff535353"));
                helper.setTextColor(R.id.tvBankCard, Color.parseColor("#ff535353"));
            }
        }
        helper.setOnClickListener(R.id.tvComplete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去补充信息
                if (isRefund && !item.isSelf())
                    return;
                if (item.isComplete())
                    return;
                BankCardDetailActivity.startActivity(activity, item.getId() + "", 1);
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelf()) {
                    ToastUtils.showShort(activity, "该银行卡绑定身份证号与身份认证不一致");
                    return;
                }
                if (!item.isComplete()) {
                    ToastUtils.showShort(activity, "银行卡信息不全，请前往补充");
                    return;
                }
                if (preSelectPos != -1) {
                    if (selectPos == helper.getLayoutPosition()) {
                        //点击的是同一个
                        selectPos = helper.getLayoutPosition();
                    } else {
                        //点击了不同的
                        preSelectPos = selectPos;
                        selectPos = helper.getLayoutPosition();
                        getData().get(preSelectPos).setSelect(false);
                    }
                } else {
                    //第一次进入 初始化
                    preSelectPos = helper.getLayoutPosition();
                    selectPos = helper.getLayoutPosition();
                }

                item.setSelect(true);

                notifyDataSetChanged();
                if (mOnCheckedListener != null) {
                    mOnCheckedListener.onChecked(item);
                }
            }
        });

    }
}
