package com.easylife.house.customer.ui.homefragment.homelookhouse.livehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.LiveHouseAdapter;
import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.pub.MainActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.LOGIN_STATE_CHANGE;

/**
 * Created by zgm on 2017/3/29.
 * 现场看房
 */

public class LiveHouseFragment extends MVPBaseFragment<LiveHouseContract.View, LiveHousePresenter> implements
        LiveHouseContract.View {


    @Bind(R.id.recycle_net)
    RecyclerView recycleNet;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rl_buy)
    RelativeLayout rlBuy;

    List<NetAppointmentBean> netList = new ArrayList<>();
    private LiveHouseAdapter mAdapter;
    private TextView tvEmptyTitle;

    public static LiveHouseFragment newInstance() {
        LiveHouseFragment myFragment = new LiveHouseFragment();
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        if (event.MsgType == LOGIN_STATE_CHANGE) {
            if (mDao.isLogin()) {
                tvEmptyTitle.setText("暂无现场看房");
                tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
                mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "1");
            } else {
                tvEmptyTitle.setText("立即登录");
                tvEmptyTitle.setTextColor(getResources().getColor(R.color.gradient_end));
                List<NetAppointmentBean> netList = new ArrayList<>();
                showNetData(netList);
                tvEmptyTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 3);
                    }
                });
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            tvEmptyTitle.setText("暂无现场看房");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
            if (mDao.isLogin())
                mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "1");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_netbook;
    }

    @Override
    public void initViews() {

        refresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.gradient_end));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mDao.isLogin()) {
                    mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "1");
                } else {
                    refresh.setRefreshing(false);
                }
            }
        });

        mAdapter = new LiveHouseAdapter(R.layout.net_appointment_item, netList);
        recycleNet.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleNet.setAdapter(mAdapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.look_house_empty, rlBuy, false);
        tvEmptyTitle = (TextView) emptyView.findViewById(R.id.tv_empty_title);
        tvEmptyTitle.setText("暂无现场看房");
        mAdapter.setEmptyView(emptyView);

        if (mDao.isLogin()) {
            tvEmptyTitle.setText("暂无现场看房");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
            mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "1");
        } else {
            tvEmptyTitle.setText("立即登录");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.gradient_end));
            List<NetAppointmentBean> netList = new ArrayList<>();
            showNetData(netList);
            tvEmptyTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 3);
                }
            });
        }

        mAdapter.setItemChildOnclickListenear(new LiveHouseAdapter.ItemChildOnclickListenear() {
            @Override
            public void onClickPhone(String phoneNum) {
//                CustomerUtils.showTip(getActivity(), phoneNum);
                ((MainActivity) getActivity()).call(phoneNum);
            }

            @Override
            public void onToVisit() {
//                CustomerUtils.showTip(getActivity(), "到访");
            }
        });

    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showNetData(List<NetAppointmentBean> netList) {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
        mAdapter.setNewData(netList);
    }

    @Override
    public void showFail() {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
    }
}
