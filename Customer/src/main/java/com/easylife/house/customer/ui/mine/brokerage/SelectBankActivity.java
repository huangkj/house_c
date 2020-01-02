package com.easylife.house.customer.ui.mine.brokerage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.SelectBankAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.MyBankCardActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.REFRESH_BANK_CARD;

/**
 * 选择银行卡 hkj
 */
public class SelectBankActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.rcvSelectBank)
    RecyclerView rcvSelectBank;
    private SelectBankAdapter selectBankAdapter;
    /**
     * 上次选中的银行卡id
     */
    private String preSelectBankNumber;
    private boolean isRefund;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_select_bankl, null);

    }

    public static void startActivity(Activity activity, String preSelectBankNumber, boolean isRefund, int requestCode) {
        Intent intent = new Intent(activity, SelectBankActivity.class);
        intent.putExtra("preSelectBankNumber", preSelectBankNumber);
        intent.putExtra("isRefund", isRefund);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        preSelectBankNumber = getIntent().getStringExtra("preSelectBankNumber");
        isRefund = getIntent().getBooleanExtra("isRefund", false);

        View footView = View.inflate(this, R.layout.my_bank_card_footview, null);
        footView.findViewById(R.id.TvAddBankCardFoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SelectBankActivity.this, MyBankCardActivity.class), 1);
            }
        });

        selectBankAdapter = new SelectBankAdapter(activity, R.layout.select_bank_item, isRefund, null);
        selectBankAdapter.addFooterView(footView);
        rcvSelectBank.setLayoutManager(new LinearLayoutManager(this));
        rcvSelectBank.setAdapter(selectBankAdapter);

        selectBankAdapter.setOnCheckedListener(new SelectBankAdapter.OnCheckedListener() {
            @Override
            public void onChecked(BankCardListBean item) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bankCardItem", item);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        requestData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        switch (event.MsgType) {
            case REFRESH_BANK_CARD:
                requestData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestData();
    }

    private void requestData() {
        if (isRefund) {
            mDao.bankCardListRefund(0, this);
        } else {
            mDao.bankCardList(0, this);
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0:
                List<BankCardListBean> listData = new Gson().fromJson(response, new TypeToken<List<BankCardListBean>>() {
                }.getType());
                if (listData != null && listData.size() > 0) {
                    for (BankCardListBean bean : listData) {
                        if (bean.getBankCardNum().equals(preSelectBankNumber)) {
                            bean.setSelect(true);
                        }
                    }
                }
                selectBankAdapter.setNewData(listData);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
