package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.BankSubListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanBankSub;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectSubBankActivity extends BaseActivity {

    @Bind(R.id.rcvSubBank)
    RecyclerView rcvSubBank;

    public static void startActivity(Activity activity, String bankId, String provinceId, String cityId, int requestCode) {
        Intent it = new Intent(activity, SelectSubBankActivity.class);

        it.putExtra("bankId", bankId);
        it.putExtra("provinceId", provinceId);
        it.putExtra("cityId", cityId);
        activity.startActivityForResult(it, requestCode);
    }


    @Override
    protected View setContentLayoutView() {
        return View.inflate(activity, R.layout.activity_select_sub_bank, null);
    }

    private String bankId;
    private String provinceId;
    private String cityId;
    private List<BeanBankSub> list;
    private BankSubListAdapter adapter;

    @Override
    protected void initView() {
        bankId = getIntent().getStringExtra("bankId");
        provinceId = getIntent().getStringExtra("provinceId");
        cityId = getIntent().getStringExtra("cityId");

        adapter = new BankSubListAdapter(null);
        rcvSubBank.setLayoutManager(new LinearLayoutManager(this));
        rcvSubBank.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BeanBankSub sub = (BeanBankSub) adapter.getItem(position);
                Intent it = new Intent();
                it.putExtra("id", sub.linkNumber);
                it.putExtra("name", sub.bankBranchName);
                setResult(RESULT_OK, it);
                finish();
            }
        });

        mDao.getSubBank(0, bankId, provinceId, cityId, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                list = new Gson().fromJson(response, new TypeToken<List<BeanBankSub>>() {
                }.getType());
                adapter.setNewData(list);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
            }
        });
    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick(R.id.edSearch)
    public void onViewClicked() {
        SelectSubBankSearchActivity.startActivity(activity, list, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }
}
