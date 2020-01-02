package com.easylife.house.customer.ui.houses.housedetailv5;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HousesTypeAdapter;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housetype.HousesTypeContract;
import com.easylife.house.customer.ui.houses.housetype.HousesTypePresenter;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.HousesTypeDetailActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.FlowViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;
import static com.easylife.house.customer.event.MessageEvent.RESTART_HOUSE_COLLECT_STATE;

/**
 * 户型列表
 */

@RuntimePermissions
public class HouseTypeActivity extends MVPBaseActivity<HousesTypeContract.View, HousesTypePresenter> implements HousesTypeContract.View {


    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    @Bind(R.id.radioGroup)
    FlowViewGroup radioGroup;
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.iv_collect)
    CheckBox ivCollect;
    @Bind(R.id.rl_collect)
    LinearLayout rlCollect;
    @Bind(R.id.rl_ask)
    LinearLayout rlAsk;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_call)
    TextView tvCall;
    @Bind(R.id.iv_collect2)
    CheckBox ivCollect2;
    @Bind(R.id.rl_collect2)
    LinearLayout rlCollect2;
    @Bind(R.id.tv_call2)
    TextView tvCall2;
    @Bind(R.id.frBottom)
    FrameLayout frBottom;
    @Bind(R.id.bottom)
    LinearLayout bottom;

    @Bind(R.id.bottomSimple)
    LinearLayout bottomSimple;


    private HousesTypeAdapter mAdapter;
    private List<HousesTypeBean> typeBeanList = new ArrayList<>();
    private List<HousesTypeBean.HouseLayoutDataBean> allList;
    private HousesDetailBaseBean baseBean;
    private SearchSingleton searchSingleton;
    private boolean isCollect;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_house_type, null);
    }


    public static void startActivity(Activity activity, String devID, int coop, HousesDetailBaseBean baseBean, boolean isCollect, int requestCode) {
        Intent intent = new Intent(activity, HouseTypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("baseBean", baseBean);
        intent.putExtras(bundle);
        intent.putExtra("devID", devID);
        intent.putExtra("coop", coop);
        intent.putExtra("isCollect", isCollect);
        activity.startActivityForResult(intent
                , requestCode);
    }


    private String devId;
    private int type;
    private int coop;


    public void updateCoopStyle() {
        if (coop == 3) {
            bottomSimple.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
            ivCollect2.setChecked(isCollect);
        } else {
            bottomSimple.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
            ivCollect.setChecked(isCollect);
        }
    }


    @Override
    public void initView() {
        baseBean = ((HousesDetailBaseBean) getIntent().getSerializableExtra("baseBean"));
        devId = getIntent().getStringExtra("devID");
        coop = getIntent().getIntExtra("coop", 0);
        isCollect = getIntent().getBooleanExtra("isCollect", false);

        updateCoopStyle();
        searchSingleton = SearchSingleton.getIstance();
        mPresenter.requestHousesTypeList(devId);
        mAdapter = new HousesTypeAdapter(R.layout.houses_type_list_item2, allList);
//        View headView = View.inflate(HouseTypeActivity.this, R.layout.houses_type_recycle_top, null);
//        mAdapter.addHeaderView(headView);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);

        recycle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = null;
                if (type == -1) {
                    houseLayoutDataBean = allList.get(position);
                } else {
                    houseLayoutDataBean = typeBeanList.get(type).houseLayoutData.get(position);
                }

                startActivity(new Intent(HouseTypeActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
                        .putExtra("BASE_BEAN", (baseBean)));
//                if (coop == 3) {
//                    startActivity(new Intent(HouseTypeActivity.this, HousesTypeDetailSimpleActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                            .putExtra("BASE_BEAN", (baseBean)));
//                } else {
//                    startActivity(new Intent(HouseTypeActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                            .putExtra("BASE_BEAN", (baseBean)));
//                }
            }
        });

