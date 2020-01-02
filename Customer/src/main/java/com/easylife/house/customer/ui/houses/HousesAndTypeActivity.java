package com.easylife.house.customer.ui.houses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HousesPagerAdapter;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.housesdetail.HousesDetailFragment;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housetype.HousesTypeFragment;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;

/**
 * 楼盘详情和户型activity
 */
@RuntimePermissions
public class HousesAndTypeActivity extends MVPBaseActivity<HousesAndTypeContract.View, HousesAndTypePresenter> implements HousesAndTypeContract.View {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    private String dev_id;
    private SearchSingleton searchSingleton;
    /**
     * 合作模式，等于3 为简化版样式
     */
    public int coop;

    public String getDev_id() {
        return dev_id;
    }

    public String getDevName() {
        if (baseBean == null)
            return null;
        return baseBean.devName;
    }

    public String getDevPhone() {
        if (baseBean == null)
            return null;
        return baseBean.estatePhone;
    }


    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Bind(R.id.id_tablayout)
    TabLayout tabLayout;
    @Bind(R.id.id_viewpager)
    ViewPager viewPager;
    @Bind(R.id.rl_collect)
    LinearLayout rlCollect;
    @Bind(R.id.iv_collect)
    CheckBox ivCollect;

    @Bind(R.id.rl_collect2)
    LinearLayout rlCollect2;
    @Bind(R.id.iv_collect2)
    CheckBox ivCollect2;

    @Bind(R.id.rl_ask)
    LinearLayout rlAsk;
//    @Bind(R.id.rl_sub)
//    LinearLayout rlSub;

    @Bind(R.id.bottom)
    LinearLayout bottom;

    @Bind(R.id.bottomSimple)
    LinearLayout bottomSimple;


    //    @Bind(R.id.rl_sub2)
//    LinearLayout rlSub2;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_call)
    TextView tvCall;

    @Bind(R.id.tv_call2)
    TextView tvCall2;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
    private HousesDetailBaseBean baseBean;
    private List<String> alredyList;
    private boolean isCollect;

    public HousesDetailBaseBean getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(HousesDetailBaseBean baseBean) {
        this.baseBean = baseBean;
    }

    public NoNetTryRequestData noNetTryRequestData;

    public interface NoNetTryRequestData {
        void tryRequestData();
    }

    public void setNoNetTryRequestData(NoNetTryRequestData noNetTryRequestData) {
        this.noNetTryRequestData = noNetTryRequestData;
    }

    public NoNetTryRequestDataType noNetTryRequestDataType;

    public interface NoNetTryRequestDataType {
        void tryRequestDataHouseType();
    }

    public void setNoNetTryRequestData(NoNetTryRequestDataType noNetTryRequestDataType) {
        this.noNetTryRequestDataType = noNetTryRequestDataType;
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_houses_and_type_activity, null);
    }

    public static void startActivity(Activity activity, String devID, boolean isCollect, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousesAndTypeActivity.class)
                        .putExtra("DEV_ID", devID).putExtra("isCollect", isCollect)
                , requestCode);
    }

    public static void startActivity(Activity activity, String devID, int coop, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousesAndTypeActivity.class)
                        .putExtra("DEV_ID", devID).putExtra("coop", coop)
                , requestCode);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        isCollect = getIntent().getBooleanExtra("isCollect", false);
        dev_id = getIntent().getStringExtra("DEV_ID");
        coop = getIntent().getIntExtra("coop", 0);
        searchSingleton = SearchSingleton.getIstance();

        updateCoopStyle();

        getLookHouseList();

        mTitles.add("楼盘");
        mTitles.add("户型");
        setTabLayout(tabLayout);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        HousesDetailFragment housesDetailFragment = HousesDetailFragment.newInstance();
        HousesTypeFragment housesTypeFragment = HousesTypeFragment.newInstance();
        fragmentsList.add(housesDetailFragment);
        fragmentsList.add(housesTypeFragment);
        HousesPagerAdapter myPagerAdapter = new HousesPagerAdapter(getSupportFragmentManager(), fragmentsList, mTitles);
        tabLayout.setSelected(true);
        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        /**
         * 判断是否收藏此楼盘
         */


        if (coop == 3) {
            if (searchSingleton.collectList.contains(dev_id)) {
                ivCollect2.setChecked(true);
            } else {
                ivCollect2.setChecked(false);
            }
        } else {


            if (searchSingleton.collectList.contains(dev_id)) {
                ivCollect.setChecked(true);
            } else {
                ivCollect.setChecked(false);
            }
        }


        setOnShareListener(new OnShareListener() {
            @Override
            public void onShare() {
                mPresenter.shareIntegration();
            }
        });
    }

    public void updateCoopStyle() {
        if (coop == 3) {
            bottomSimple.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            bottomSimple.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
        }
    }

    public void getLookHouseList() {
        if (dao.getLoginCache() != null) {
            mPresenter.getLookHouseList(dao.getLoginCache().userCode, dao.getLoginCache().token);
        }
    }


    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {
        noNetTryRequestData.tryRequestData();
        noNetTryRequestDataType.tryRequestDataHouseType();
    }

    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.rl_collect, R.id.rl_ask, R.id.tv_look, R.id.tv_call, R.id.iv_back, R.id.iv_share, R.id.ivRecommend, R.id.rl_collect2,
            R.id.tv_call2})
    public void onClick(View view) {
        final LoginResult loginCache = dao.getLoginCache();
        switch (view.getId()) {
            case R.id.rl_collect:
                collect(loginCache, ivCollect);

                break;
            case R.id.rl_collect2://简版
                collect(loginCache, ivCollect2);

                break;
            case R.id.rl_ask:
                if (dao.isLogin()) {
//                    startActivity(new Intent(this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
                } else {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                }

                break;
            case R.id.tv_look:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

//                if (alredyList != null && alredyList.contains(dev_id)) {
//                    CustomerUtils.showTip(this, "您已预约该楼盘");
//                } else {
                if (baseBean != null) {
                    searchSingleton.lookHouse.add(HousesAndTypeActivity.this);
                    startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                }
//                }
                break;
            case R.id.tv_call:
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
            case R.id.tv_call2://简版
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ivRecommend:
                // 推荐有礼
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(dao.getCustomer().username, dao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;
            case R.id.iv_share:
                if (baseBean == null)
                    return;
                if (baseBean.effectId != null) {
                    if (baseBean.effectId.size() == 0) {
                        share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                                baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                                baseBean.feature, "");
                    } else {
                        share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                                baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                                baseBean.feature, baseBean.effectId.get(0).thumbnailImage);
                    }

                } else {
                    share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                            baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                            baseBean.feature, "");
                }
                break;
            //楼盘订阅
