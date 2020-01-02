package com.easylife.house.customer.ui.fitment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.DesignCaseListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanDesignCase;

import butterknife.Bind;

/**
 * 设计案例列表
 */
public class DesignCaseListActivity extends BaseActivity {

    @Bind(R.id.recycler)
    RecyclerView recycler;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, DesignCaseListActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_list_simple, null);
    }

    private DesignCaseListAdapter adapter;

    @Override
    protected void initView() {
        adapter = new DesignCaseListAdapter();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.addData(new BeanDesignCase());
        adapter.addData(new BeanDesignCase());
        adapter.addData(new BeanDesignCase());
    }

    @Override
    protected void setActionBarDetail() {

    }

}
