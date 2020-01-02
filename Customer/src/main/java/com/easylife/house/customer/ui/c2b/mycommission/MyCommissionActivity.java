package com.easylife.house.customer.ui.c2b.mycommission;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Commission;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DialogUtil;

import butterknife.Bind;


/**
 * 我的佣金
 */
public class MyCommissionActivity extends MVPBaseActivity<MyCommissionContract.View, MyCommissionPresenter> implements MyCommissionContract.View {


    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.tvMoneyWait)
    TextView tvMoneyWait;
    @Bind(R.id.tvExpensesWait)
    TextView tvExpensesWait;
    @Bind(R.id.tvCommissionAll)
    TextView tvCommissionAll;
    @Bind(R.id.tvCommissionPaid)
    TextView tvCommissionPaid;
    @Bind(R.id.tvCommissionWait)
    TextView tvCommissionWait;
    @Bind(R.id.layDataContent)
    LinearLayout layDataContent;

    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, MyCommissionActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void showDetail(Commission commission) {
        this.mCommission = commission;
        if (commission == null) {
            layDataContent.setVisibility(View.GONE);
        } else {
            layDataContent.setVisibility(View.VISIBLE);

            tvCommissionAll.setText(commission.yingjieyongjine);//应结佣金额
            tvCommissionPaid.setText(commission.yijieyongjine);// 已结佣金额
            tvCommissionWait.setText(commission.daijieyongjine);// 待结佣金额
            tvMoney.setText(commission.yishoukuanjine);// 已收款金额
            tvMoneyWait.setText(commission.daishoukuanjine);//代收款金额
            tvExpensesWait.setText(commission.yikoujianshuifei);// 已扣减税费
        }
    }

    @Override
    public void withdraw(Commission commission) {
        // TODO  提现
    }

    @Override
    public void showMenu() {
        // TODO  显示弹窗菜单
        if (mCommission == null) {
            // 只显示银行卡
        } else {
            // 同时显示收款明细
        }
    }

    private DialogUtil mDialog;
    private PopupWindow pop;

    private void initMenu() {
        mDialog = new DialogUtil(this);
        final int[] location = new int[2];
        btnRight.getLocationOnScreen(location);
//        pop = mDialog.getRecordMenuPop(btnRight, new DialogUtil.OnEventClickListener() {
//            @Override
//            public void click(int position) {
//            }
//        });
    }
    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_commission, null);
    }

    private Commission mCommission;

    @Override
    protected void initView() {
        mPresenter.getNetData();
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.menu_black);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   弹窗菜单
                showMenu();
            }
        });
    }

    @Override
    protected void tryRequestData() {

    }

}
