package com.easylife.house.customer.ui.mine.brokerage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.BrokerDetailAdapter;
import com.easylife.house.customer.adapter.OperaterRecordAdapter2;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.BrokerDetailBean;
import com.easylife.house.customer.bean.OperaterRecordBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 结佣详情 hkj
 */
public class BrokerageDetailActivity extends BaseActivity {

    @Bind(R.id.rcvBrokerDetail)
    RecyclerView rcvBrokerDetail;
    private BrokerDetailAdapter brokerDetailAdapter;
    /**
     * 提交记录id
     */
    private String id;
    private TextView tvName;
    private TextView tvIdCardNumber;
    private TextView tvBankCardNumber;
    private TextView tvCreateBank;
    private TextView tvShouldPayDate;
    private TextView tvBrokerPrice;
    private RecyclerView rcvRecord;
    private RelativeLayout relSubmit;
    private TextView btnSubmit;
    /**
     * 审核状态：1:待审核,2:审核中,3:审核通过,4:审核拒绝  5财务审核拒绝"
     */
    private int status;
    private OperaterRecordAdapter2 operaterRecordAdapter;
    private LinearLayout llShouldPayDate;
    private Button btnSubmit1;
    private BrokerDetailBean brokerDetailBean;
    private View v1;
    private TextView tvTax;
    private TextView tvStatus;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_brokerage_detail, null);
    }

    public static void startActivity(Activity activity, String id, int status, int requestCode) {
        Intent intent = new Intent(activity, BrokerageDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("status", status);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        status = getIntent().getIntExtra("status", 0);


        View headView = View.inflate(this, R.layout.broker_detail_headview, null);
        View footView = View.inflate(this, R.layout.broker_detail_footview, null);

        rcvRecord = (RecyclerView) footView.findViewById(R.id.rcvRecord);
        relSubmit = (RelativeLayout) footView.findViewById(R.id.relSubmit);
        btnSubmit = (TextView) footView.findViewById(R.id.btnSubmit);
        tvBrokerPrice = (TextView) footView.findViewById(R.id.tvBrokerPrice);
        tvTax = (TextView) footView.findViewById(R.id.tvTax);
        btnSubmit1 = (Button) footView.findViewById(R.id.btnSubmit);

        btnSubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新申请
                getBankList();
            }
        });
        operaterRecordAdapter = new OperaterRecordAdapter2(null);

        operaterRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                new AlertDialog.Builder(BrokerageDetailActivity.this).setMessage(operaterRecordAdapter.getItem(position).getRemark()).
