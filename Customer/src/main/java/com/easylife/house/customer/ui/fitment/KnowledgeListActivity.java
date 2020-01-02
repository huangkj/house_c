package com.easylife.house.customer.ui.fitment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.KnowledgeListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanDesignCase;

import butterknife.Bind;

/**
 * 设计案例列表
 */
public class KnowledgeListActivity extends BaseActivity {

    @Bind(R.id.recycler)
    RecyclerView recycler;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, KnowledgeListActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_list_simple, null);
    }

    private KnowledgeListAdapter adapter;

    @Override
    protected void initView() {
        adapter = new KnowledgeListAdapter();

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
