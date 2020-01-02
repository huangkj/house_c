package com.easylife.house.customer.ui.mine.collection.housetypelist;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HouseCollectTypeAdapter;
import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.HousesTypeDetailActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HouseTypeListFragment extends MVPBaseFragment<HouseTypeListContract.View, HouseTypeListPresenter> implements HouseTypeListContract.View {


    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;
    List<HouseColletion> apps = new ArrayList<>();

    public static HouseTypeListFragment newInstance() {
        HouseTypeListFragment fragment = new HouseTypeListFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }

    private HouseCollectTypeAdapter adapter;

    @Override
    public void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseCollectTypeAdapter(R.layout.houses_type_list_item, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNetData();
            }
        });
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        adapter.setEmptyView(emptyView);
        mPresenter.getNetData();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (apps.size() == 0) {
                    return;
                }

                HouseColletion houseColletion = apps.get(position);
                HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = new HousesTypeBean.HouseLayoutDataBean();
                houseLayoutDataBean.avgprice = houseColletion.avgprice;
                houseLayoutDataBean.fArea = houseColletion.fArea;
                houseLayoutDataBean.price = houseColletion.mTotalPrice;
                houseLayoutDataBean.devName = houseColletion.devName;
                houseLayoutDataBean.houseImg = houseColletion.houseImg;
                houseLayoutDataBean.houseName = houseColletion.houseName;
                houseLayoutDataBean.introduce = houseColletion.introduce;
                houseLayoutDataBean.structure = houseColletion.structure;
                houseLayoutDataBean.tag = houseColletion.tag;
                houseLayoutDataBean.houseId = houseColletion.houseId;
                HousesDetailBaseBean baseBean = new HousesDetailBaseBean();
                baseBean.estateProjectId = houseColletion.projectID;
                baseBean.devId = houseColletion.devId;
                HousesTypeDetailActivity.startActivity(getActivity(), houseLayoutDataBean, baseBean, true, 1);
//                startActivityForResult(new Intent(getActivity(), HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                        .putExtra("BASE_BEAN", baseBean),1);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            swipeLayout.setRefreshing(true);
            mPresenter.getNetData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getNetData();
    }

    @Override
    public void initData(List<HouseColletion> apps) {
        LogOut.d("收藏户型：", apps);
        this.apps = apps;
        swipeLayout.setRefreshing(false);
        adapter.setNewData(apps);
    }

    @Override
    public void showFail() {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }
}
