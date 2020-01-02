package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemClickListener;
import com.easylife.house.customer.adapter.PYListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanBank;
import com.easylife.house.customer.bean.PYBean;
import com.easylife.house.customer.bean.py.PinyinComparator;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.view.SideBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class BankListActivity extends BaseActivity {
    @Bind(R.id.mPyList)
    ListView mPYListView;
    @Bind(R.id.dialog)
    TextView dialog;
    @Bind(R.id.sidebar)
    SideBar sideBar;
    @Bind(R.id.edSearch)
    TextView edSearch;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, BankListActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_bank_list, null);
    }

    private PYListAdapter adapter;
    private List<BeanBank> data;

    @Override
    protected void initView() {
        // 建立关联
        adapter = new PYListAdapter(activity, new ItemClickListener<PYBean>() {
            @Override
            public void itemClick(int viewId, int actionType, PYBean data) {
                Intent intent = new Intent();
                intent.putExtra("name", data.getBeanName());
                intent.putExtra("id", data.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        adapter.setData(data);
        mPYListView.setAdapter(adapter);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mPYListView.setSelection(position);
                }
            }
        });
        mPYListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.getList().get(position);
            }
        });

        getNetData();
    }


    private void getNetData() {
        mDao.getBankList(1, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                data = new Gson().fromJson(response, new TypeToken<List<BeanBank>>() {
                }.getType());
                Collections.sort(data, new PinyinComparator());
                adapter.setData(data);
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
        // 跳转搜索页面
        BankSearchListActivity.startActivity(activity, data, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
