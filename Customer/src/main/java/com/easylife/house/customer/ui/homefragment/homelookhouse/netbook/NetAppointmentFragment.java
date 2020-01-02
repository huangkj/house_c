package com.easylife.house.customer.ui.homefragment.homelookhouse.netbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.NetAppointmentAdapter;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.bean.LookQrBean;
import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.exportshop.shop.ExportShopActivity;
import com.easylife.house.customer.ui.pub.MainActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DimenUtils;
import com.easylife.house.customer.util.QRCodeUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.LOGIN_STATE_CHANGE;
import static com.easylife.house.customer.event.MessageEvent.LOOK_HOUSE_CHANGE;

/**
 * Created by zgm on 2017/3/29.
 * 网上看房
 */

public class NetAppointmentFragment extends MVPBaseFragment<NetAppointmentContract.View, NetAppointmentPresenter> implements
        NetAppointmentContract.View {


    @Bind(R.id.recycle_net)
    RecyclerView recycleNet;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rl_buy)
    RelativeLayout rlBuy;

    List<NetAppointmentBean> netList = new ArrayList<>();
    private NetAppointmentAdapter mAdapter;
    private TextView tvEmptyTitle;

    public static NetAppointmentFragment newInstance() {
        NetAppointmentFragment myFragment = new NetAppointmentFragment();
        return myFragment;
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
                    mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
                } else {
                    refresh.setRefreshing(false);
                }
            }
        });

        mAdapter = new NetAppointmentAdapter(R.layout.net_appointment_item, netList);
        recycleNet.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleNet.setAdapter(mAdapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.look_house_empty, rlBuy, false);
        tvEmptyTitle = (TextView) emptyView.findViewById(R.id.tv_empty_title);
        tvEmptyTitle.setText("暂无网上预约");
        mAdapter.setEmptyView(emptyView);

        if (mDao.isLogin()) {
            tvEmptyTitle.setText("暂无网上预约");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
            mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
        } else {
            tvEmptyTitle.setText("立即登录");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.gradient_end));
            List<NetAppointmentBean> netList = new ArrayList<>();
            showNetData(netList);
            tvEmptyTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 2);
                }
            });
        }

        mAdapter.setItemChildOnclickListenear(new NetAppointmentAdapter.ItemChildOnclickListenear() {
            @Override
            public void onClickPhone(String phoneNum) {
//                call(phoneNum);
                ((MainActivity) getActivity()).call(phoneNum);
            }

            @Override
            public void onCancleClick(final String devId) {

                new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage("是否取消预约? ")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginResult loginCache = mDao.getLoginCache();
                                if (loginCache != null) {
                                    mPresenter.delLookHouse(loginCache.userCode, loginCache.token, devId);
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }

            @Override
            public void onShopClick(String brokeCode) {
                startActivity(new Intent(getActivity(), ExportShopActivity.class).putExtra("BROKECODE", brokeCode));
            }

            @Override
            public void showQRdialog(NetAppointmentBean netAppointmentBean) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RESTART_LOCATION));
                final AlertDialog builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).create();
                builder.show();
                Window window = builder.getWindow();
                window.setDimAmount(0);
                window.setContentView(R.layout.look_qr);
                ImageView ivQr = (ImageView) window.findViewById(R.id.iv_look_qr);
                FrameLayout flCancle = (FrameLayout) window.findViewById(R.id.fl_cancle);
                LookQrBean lookQrBean = new LookQrBean();
                if (mDao != null && mDao.getCustomer() != null) {
                    lookQrBean.customerPhone = mDao.getCustomer().phone;
                }
                lookQrBean.devId = netAppointmentBean.dev.id;
                lookQrBean.longitude = mDao.localDao.getLocateCache().lon + "";
                lookQrBean.latitude = mDao.localDao.getLocateCache().lat + "";
                String encode = Base64Util.encode(new Gson().toJson(lookQrBean).getBytes());
                Bitmap bitmap = QRCodeUtil.createQRImage(encode, DimenUtils.dip2px(getActivity(), 100), DimenUtils.dip2px(getActivity(), 100));
                if (bitmap != null) {
                    ivQr.setImageBitmap(bitmap);
                    ivQr.invalidate();
                }
                builder.setCanceledOnTouchOutside(false);
                flCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        if (event.MsgType == LOOK_HOUSE_CHANGE) {
            if (mDao.isLogin()) {
                mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
            }
        } else if (event.MsgType == LOGIN_STATE_CHANGE) {
            if (mDao.isLogin()) {
                tvEmptyTitle.setText("暂无网上预约");
                tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
                mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
            } else {
                tvEmptyTitle.setText("立即登录");
                tvEmptyTitle.setTextColor(getResources().getColor(R.color.gradient_end));
                List<NetAppointmentBean> netList = new ArrayList<>();
                showNetData(netList);
                tvEmptyTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 2);
                    }
                });
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            tvEmptyTitle.setText("暂无网上预约");
            tvEmptyTitle.setTextColor(getResources().getColor(R.color.black));
            if (mDao.isLogin())
                mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
        }
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
    public void showDelSuc() {
        CustomerUtils.showTip(getActivity(), "预约已取消");
        refresh.setRefreshing(true);
        if (mDao.isLogin()) {
            mPresenter.requestLookHouse(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
        } else {
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void showFail() {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
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
}
