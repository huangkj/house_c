package com.easylife.house.customer.ui.mine.card;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MyBankCardAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.mine.accountsafe.SetTradePswVerificationCodeActivity;
import com.easylife.house.customer.ui.mine.accountsafe.SetTraderPasswordActivity;
import com.easylife.house.customer.view.SpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.REFRESH_BANK_CARD;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_BIND_BANK_CARD;

public class MyBankCardActivity extends BaseActivity {

    @Bind(R.id.rcvBankCard)
    RecyclerView rcvBankCard;
    @Bind(R.id.rl_empty)
    LinearLayout rlEmpty;
    private MyBankCardAdapter myBankCardAdapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_my_bank_card, null);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        myBankCardAdapter = new MyBankCardAdapter(R.layout.my_bank_card_item, null);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.my_back_card_empty_view, rlEmpty, false);
        emptyView.findViewById(R.id.btnAddBankCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBankCard();
            }
        });
        myBankCardAdapter.setEmptyView(emptyView);
        View footView = View.inflate(this, R.layout.my_bank_card_footview, null);
        footView.findViewById(R.id.TvAddBankCardFoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBankCard();
            }
        });
        myBankCardAdapter.addFooterView(footView);
        myBankCardAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BankCardListBean bankCardListBean = myBankCardAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.llMyBankCard:
                        BankCardDetailActivity.startActivity(MyBankCardActivity.this, bankCardListBean.getId() + "", 5);
                        break;
                }
            }
        });
        rcvBankCard.setLayoutManager(new LinearLayoutManager(this));
        rcvBankCard.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(20)));
        rcvBankCard.setAdapter(myBankCardAdapter);
        requestData();
    }

    private void requestData() {
        showLoading();
        mDao.bankCardList(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                List<BankCardListBean> listData = new Gson().fromJson(response, new TypeToken<List<BankCardListBean>>() {
                }.getType());
                myBankCardAdapter.setNewData(listData);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
                ToastUtils.showShort(code.msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        switch (event.MsgType) {
            case REFRESH_BANK_CARD:
                requestData();
                break;
        }
    }


    private void addBankCard() {
        Customer customer = mDao.getCustomer();
//        if (!TextUtils.isEmpty(customer.transactionPassword)) {//已经设置了交易密码
//            InputTraderPasswordActivity.startActivity(MyBankCardActivity.this, ServerDao.ADD_BANK_CARD_INPUT_TRADE_PASSWORD);
//        } else {
//            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
//            builder.title("提示").content("请先设置交易密码").positiveText("去设置").negativeText("关闭").onPositive(new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                    startActivityForResult(new Intent(MyBankCardActivity.this, AccountSafeActivity.class), 1);
//                }
//            }).show();
//
//        }
//


        if (TextUtils.isEmpty(customer.phone)) {//是否绑定手机号
            new MaterialDialog.Builder(MyBankCardActivity.this).content("请先进行实名认证").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    BindMobileActivity.startActivity(MyBankCardActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
                }
            }).show();
        } else {
            //已经绑定了手机号，判断是否身份认证了
            if (TextUtils.isEmpty(customer.identityCardNum)) {
                new MaterialDialog.Builder(MyBankCardActivity.this).content("请先进行实名认证").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        IDCardIdentificationActivity.startActivity(MyBankCardActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
                    }
                }).show();
            } else {
                if (TextUtils.isEmpty(customer.transactionPassword)) {
                    // 已实名认证  未设置交易密码
                    new MaterialDialog.Builder(MyBankCardActivity.this).content("请设置交易密码").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SetTraderPasswordActivity.startActivity(MyBankCardActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
//                            SetTradePswVerificationCodeActivity.startActivity(MyBankCardActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
                        }
                    }).show();
                } else {
                    //已经设置交易密码，进入输入交易密码页面
                    InputTraderPasswordActivity.startActivity(MyBankCardActivity.this, ServerDao.ADD_BANK_CARD_INPUT_TRADE_PASSWORD);
                }
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        requestData();
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
