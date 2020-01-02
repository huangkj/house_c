package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.BankSearchListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanBank;
import com.easylife.house.customer.bean.py.PinyinComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class BankSearchListActivity extends BaseActivity {

    @Bind(R.id.edSearch)
    EditText edSearch;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void startActivity(Activity activity, List<BeanBank> data, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) data);
        activity.startActivityForResult(new Intent(activity, BankSearchListActivity.class)
                .putExtras(bundle), requestCode);
    }


    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_bank_search_list, null);
    }

    private BankSearchListAdapter adapter;
    private List<BeanBank> data;
    private View empty;

    @Override
    protected void initView() {
        data = new ArrayList<>();
        data.addAll((ArrayList<BeanBank>) getIntent().getExtras().getSerializable("data"));

        adapter = new BankSearchListAdapter();
        empty = getLayoutInflater().inflate(R.layout.empty_pub, null);
        TextView textView = empty.findViewById(R.id.tvEmpty);
        textView.setText("该银行暂未支持绑定，请更换其他卡片试试");
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BeanBank data = (BeanBank) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("name", data.bankName);
                intent.putExtra("id", data.bankCode);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filtrateData();
                }
                return false;
            }
        });
    }

    private void filtrateData() {
        adapter.setEmptyView(empty);
        List<BeanBank> result = new ArrayList<>();
        String tag = edSearch.getText().toString();
        for (BeanBank bank : data) {
            if (bank.bankName.contains(tag) || bank.pinyin.contains(tag) || (!TextUtils.isEmpty(bank.bankCode) && bank.bankCode.toUpperCase().contains(tag.toUpperCase()))) {
                result.add(bank);
            }
        }
        Collections.sort(result, new PinyinComparator());
        adapter.setNewData(result);
    }

    @Override
    protected void setActionBarDetail() {

    }

}
