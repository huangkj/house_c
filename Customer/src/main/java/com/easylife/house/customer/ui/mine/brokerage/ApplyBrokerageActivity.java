package com.easylife.house.customer.ui.mine.brokerage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ApplyBrokerageAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ApplyBrokerRequest;
import com.easylife.house.customer.bean.ApplyBrokerSucc;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.BrokerDetailBean;
import com.easylife.house.customer.bean.IdCardResultBean;
import com.easylife.house.customer.bean.SelectBrokerOrderBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.InputTraderPasswordActivity;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.dao.ServerDao.BROKER_INPUT_TRADE_PASSWORD;

public class ApplyBrokerageActivity extends BaseActivity implements View.OnClickListener, RequestManagerImpl {


    @Bind(R.id.rcvApplyBrokerage)
    RecyclerView rcvApplyBrokerage;
    private ApplyBrokerageAdapter applyBrokerageAdapter;
    private TextView tvBankName;
    private TextView tvBankCardDesc;
    private TextView tvUserName;
    private TextView tvIdCardNumber;

    /**
     * 选择的银行卡
     */
    private BankCardListBean bankCardItem;
    private LinearLayout llBankCardContent;
    private TextView tvOrderPrice;
    private TextView btnConfirm;
    private IdCardResultBean idCardResultBean;
    private ApplyBrokerRequest applyBrokerRequest;
    /**
     * 选择的订单集合
     */
    private ArrayList<SelectBrokerOrderBean> selectedList;
    private BrokerDetailBean brokerDetailBean;
    private double totalAmout;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_apply_brokerage, null);
    }

    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ApplyBrokerageActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    public static void startActivity(Activity activity, BrokerDetailBean brokerDetailBean, int requestCode) {
        Intent intent = new Intent(activity, ApplyBrokerageActivity.class);
        intent.putExtra("brokerDetailBean", brokerDetailBean);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        brokerDetailBean = (BrokerDetailBean) getIntent().getSerializableExtra("brokerDetailBean");


        applyBrokerageAdapter = new ApplyBrokerageAdapter(R.layout.apply_brokerage_item, null);
        View headView = View.inflate(this, R.layout.apply_brokerage_headview, null);
        View footView = View.inflate(this, R.layout.apply_brokerage_footview, null);
        applyBrokerageAdapter.addHeaderView(headView);
        applyBrokerageAdapter.addFooterView(footView);


        headView.findViewById(R.id.relSelectOrder).setOnClickListener(this);
        tvUserName = (TextView) headView.findViewById(R.id.tvUserName);
        tvIdCardNumber = (TextView) headView.findViewById(R.id.tvIdCardNumber);
        tvBankName = (TextView) headView.findViewById(R.id.tvBankName);
        tvBankCardDesc = (TextView) headView.findViewById(R.id.tvBankCardDesc);
        llBankCardContent = (LinearLayout) headView.findViewById(R.id.llBankCardContent);
        headView.findViewById(R.id.relSelectBankCard).setOnClickListener(this);


        tvOrderPrice = (TextView) footView.findViewById(R.id.tvOrder);
        btnConfirm = (TextView) footView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);


        rcvApplyBrokerage.setLayoutManager(new LinearLayoutManager(this));
        rcvApplyBrokerage.setAdapter(applyBrokerageAdapter);


        mDao.getAuthInfo(0, this);
        mDao.bankCardList(2, this);

        applyBrokerRequest = new ApplyBrokerRequest();

        if (brokerDetailBean != null) {
            selectedList = new ArrayList<>();
            List<BrokerDetailBean.BrokerOdersBean> brokerOders = brokerDetailBean.getBrokerOders();

            for (BrokerDetailBean.BrokerOdersBean bean : brokerOders
            ) {
                selectedList.add(BrokerOdersBean2SelectOrderBean(bean));
            }
            applyBrokerageAdapter.setNewData(selectedList);
            ArrayList<String> selectId = new ArrayList<>();
            for (SelectBrokerOrderBean bean :
                    selectedList) {
                selectId.add(bean.getId() + "");
            }
//            applyBrokerRequest.setBrokerOders(selectId);
            applyBrokerRequest.setBrokerOders(selectedList);
            applyBrokerRequest.setCode(brokerDetailBean.getPusherApply().getCode() + "");
            applyBrokerRequest.setApplyBrokerage(brokerDetailBean.getPusherApply().getApplyBrokerage());
            tvOrderPrice.setText("结佣金额： ￥" + MoneyUtils.moneyFormat3(brokerDetailBean.getPusherApply().getApplyBrokerage()));
        }


    }


    @Override
    protected void setActionBarDetail() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relSelectOrder://选择订单
                SelectBrokerageOrderActivity.startActivity(this, selectedList, 1);
                break;
            case R.id.relSelectBankCard://选择银行卡
                if (bankCardItem != null) {
                    SelectBankActivity.startActivity(this, bankCardItem.getBankCardNum(), false, 0);
                } else {
                    SelectBankActivity.startActivity(this, "", false, 0);

                }
                break;
            case R.id.btnConfirm://提交
                if (idCardResultBean == null) {
                    com.blankj.utilcode.util.ToastUtils.showShort("身份信息获取失败");
                    return;
                }

                if (bankCardItem == null) {
                    com.blankj.utilcode.util.ToastUtils.showShort("请选择银行卡");
                    return;
                }


                if (selectedList == null || selectedList.size() == 0) {
                    com.blankj.utilcode.util.ToastUtils.showShort("请选择订单");
                    return;
                }


                if (totalAmout < 0) {
                    ToastUtils.showShort("申请结佣金额需大于0元");
                    return;
                }

                InputTraderPasswordActivity.startActivity(this, BROKER_INPUT_TRADE_PASSWORD);

                break;
        }
    }

    private void submit() {
        mDao.subminBrokeAmount(1, applyBrokerRequest, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {//选择银行卡
                llBankCardContent.setVisibility(View.VISIBLE);
                bankCardItem = (BankCardListBean) data.getSerializableExtra("bankCardItem");
                tvBankName.setText(bankCardItem.getBelongToBank());
                String bankCardNum = bankCardItem.getBankCardNum();
                String last4Num = bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length());
                tvBankCardDesc.setText("尾号" + last4Num + "储蓄卡");

                applyBrokerRequest.setBelongToBank(bankCardItem.getBelongToBank());
                applyBrokerRequest.setOpenBranchBank(bankCardItem.getOpenBranchBank());
                applyBrokerRequest.setBankCardNum(bankCardItem.getBankCardNum());
                applyBrokerRequest.setBankBranchNum(bankCardItem.getLinkNumber());
            } else if (requestCode == 1) {
                totalAmout = data.getDoubleExtra("totalAmout", 0);
                selectedList = (ArrayList<SelectBrokerOrderBean>) data.getSerializableExtra("selectedList");
                applyBrokerageAdapter.setNewData(selectedList);
                tvOrderPrice.setText("结佣金额： ￥" + MoneyUtils.moneyFormat3(totalAmout + ""));
                applyBrokerRequest.setApplyBrokerage(totalAmout + "");
                ArrayList<String> selectId = new ArrayList<>();
                for (SelectBrokerOrderBean bean :
                        selectedList) {
                    selectId.add(bean.getId() + "");
                }
//                applyBrokerRequest.setBrokerOders(selectId);
                applyBrokerRequest.setBrokerOders(selectedList);
            }

        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0://获取身份证信息
                idCardResultBean = GsonUtils.fromJson(response, IdCardResultBean.class);
                if (idCardResultBean != null) {
                    tvUserName.setText(idCardResultBean.getRealName());
                    tvIdCardNumber.setText(idCardResultBean.getIdentityCardNum());
                    applyBrokerRequest.setUserId(mDao.getCustomer().id);
                    applyBrokerRequest.setApplyUserCardNum(idCardResultBean.getIdentityCardNum());
                    applyBrokerRequest.setApplyUserName(idCardResultBean.getRealName());
                    applyBrokerRequest.setCardPhotoFront(idCardResultBean.getIdentityCardFront());
                    applyBrokerRequest.setCardPhotoReverse(idCardResultBean.getIdentityCardReverse());
                }
                break;

            case 1:
                ApplyBrokerSucc applyBrokerSucc = GsonUtils.fromJson(response, ApplyBrokerSucc.class);
                if (null != applyBrokerSucc) {
                    if ("205".equals(applyBrokerSucc.getCode())) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ApplyBrokerageActivity.this).setMessage(applyBrokerSucc.getMsg()).
                                setPositiveButton("是", null).setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                CompanyBrokerageActivity.startActivity(ApplyBrokerageActivity.this, 0);
//                        EventBus.getDefault().post(new MessageEvent(BROKER_FINISH_PAGE));
                                finish();
                            }
                        }).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    } else {

                        com.blankj.utilcode.util.ToastUtils.showShort("提交成功");
                    }
                } else {
                    com.blankj.utilcode.util.ToastUtils.showShort("提交成功");
                    finish();
                }
                break;

            case 2://获取银行卡
                List<BankCardListBean> listData = new Gson().fromJson(response, new TypeToken<List<BankCardListBean>>() {
                }.getType());
                if (listData != null && listData.size() > 0) {
                    if (brokerDetailBean == null) {
                        if (listData.get(0).isComplete()) {
                            bankCardItem = listData.get(0);
                        }
                    } else {//重新申请
                        for (BankCardListBean data : listData
                        ) {
                            if (brokerDetailBean.getPusherApply().getBankCardNum().equals(data.getBankCardNum()) && data.isComplete()) {
                                bankCardItem = data;
                            } else {
                                if (listData.get(0).isComplete()) {
                                    bankCardItem = listData.get(0);
                                }
                            }

                        }

                    }
                    if (bankCardItem != null) {
                        tvBankName.setText(bankCardItem.getBelongToBank());
                        String bankCardNum = bankCardItem.getBankCardNum();
                        String last4Num = bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length());
                        tvBankCardDesc.setText("尾号" + last4Num + "储蓄卡");
                        applyBrokerRequest.setBelongToBank(bankCardItem.getBelongToBank());
                        applyBrokerRequest.setOpenBranchBank(bankCardItem.getOpenBranchBank());
                        applyBrokerRequest.setBankCardNum(bankCardItem.getBankCardNum());
                        applyBrokerRequest.setBankBranchNum(bankCardItem.getLinkNumber());
                    }

                }
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        com.blankj.utilcode.util.ToastUtils.showShort(code.msg);
        if (requestType == 1) {
        /*    if (code.code.equals("205")) {
                AlertDialog alertDialog = new AlertDialog.Builder(ApplyBrokerageActivity.this).setMessage(code.msg).
                        setPositiveButton("是", null).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                                CompanyBrokerageActivity.startActivity(ApplyBrokerageActivity.this, 0);
//                        EventBus.getDefault().post(new MessageEvent(BROKER_FINISH_PAGE));
                        finish();
                    }
                }).create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }*/
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == MessageEvent.CHECK_TRADE_PASSWORD) {//验证交易密码成功
            submit();
        }
    }

    private SelectBrokerOrderBean BrokerOdersBean2SelectOrderBean(BrokerDetailBean.BrokerOdersBean bean) {
        SelectBrokerOrderBean selectBrokerOrderBean = new SelectBrokerOrderBean();

        selectBrokerOrderBean.brokerageAmount = bean.brokerageAmount;
        selectBrokerOrderBean.frontAfterNodeAmount = bean.frontAfterNodeAmount;
        selectBrokerOrderBean.orderId = bean.orderId;
        selectBrokerOrderBean.customerFollowId = bean.customerFollowId;
        selectBrokerOrderBean.orderBy = bean.orderBy;
        selectBrokerOrderBean.shouldAmount = bean.shouldAmount + "";
        selectBrokerOrderBean.cityId = bean.cityId;
        selectBrokerOrderBean.customerPhone = bean.customerPhone;
        selectBrokerOrderBean.frontAmount = bean.frontAmount;
        selectBrokerOrderBean.creatTime = bean.creatTime;
        selectBrokerOrderBean.customerId = bean.customerId;
        selectBrokerOrderBean.id = bean.id;
        selectBrokerOrderBean.examineState = bean.examineState;
        selectBrokerOrderBean.ruleId = bean.ruleId;
        selectBrokerOrderBean.devAmount = bean.devAmount;
        selectBrokerOrderBean.devId = bean.devId;
        selectBrokerOrderBean.brokerId = bean.brokerId;
        selectBrokerOrderBean.devIdList = bean.devIdList;
        selectBrokerOrderBean.dealAmount = bean.dealAmount;
        selectBrokerOrderBean.packageId = bean.packageId;
        selectBrokerOrderBean.confirmState = TextUtils.isEmpty(bean.confirmState) ? 0 : Integer.parseInt(bean.confirmState);
        selectBrokerOrderBean.shouldReturnAmount = bean.shouldReturnAmount;
        selectBrokerOrderBean.brokerOders = bean.brokerOders;
        selectBrokerOrderBean.devName = bean.devName;
        selectBrokerOrderBean.returnMark = bean.returnMark;
        selectBrokerOrderBean.customerName = bean.customerName;
        selectBrokerOrderBean.frontNodeAmount = bean.frontNodeAmount;
        selectBrokerOrderBean.brokerageNode = bean.brokerageNode;
        selectBrokerOrderBean.companyId = bean.companyId;
        selectBrokerOrderBean.afterNodeAmount = bean.afterNodeAmount;
        selectBrokerOrderBean.estimateAmount = bean.estimateAmount;
        selectBrokerOrderBean.creatTimeStr = bean.creatTimeStr;
        selectBrokerOrderBean.surplusAmount = bean.surplusAmount;
        selectBrokerOrderBean.brokerName = bean.brokerName;
        selectBrokerOrderBean.afterAmount = bean.afterAmount;
        selectBrokerOrderBean.frontSurplusAmount = bean.frontSurplusAmount;
        selectBrokerOrderBean.afterSurplusAmount = bean.afterSurplusAmount;
        selectBrokerOrderBean.houseNum = bean.houseNum;
        selectBrokerOrderBean.actualReturnAmount = bean.actualReturnAmount;


        return selectBrokerOrderBean;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
