package com.easylife.house.customer.ui.houses.housepincontrol;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemSelectAdapter;
import com.easylife.house.customer.bean.HouseStatistics;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.ModelHouse;
import com.easylife.house.customer.bean.ResultBuild;
import com.easylife.house.customer.bean.ResultHousePinControl;
import com.easylife.house.customer.bean.ResultStructure;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.houseresource.HouseResourceActivity;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.HousePinControlView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房源销控
 */
public class HousePinControlActivity extends MVPBaseActivity<HousePinControlContract.View, HousePinControlPresenter> implements HousePinControlContract.View {

    @Bind(R.id.tvBuildNumber)
    TextView tvBuildNumber;
    @Bind(R.id.tvHouseStructure)
    TextView tvHouseStructure;
    @Bind(R.id.tvHouseArea)
    TextView tvHouseArea;
    @Bind(R.id.tvHousePrice)
    TextView tvHousePrice;
    @Bind(R.id.btnDetail)
    ButtonTouch btnDetail;
    @Bind(R.id.layHouseDetail)
    LinearLayout layHouseDetail;
    @Bind(R.id.tvHouseStatusDesc)
    TextView tvHouseStatusDesc;
    @Bind(R.id.listFilter)
    RecyclerView listFilter;
    @Bind(R.id.layEmpty)
    View layEmpty;
    @Bind(R.id.layFloat)
    LinearLayout layFloat;

