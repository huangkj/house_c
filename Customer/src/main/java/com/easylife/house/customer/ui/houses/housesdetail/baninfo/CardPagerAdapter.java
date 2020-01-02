package com.easylife.house.customer.ui.houses.housesdetail.baninfo;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BuildingInfoBean;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.card.CardAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<BuildingInfoBean> mData;
    private float mBaseElevation;

    public CardPagerAdapter(List<BuildingInfoBean> mData) {
        this.mData = mData;
        mViews = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            mViews.add(null);
        }
    }

    public void addCardItem(BuildingInfoBean item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.ban_info_adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(BuildingInfoBean item, View view) {
        TextView tvBan = (TextView) view.findViewById(R.id.tv_ban);
        TextView tvSale = (TextView) view.findViewById(R.id.tv_sale);
        TextView tvOpen = (TextView) view.findViewById(R.id.tv_open_value);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok_value);
        TextView tvBuild = (TextView) view.findViewById(R.id.tv_build_type_value);
        TextView tvProperty = (TextView) view.findViewById(R.id.tv_property_value);
        TextView tvUnit = (TextView) view.findViewById(R.id.tv_unit_value);
        TextView tvLadder = (TextView) view.findViewById(R.id.tv_ladder_value);
        TextView tvFloor = (TextView) view.findViewById(R.id.tv_floor_value);
        TextView tvSaling = (TextView) view.findViewById(R.id.tv_saling_value);

        tvBan.setText(item.buildName);
        switch (item.buildingStatus) {
            case "1":
                tvSale.setText("已售");
                break;
            case "2":
                tvSale.setText("可售");
                break;
            case "3":
                tvSale.setText("待售");
                break;
            case "4":
                tvSale.setText("不可售");
                break;
        }
        //建筑类型
        if (TextUtils.isEmpty(item.buildType)) {
            tvBuild.setText("暂无数据");
        } else {
            switch (item.buildType) {
                case "0":
                    tvBuild.setText("低层");
                    break;
                case "1":
                    tvBuild.setText("多层");
                    break;
                case "2":
                    tvBuild.setText("小高层");
                    break;
                case "3":
                    tvBuild.setText("高层");
                    break;
                case "4":
                    tvBuild.setText("超高层");
                    break;
                case "5":
                    tvBuild.setText("板楼");
                    break;
            }
        }

        tvFloor.setText(item.floorNum + "层");
        tvLadder.setText(item.sROH);//1梯2户
        if (!TextUtils.isEmpty(item.buildOpenTime)) {
            if (item.buildOpenTime.equals("0")) {
                tvOpen.setText("暂无数据");//开盘
            } else {
                tvOpen.setText(CustomerUtils.dateTransSdf(Long.parseLong(item.buildOpenTime)));//开盘
            }
        } else {
            tvOpen.setText("暂无数据");
        }

        if (!TextUtils.isEmpty(item.liveOpenTime)) {
            if (item.liveOpenTime.equals("0")) {
                tvOk.setText("暂无数据");//交房
            } else {
                tvOk.setText(CustomerUtils.dateTransSdf(Long.parseLong(item.liveOpenTime)));//交房
            }
        } else {
            tvOk.setText("暂无数据");
        }

        if (TextUtils.isEmpty(item.decorateLevel)) {
            tvProperty.setText("暂无数据");
        } else {
            tvProperty.setText(item.decorateLevel);//装修标准
        }

        if(TextUtils.isEmpty(item.unitNum)){
            tvUnit.setText("暂无数据");
        }else {
            tvUnit.setText(item.unitNum);//单元号
        }
        if ("0".equals(item.saleCount)) {
            tvSaling.setText("暂无数据");
        } else {
            tvSaling.setText(item.saleCount + "套");//在售房源
        }


    }

}
