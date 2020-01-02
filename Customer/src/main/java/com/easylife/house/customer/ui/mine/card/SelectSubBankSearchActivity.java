package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.BankSubListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanBankSub;
import com.easylife.house.customer.view.EditTextClearAble;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SelectSubBankSearchActivity extends BaseActivity {

    @Bind(R.id.edSearch)
    EditTextClearAble edSearch;
    @Bind(R.id.rcvSubBank)
    RecyclerView rcvSubBank;

    public static void startActivity(Activity activity, List<BeanBankSub> subList, int requestCode) {
        Intent it = new Intent(activity, SelectSubBankSearchActivity.class);
        it.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) subList);
        activity.startActivityForResult(it, requestCode);
    }


    @Override
    protected View setContentLayoutView() {
        return View.inflate(activity, R.layout.activity_select_sub_bank_search, null);
    }

    private List<BeanBankSub> subList;
    private BankSubListAdapter adapter;

    @Override
    protected void initView() {
        subList = getIntent().getParcelableArrayListExtra("list");

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

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String tag = edSearch.getText().toString();
                    if (!TextUtils.isEmpty(tag)) {
                        List<BeanBankSub> listSearch = new ArrayList<>();
                        for (BeanBankSub sub : subList) {
                            if (sub.bankBranchName.contains(tag)) {
                                listSearch.add(sub);
                            }
                        }
                        adapter.setNewData(listSearch);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
    }

}