    public static void startActivity(Fragment fragment, String devId, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), HousePinControlActivity.class)
                        .putExtra("devId", devId)
                , requestCode);
    }

    public static void startActivity(Activity activity, String devId, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousePinControlActivity.class)
                        .putExtra("devId", devId)
                , requestCode);
    }

    @Bind(R.id.btnHouseType)
    CheckBox btnHouseType;
    @Bind(R.id.btnHouseBuild)
    CheckBox btnHouseBuild;
    @Bind(R.id.vHousePinControl)
    HousePinControlView vHousePinControl;

    private boolean isFilter;
    private String devId, buildId, buildName, structure = "";
    private String buildNo, HouseId;
    private List<ItemSelect> listRoomCount;
    private List<ItemSelect> listBuildName;
    private ItemSelectAdapter selectAdapter;
    private String devName;

    @Override
    public void showTip(String msg) {
    }

    @Override
    protected void initView() {
        devId = getIntent().getStringExtra("devId");

        listFilter.setLayoutManager(new LinearLayoutManager(this));
        selectAdapter = new ItemSelectAdapter(R.layout.pub_item_select, listRoomCount);
        selectAdapter.setGravity(Gravity.CENTER);
        listFilter.setAdapter(selectAdapter);
        listFilter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                layFloat.setVisibility(View.GONE);

                if (btnHouseBuild.isChecked()) {
                    //   更新参数，查看其它楼栋信息
                    isFilter = false;
                    btnHouseBuild.setChecked(false);
                    buildId = ((ItemSelect) baseQuickAdapter.getItem(i)).getId();
                    buildName = ((ItemSelect) baseQuickAdapter.getItem(i)).getText();

                    btnHouseBuild.setText(buildName);
                } else {
                    isFilter = true;
                    //   根据户型筛选
                    btnHouseType.setChecked(false);
                    structure = ((ItemSelect) baseQuickAdapter.getItem(i)).getText();
                }
                showLoading();
                mPresenter.getHouseData(devId, buildId, structure);
            }
        });

        showLoading();
        mPresenter.getBuildList(devId);

        vHousePinControl.setOnHouseClickListener(new HousePinControlView.OnHouseClickListener() {
            @Override
            public void onHouseClick(ModelHouse house) {
                if (house == null) {
                    layHouseDetail.setVisibility(View.GONE);
                    return;
                }
                LogOut.d("setOnHouseClickListener:\n", house.toString());
                HouseId = house.id;
                layHouseDetail.setVisibility(View.VISIBLE);
                initHouseDetail(house);
            }


        });
    }

    private void initHouseDetail(ModelHouse house) {
        if (house == null)
            return;

        buildNo = house.buildName + "-" + house.unitName + "-" + house.cellName;
        tvBuildNumber.setText(buildNo);
        tvHouseStructure.setText(house.structure);
        tvHouseArea.setText(DoubleFomat.format2(house.fArea) + "㎡");
        double price = Double.parseDouble(house.mTotalPrice);
        tvHousePrice.setText(DoubleFomat.format2(price / 10000) + "万元");
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.house_activity_pin_control, null);
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    protected void tryRequestData() {

    }

    @OnClick({R.id.btnHouseType, R.id.btnHouseBuild, R.id.btnDetail, R.id.layEmpty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHouseType:
                if (layFloat.getVisibility() != View.VISIBLE) {
                    layFloat.setVisibility(View.VISIBLE);
                } else {
                    if (!btnHouseBuild.isChecked())
                        layFloat.setVisibility(View.GONE);
                }
                btnHouseBuild.setChecked(false);
                selectAdapter.setNewData(listRoomCount);
                selectAdapter.setDefaultItem(structure);
                break;
            case R.id.btnHouseBuild:
                if (layFloat.getVisibility() != View.VISIBLE) {
                    layFloat.setVisibility(View.VISIBLE);
                } else {
                    if (!btnHouseType.isChecked())
                        layFloat.setVisibility(View.GONE);
                }
                structure = "";
                btnHouseType.setChecked(false);
                selectAdapter.setNewData(listBuildName);
                selectAdapter.setDefaultItem(new ItemSelect(buildName, buildId));
                break;
            case R.id.layEmpty:
                layFloat.setVisibility(View.GONE);
                btnHouseBuild.setChecked(false);
                btnHouseType.setChecked(false);
                break;
            case R.id.btnDetail:
                HouseResourceActivity.startActivity(activity, buildNo, HouseId, 0);
                break;
        }
    }

    private ResultHousePinControl curData;
    private ArrayList<String> floors;

    @Override
    public void loadFail(NetBaseStatus code) {
        cancelLoading();
        showTip(code.msg);
    }

    @Override
    public void initListData(ResultHousePinControl data) {
        if (isFilter) {
            vHousePinControl.setData(true, curData.hight, curData.width, floors, curData.estateUnitBeanList, data.estateUnitBeanList);
        } else {
            if (TextUtils.isEmpty(devName)) {
                devName = data.devName;
                tvTitle.setText(devName + "房源");
            }
            if (data != null) {
                curData = data;
                if (data.estateUnitBeanList != null && data.estateUnitBeanList.size() != 0) {
                    buildName = data.estateUnitBeanList.get(0).buildName;
                    buildId = data.estateUnitBeanList.get(0).buildId;
                }
                floors = new ArrayList<>();
                if (data.floorList != null && data.floorList.size() != 0) {
                    for (int i = 0; i < data.floorList.size(); i++) {
                        ResultHousePinControl.FloorName name = data.floorList.get(i);
                        floors.add(0, name.floorName);
                    }
                }
                vHousePinControl.setData(true, data.hight, data.width, floors, data.estateUnitBeanList, null);
            }
        }
        cancelLoading();
    }

    @Override
    public void initListResultStructure(List<ResultStructure> data) {
        cancelLoading();
        if (data == null || data.size() == 0)
            return;
        listRoomCount = new ArrayList<>();
        for (ResultStructure s : data) {
            listRoomCount.add(new ItemSelect(s.getText()));
        }
    }

    @Override
    public void initListResultBuild(List<ResultBuild> data) {
        cancelLoading();
        if (data == null || data.size() == 0)
            return;
        listBuildName = new ArrayList<>();
        for (ResultBuild s : data) {
            listBuildName.add(new ItemSelect(s.getText(), s.getId()));
        }

        buildId = listBuildName.get(0).getId();
        buildName = listBuildName.get(0).getText();

        btnHouseBuild.setText(buildName);
        mPresenter.getStructureList(devId, buildId);
        mPresenter.getHouseData(devId, buildId, structure);
    }

    @Override
    public void initListHouseStatistics(HouseStatistics data) {
        cancelLoading();
        if (data == null)
            return;
        tvHouseStatusDesc.setText("楼盘房源概述:  在售:" +
                (TextUtils.isEmpty(data.inthesaleof) ? "0" : data.inthesaleof) +
                "套  已售:" +
                (TextUtils.isEmpty(data.soldout) ? "0" : data.soldout) +
                "套  待售:" +
                (TextUtils.isEmpty(data.forsale) ? "0" : data.forsale) +
                "套  锁定:" +
                (TextUtils.isEmpty(data.lock) ? "0" : data.lock) +
                "套  不可售:" +
                (TextUtils.isEmpty(data.notforsale) ? "0" : data.notforsale) +
                "套");
    }
}
