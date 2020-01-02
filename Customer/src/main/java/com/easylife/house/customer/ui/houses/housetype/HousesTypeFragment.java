package com.easylife.house.customer.ui.houses.housetype;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HousesTypeAdapter;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.HousesAndTypeActivity;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.HousesTypeDetailActivity;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表
 */

public class HousesTypeFragment extends MVPBaseFragment<HousesTypeContract.View, HousesTypePresenter> implements HousesTypeContract.View {


    @Bind(R.id.recycle)
    RecyclerView recycle;

    @Bind(R.id.radioGroup)
    FlowViewGroup radioGroup;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;

    private int type;

    private List<HousesTypeBean> typeBeanList = new ArrayList<>();
    private List<HousesTypeBean.HouseLayoutDataBean> allList;
    private HousesTypeAdapter mAdapter;


    public static HousesTypeFragment newInstance() {
        HousesTypeFragment myFragment = new HousesTypeFragment();
        return myFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_houses_type;
    }

    @Override
    public void initViews() {
        if(!TextUtils.isEmpty(((HousesAndTypeActivity)getActivity()).getDev_id())){
            mPresenter.requestHousesTypeList(((HousesAndTypeActivity)getActivity()).getDev_id());
        }
        mAdapter = new HousesTypeAdapter(R.layout.houses_type_list_item2, allList);
//        View headView = View.inflate(getActivity(), R.layout.houses_type_recycle_top, null);
//        mAdapter.addHeaderView(headView);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(mAdapter);

        recycle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = null;
                if(type == -1){
                    houseLayoutDataBean = allList.get(position);
                }else {
                    houseLayoutDataBean = typeBeanList.get(type).houseLayoutData.get(position);
                }
                startActivity(new Intent(getActivity(), HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL",houseLayoutDataBean)
                        .putExtra("BASE_BEAN",((HousesAndTypeActivity)getActivity()).getBaseBean()));
            }
        });

        ((HousesAndTypeActivity)getActivity()).setNoNetTryRequestData(new HousesAndTypeActivity.NoNetTryRequestDataType() {
            @Override
            public void tryRequestDataHouseType() {
                mPresenter.requestHousesTypeList(((HousesAndTypeActivity)getActivity()).getDev_id());
            }
        });

    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showSuccess(List<HousesTypeBean> beanList) {

        if(beanList == null || beanList.size() == 0){
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            type = -1;//默认type 是全部
            typeBeanList = beanList;
            allList = new ArrayList<>();
            TextView text = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.type_fragment_flow, radioGroup, false);

            radioGroup.addView(text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChoosed(-1);
                }
            });
            for (int i = 0; i < beanList.size(); i++) {
                HousesTypeBean housesTypeBean = beanList.get(i);
                TextView text1 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.type_fragment_flow, radioGroup, false);
                switch (housesTypeBean.name){
                    case "1居":
                        text1.setText("一居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "2居":
                        text1.setText("二居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "3居":
                        text1.setText("三居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "4居":
                        text1.setText("四居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "5居":
                        text1.setText("五居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "6居":
                        text1.setText("六居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "7居":
                        text1.setText("七居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "8居":
                        text1.setText("八居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "9居":
                        text1.setText("九居("+housesTypeBean.houseLayoutData.size()+")");
                        break;
                    case "10居":
                        text1.setText("十居("+housesTypeBean.houseLayoutData.size()+")");
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
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);

            text.setText("全部("+allList.size()+")");
            mAdapter.setNewData(allList);
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
    }

    @Override
    public void showCollect() {

    }

    @Override
    public void showDelCollectSucc() {

    }

    @OnClick({R.id.radioGroup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioGroup:
                break;
        }
    }

    /**
     * 户型列表点击哪个类型
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

        if(type == -1){
            if(allList != null)
            mAdapter.setNewData(allList);
        }else {
            mAdapter.setNewData(typeBeanList.get(type).houseLayoutData);
        }

    }
}
