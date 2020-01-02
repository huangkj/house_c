package com.easylife.house.customer.ui.housetypelist;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HouseCollectTypeAdapter;
import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 文件已作废，放弃使用-2019.06.13
 */
public class HouseTypeListFragment extends MVPBaseFragment<HouseTypeListContract.View, HouseTypeListPresenter> implements HouseTypeListContract.View {
    public static HouseTypeListFragment newInstance() {
        HouseTypeListFragment fragment = new HouseTypeListFragment();
        return fragment;
    }

    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }

    private HouseCollectTypeAdapter adapter;

    @Override
    public void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseCollectTypeAdapter(R.layout.item_house_type, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        adapter.setEmptyView(emptyView);
        mPresenter.getNetData();
    }

    @Override
    public void initData(List<HouseColletion> apps) {
        adapter.setNewData(apps);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }
}
