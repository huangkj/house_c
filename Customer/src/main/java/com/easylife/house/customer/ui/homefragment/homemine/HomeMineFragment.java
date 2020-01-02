package com.easylife.house.customer.ui.homefragment.homemine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.findhouse.FindHouseActivity;
import com.easylife.house.customer.ui.mine.AccountSettingsActivity;
import com.easylife.house.customer.ui.mine.MyCollectActivity;
import com.easylife.house.customer.ui.mine.MyRecommendListActivity;
import com.easylife.house.customer.ui.mine.brokerage.MyBrokerageActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.calculator.taxes.TaxesActivity;
import com.easylife.house.customer.ui.mine.integral.IntegralActivity;
import com.easylife.house.customer.ui.mine.invitefriend.InviteFriendActivity;
import com.easylife.house.customer.ui.mine.order.OrderListActivity;
import com.easylife.house.customer.ui.mine.prize.PrizeActivity;
import com.easylife.house.customer.ui.mine.sign.SignActivity;
import com.easylife.house.customer.ui.mine.userinfo.UserInfoActivity;
import com.easylife.house.customer.ui.pub.StoreWebActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.photoview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.IM_SAVEUSERINFO_NAME;
import static com.easylife.house.customer.event.MessageEvent.LOGIN_STATE_CHANGE;

public class HomeMineFragment extends MVPBaseFragment<HomeMineContract.View, HomeMinePresenter> implements HomeMineContract.View, RequestManagerImpl {

    @Bind(R.id.imgUserHeader)
    RoundedImageView imgUserHeader;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvScore)
    TextView tvScore;
    @Bind(R.id.layUserInfoContent)
    RelativeLayout layUserInfoContent;
    @Bind(R.id.layRecommend)
    LinearLayout layRecommend;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_mine;
    }

    @Override
    public void initViews() {
        mPresenter.getUserInfo();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                mPresenter.getUserInfo();
            }
        });
    }

    public static HomeMineFragment newInstance() {
        HomeMineFragment fragment = new HomeMineFragment();
        return fragment;
    }

    @Override
    public void initUserInfo(Customer customer) {
        if (customer == null) {
            tvUserName.setText("注册/登录");
            CacheManager.initImageUserHeader(getActivity(), imgUserHeader, null);
            tvScore.setText(null);
            layRecommend.setVisibility(View.GONE);
        } else {
            tvUserName.setText(customer.username);
            CacheManager.initImageUserHeader(getActivity(), imgUserHeader, customer.headimg);
            tvScore.setText(customer.myPoint);
            // 网络推客才显示推荐有礼
            layRecommend.setVisibility("1".equals(customer.common) ? View.VISIBLE : View.GONE);


            //登录注册后，更新城市信息
            SearchSingleton singleton = SearchSingleton.getIstance();
            Log.d("@@", "singleton city:  " + singleton.city);
            Log.d("@@", "customer.city:  " + customer.city);
            if (customer != null && TextUtils.isEmpty(customer.city)) {//如果用户没有城市信息，更新之
                mDao.updateCity(0, customer.phone, singleton.city, this);
                Log.d("@@", "updateCity  " + "customer.phone:" + customer.phone + " singleton.city: " + singleton.city);
            }




        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2:
                    mPresenter.getUserInfo();
                    break;
            }
        }
    }

    @OnClick({R.id.btnMineRecommend, R.id.btnMyCommission, R.id.btnRecommend,
            R.id.imgUserHeader, R.id.layUserInfoContent, R.id.tvUserName, R.id.btnBuyHouse, R.id.btnCollect,
            R.id.btnOrders, R.id.btnSettings, R.id.btnRecommen, R.id.tvToolLoan, R.id.tvToolTaxes,
            R.id.btnInvite, R.id.btnPrize, R.id.btnSign, R.id.layScore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRecommend:
                //   我要推荐
//                if (!mPresenter.isLogin()) {
//                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
//                    return;
//                }
//                if (!ValidatorUtils.isMobile(mDao.getCustomer().phone)) {
//                    showTip("请先绑定手机号");
//                    return;
//                }
                if (mPresenter.isLogin()) {
                    WebViewActivity.startActivity(getActivity(), "推荐有礼",
                            Constants.getCTOBUrl(mDao.getCustomer().username, mDao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(getActivity(), "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;
            case R.id.btnMineRecommend:
                //    我的推荐
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                if (!ValidatorUtils.isMobile(mDao.getCustomer().phone)) {
                    showTip("请先绑定手机号");
                    return;
                }
                MyRecommendListActivity.startActivity(getActivity(), 0);
                break;
            case R.id.btnMyCommission:
                //    我的佣金
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivity(new Intent(getActivity(), MyBrokerageActivity.class));
                break;
            case R.id.imgUserHeader:
            case R.id.tvUserName:
            case R.id.layUserInfoContent:
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                HomeMineFragment.this.startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), 2);
                break;
            case R.id.btnSettings:
                AccountSettingsActivity.startActivity(getActivity(), 0);
                break;
            case R.id.btnBuyHouse:
//                if (!mPresenter.isLogin()) {
//                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
//                    return;
//                }
                FindHouseActivity.startActivity(getActivity(), 0);
//                startActivityForResult(new Intent(getActivity(), MineBuyHouseActivity.class), 0);
                break;
            case R.id.btnCollect:
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivityForResult(new Intent(getActivity(), MyCollectActivity.class), 0);
                break;
            case R.id.btnOrders:
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivityForResult(new Intent(getActivity(), OrderListActivity.class), 0);
                break;
            case R.id.btnRecommen:
                // 积分商场
                if (!mPresenter.isLogin()) {
//                                startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                    StoreWebActivity.startActivity(getActivity(), "积分商城",
                            Constants.WEB_INTEGRAL_STORE);
                    return;
                }
                Customer customer = mDao.localDao.getCustomer();
                StoreWebActivity.startActivity(getActivity(), "积分商城",
                        Constants.WEB_INTEGRAL_STORE + "?userId=" + customer.id + "&userName=" + customer.username + "&userPhone=" + customer.phone
                );
                break;
            case R.id.tvToolLoan:
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
                break;
            case R.id.tvToolTaxes:
                TaxesActivity.startActivity(getActivity(), 0);
                break;
            case R.id.btnInvite:
                //  邀请好友
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivity(new Intent(getActivity(), InviteFriendActivity.class));
                break;
            case R.id.btnPrize:
                //  我的奖品
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivity(new Intent(getActivity(), PrizeActivity.class));
                break;
            case R.id.btnSign:
                //  每日签到
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivity(new Intent(getActivity(), SignActivity.class));
                break;
            case R.id.layScore:
                //我的积分
                if (!mPresenter.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                startActivity(new Intent(getActivity(), IntegralActivity.class));
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {
        if (event.MsgType == LOGIN_STATE_CHANGE) {
            Log.d("@@", "LOGIN_STATE_CHANGE");
            mPresenter.getUserInfo();

        } else if (event.MsgType == IM_SAVEUSERINFO_NAME) {
            initUserInfo(null);
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