//                        setPositiveButton("知道了", null).create().show();
                View dialogView = View.inflate(activity, R.layout.operater_dialog_layout, null);
                final Dialog alertDialog = new Dialog(activity, R.style.dialogStyle);
                alertDialog.setContentView(dialogView);
                alertDialog.show();
                ((TextView) dialogView.findViewById(R.id.tvContent)).setText(operaterRecordAdapter.getItem(position).getRemark());
                dialogView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });

        rcvRecord.setLayoutManager(new LinearLayoutManager(this));
        rcvRecord.setAdapter(operaterRecordAdapter);


        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvIdCardNumber = (TextView) headView.findViewById(R.id.tvIdCardNumber);
        tvBankCardNumber = (TextView) headView.findViewById(R.id.tvBankCardNumber);
        tvCreateBank = (TextView) headView.findViewById(R.id.tvCreateBank);
        tvShouldPayDate = (TextView) headView.findViewById(R.id.tvShouldPayDate);
        tvStatus = (TextView) headView.findViewById(R.id.tvStatus);
        llShouldPayDate = (LinearLayout) headView.findViewById(R.id.llShouldPayDate);
        v1 = headView.findViewById(R.id.v1);


        brokerDetailAdapter = new BrokerDetailAdapter(R.layout.broker_detail_item, null);
        brokerDetailAdapter.addHeaderView(headView);
        brokerDetailAdapter.addFooterView(footView);


        rcvBrokerDetail.setLayoutManager(new LinearLayoutManager(this));
        rcvBrokerDetail.setAdapter(brokerDetailAdapter);

        llShouldPayDate.setVisibility(status == 3 ? View.VISIBLE : View.GONE);
        v1.setVisibility(status == 3 ? View.VISIBLE : View.GONE);

        tvName.setFocusableInTouchMode(true);
        tvName.requestFocus();


        requestData();


    }

    private void getBankList() {
        mDao.bankCardList(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                List<BankCardListBean> listData = new Gson().fromJson(response, new TypeToken<List<BankCardListBean>>() {
                }.getType());
                if (listData != null && listData.size() > 0) {
//                    startActivity(new Intent(MyBrokerageActivity.this, ApplyBrokerageActivity.class));
                    ApplyBrokerageActivity.startActivity(BrokerageDetailActivity.this, brokerDetailBean, 0);
                } else {
                    ToastUtils.showShort("请先绑定银行卡");
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort("请先绑定银行卡");
            }
        });
    }

    private void requestData() {
        mDao.brokeAmountInfo(0, id, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                brokerDetailBean = GsonUtils.fromJson(response, BrokerDetailBean.class);
                BrokerDetailBean.PusherApplyBean pusherApply = brokerDetailBean.getPusherApply();
                tvName.setText(pusherApply.getApplyUserName());
                tvIdCardNumber.setText(pusherApply.getApplyUserCardNum());

                String bankCardNum = pusherApply.getBankCardNum();
                tvStatus.setText(pusherApply.getStateStr());


                if ("资料确认中".equals(pusherApply.getStateStr()) || "交易处理中".equals(pusherApply.getStateStr())
                        || "支付确认中".equals(pusherApply.getStateStr()) || "银行打款中".equals(pusherApply.getStateStr())) {
                    tvStatus.setTextColor(Color.parseColor("#FDA90B"));

                } else if ("待重新提交".equals(pusherApply.getStateStr())) {
                    tvStatus.setTextColor(Color.parseColor("#FDA90B"));

                } else if ("交易完成".equals(pusherApply.getStateStr())) {
                    tvStatus.setTextColor(Color.parseColor("#1CAC9E"));

                } else if ("申请结佣".equals(pusherApply.getStateStr())) {
                    tvStatus.setTextColor(Color.parseColor("#FD6A41"));

                } else if ("审核拒绝".equals(pusherApply.getStateStr())) {
                    tvStatus.setTextColor(Color.parseColor("#FE505C"));

                } else {
                    tvStatus.setTextColor(Color.parseColor("#c5c5ca"));
                }

                StringBuilder str = new StringBuilder(bankCardNum.replace(" ", ""));

                int i = str.length() / 4;
                int j = str.length() % 4;

                for (int x = (j == 0 ? i - 1 : i); x > 0; x--) {
                    str = str.insert(x * 4, " ");
                }

                tvBankCardNumber.setText(str);

                tvCreateBank.setText(pusherApply.getOpenBranchBank());

                if (!TextUtils.isEmpty(pusherApply.getAuditStatus())) {
                    status = Integer.parseInt(brokerDetailBean.getPusherApply().getAuditStatus());
                }
                relSubmit.setVisibility(status == 4 || status == 5 ? View.VISIBLE : View.GONE);


                String expectPayTime = pusherApply.getExpectPayTime();
                if (!TextUtils.isEmpty(expectPayTime)) {
                    tvShouldPayDate.setText(TimeUtils.millis2String(Long.parseLong(expectPayTime), new SimpleDateFormat("yyyy-MM-dd")));
                }


                if (status == 3) {//已通过
                    tvBrokerPrice.setText(Html.fromHtml("<font color='#575757'>实际结佣金额： </font>" + "¥" + MoneyUtils.moneyFormat3(pusherApply.getPayBrokerage())));
                    tvTax.setVisibility(View.VISIBLE);
//（申请金额¥50000.00-税费¥10000.00）
                    tvTax.setText("(申请金额¥" + MoneyUtils.moneyFormat3(pusherApply.getApplyBrokerage()) + "-税费¥" + MoneyUtils.moneyFormat3(pusherApply.getTaxBrokerage()));
                } else {

                    tvBrokerPrice.setText(Html.fromHtml("<font color='#575757'>申请结佣金额： </font>" + "¥" + MoneyUtils.moneyFormat3(pusherApply.getApplyBrokerage())));
                    tvTax.setVisibility(View.GONE);
                }

                List<BrokerDetailBean.BrokerOdersBean> brokerOders = brokerDetailBean.getBrokerOders();

                brokerDetailAdapter.setNewData(brokerOders);

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
            }
        });


        mDao.brokeOperationRecord(0, id, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {


                List<OperaterRecordBean> listData = new Gson().fromJson(response, new TypeToken<List<OperaterRecordBean>>() {
                }.getType());

                if (listData != null) {
                    operaterRecordAdapter.setNewData(listData);
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
            }
        });
    }

    private void fakeData() {
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            strings.add("ff" + i);

        }
//        brokerDetailAdapter.setNewData(strings);
    }

    @Override
    protected void setActionBarDetail() {

    }

}