//        ((HousesAndTypeActivity) HouseTypeActivity.this).setNoNetTryRequestData(new HousesAndTypeActivity.NoNetTryRequestDataType() {
//            @Override
//            public void tryRequestDataHouseType() {
//                mPresenter.requestHousesTypeList(((HousesAndTypeActivity) HouseTypeActivity.this).getDev_id());
//            }
//        });

    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showSuccess(List<HousesTypeBean> beanList) {

        if (beanList == null || beanList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            type = -1;//默认type 是全部
            typeBeanList = beanList;
            allList = new ArrayList<>();
            TextView text = (TextView) LayoutInflater.from(HouseTypeActivity.this).inflate(R.layout.type_fragment_flow, radioGroup, false);

            radioGroup.addView(text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChoosed(-1);
                }
            });
            for (int i = 0; i < beanList.size(); i++) {
                HousesTypeBean housesTypeBean = beanList.get(i);
                TextView text1 = (TextView) LayoutInflater.from(HouseTypeActivity.this).inflate(R.layout.type_fragment_flow, radioGroup, false);
                switch (housesTypeBean.name) {
                    case "1居":
                        text1.setText("一居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "2居":
                        text1.setText("二居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "3居":
                        text1.setText("三居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "4居":
                        text1.setText("四居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "5居":
                        text1.setText("五居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "6居":
                        text1.setText("六居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "7居":
                        text1.setText("七居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "8居":
                        text1.setText("八居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "9居":
                        text1.setText("九居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                    case "10居":
                        text1.setText("十居(" + housesTypeBean.houseLayoutData.size() + ")");
                        break;
                }
                radioGroup.addView(text1);
                final int finalI = i;
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isChoosed(finalI);
                    }
                });
                allList.addAll(housesTypeBean.houseLayoutData);
            }
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

            text.setText("全部(" + allList.size() + ")");
            mAdapter.setNewData(allList);
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
    }

    @Override
    public void showCollect() {
        ToastUtils.showShort("收藏成功");
        MessageEvent messageEvent = new MessageEvent(RESTART_HOUSE_COLLECT_STATE);
        messageEvent.setFlag(true);
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public void showDelCollectSucc() {
        ToastUtils.showShort("取消成功");
        MessageEvent messageEvent = new MessageEvent(RESTART_HOUSE_COLLECT_STATE);
        messageEvent.setFlag(false);
        EventBus.getDefault().post(messageEvent);
    }

    @OnClick({R.id.radioGroup, R.id.rl_collect, R.id.rl_collect2, R.id.rl_ask, R.id.tv_look, R.id.tv_call, R.id.tv_call2})
    public void onClick(View view) {
        final LoginResult loginCache = dao.getLoginCache();
        switch (view.getId()) {
            case R.id.radioGroup:
                break;

            //----//
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
                    searchSingleton.lookHouse.add(HouseTypeActivity.this);
                    startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                }
//                }
                break;
            case R.id.tv_call:
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
                break;
            case R.id.tv_call2://简版
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
                break;
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
                        HouseTypeActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HouseTypeActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
        dao.pointCall(devId, baseBean.devName, phone);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void collect(LoginResult loginCache, CheckBox ivCollect) {
        if (dao.isLogin()) {
            if (!ivCollect.isChecked()) {
                ivCollect.setChecked(true);
                if (!TextUtils.isEmpty(devId) && !searchSingleton.collectList.contains(devId)) {
                    searchSingleton.collectList.add(devId);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    if (loginCache != null) {
                        mPresenter.collectHouse(devId, baseBean.devName, loginCache.userCode, loginCache.token, "0", "");
                    }
                }

            } else {
                ivCollect.setChecked(false);
                if (!TextUtils.isEmpty(devId) && searchSingleton.collectList.contains(devId)) {
                    searchSingleton.collectList.remove(devId);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    mPresenter.delCollectHouse(devId, baseBean.devName, loginCache.userCode, loginCache.token, "0", "");
                }


            }
        } else {
            ivCollect.setChecked(false);
            startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
        }
    }

    /**
     * 户型列表点击哪个类型
     *
     * @param type -1 全部 0 3居 1 2居
     */
    public void isChoosed(int type) {
        this.type = type;
//        for (int i = 0; i < radioGroup.getChildCount(); i++) {
//            RadioButton childAt = (RadioButton) radioGroup.getChildAt(i);
//            if (childAt.isChecked()) {
//                childAt.setTextColor(getResources().getColor(R.color.gradient_end));
//            } else {
//                childAt.setTextColor(getResources().getColor(R.color._666565));
//            }
//        }

        if (type == -1) {
            if (allList != null)
                mAdapter.setNewData(allList);
        } else {
            mAdapter.setNewData(typeBeanList.get(type).houseLayoutData);
        }

    }

}