//            case R.id.rl_sub:
//                if (!dao.isLogin()) {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;

//            case R.id.rl_sub2:
//                if (!dao.isLogin()) {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;

        }
    }

    private void collect(LoginResult loginCache, CheckBox ivCollect) {
        if (dao.isLogin()) {
            if (!ivCollect.isChecked()) {
                ivCollect.setChecked(true);
                if (!TextUtils.isEmpty(dev_id) && !searchSingleton.collectList.contains(dev_id)) {
                    searchSingleton.collectList.add(dev_id);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    if (loginCache != null) {
                        mPresenter.collectHouse(dev_id, getDevName(), loginCache.userCode, loginCache.token, "0", "");
                    }
                }

            } else {
                ivCollect.setChecked(false);
                if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
                    searchSingleton.collectList.remove(dev_id);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    mPresenter.delCollectHouse(dev_id, getDevName(), loginCache.userCode, loginCache.token, "0", "");
                }

//                        new AlertDialog.Builder(HousesAndTypeActivity.this).setTitle("提示")
//                                .setMessage("是否取消收藏 ")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
//                                            searchSingleton.collectList.remove(dev_id);
//                                            EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
//                                            mPresenter.delCollectHouse(dev_id,loginCache.userCode,loginCache.token,"0","");
//                                        }
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ivCollect.setChecked(true);
//                                    }
//                                })
//                                .show();

            }
        } else {
            ivCollect.setChecked(false);
            startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            LoginResult loginCache = dao.getLoginCache();
            if (loginCache != null) {
                mPresenter.collectHouseList(loginCache.userCode, loginCache.token, "1");
            }
            getLookHouseList();
        }
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */
    public void call(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HousesAndTypeActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HousesAndTypeActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
        dao.pointCall(dev_id, getDevName(), phone);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HousesAndTypeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == MessageEvent.UPDATE_HOUSES_TYPE_DETAIL) {
            viewPager.setCurrentItem(0, false);
        } else if (event.MsgType == HOUSES_INDEXT_COLLECTION) {
            // 刷新收藏状态
            if (event.msgI == 1) {
                if (coop == 3) {
                    ivCollect2.setChecked(true);
                    if (!TextUtils.isEmpty(dev_id) && !searchSingleton.collectList.contains(dev_id)) {
                        searchSingleton.collectList.add(dev_id);
                    }
                } else {
                    ivCollect.setChecked(true);
                    if (!TextUtils.isEmpty(dev_id) && !searchSingleton.collectList.contains(dev_id)) {
                        searchSingleton.collectList.add(dev_id);
                    }
                }
            } else if (event.msgI == 2) {
                if (coop == 3) {
                    ivCollect2.setChecked(false);
                    if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
                        searchSingleton.collectList.remove(dev_id);
                    }
                } else {
                    ivCollect.setChecked(false);
                    if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
                        searchSingleton.collectList.remove(dev_id);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (isCollect) {
            setResult(RESULT_OK);
        }
    }

    /**
     * 已经预约的楼盘
     *
     * @param alredyList
     */
    @Override
    public void showAlreadyHouse(List<String> alredyList) {
        this.alredyList = alredyList;
    }

    @Override
    public void showCollect() {
        CustomerUtils.showTip(this, "收藏成功");
    }

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(HousesAndTypeActivity.this, "取消收藏");
    }

    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {
        List<String> typeCollectList = new ArrayList<>();
        for (CollectionListBean bean : collectionList) {
            typeCollectList.add(bean.houseName);
        }
        if (typeCollectList.contains(dev_id)) {
            if (coop == 3) {
                ivCollect2.setChecked(true);
            } else {
                ivCollect.setChecked(true);
            }
        } else {
            if (coop == 3) {
                ivCollect2.setChecked(false);
            } else {
                ivCollect.setChecked(false);
            }
        }
    }

    @Override
    public void showTipDialog(String tip) {
        DialogUtil.showTip(this, tip);
    }


}
